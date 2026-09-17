# Query Optimization Reference

Patterns for writing efficient queries and diagnosing performance issues.

## EXPLAIN ANALYZE (PostgreSQL)
```sql
-- Always use EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT) for query diagnosis
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT u.id, u.name, COUNT(o.id) as order_count
FROM users u
LEFT JOIN orders o ON o.user_id = u.id
WHERE u.created_at > NOW() - INTERVAL '30 days'
GROUP BY u.id, u.name
ORDER BY order_count DESC
LIMIT 20;

-- Key metrics to look for:
-- "Seq Scan" on large tables → needs index
-- "Hash Join" on large sets → consider merge join with sorted indexes
-- Rows estimate vs actual: large discrepancy → run ANALYZE to update stats
-- Buffers: hit (good, from cache) vs read (bad, from disk)
-- "Nested Loop" with large outer rows → often causes N+1
```

## Index Types and Use Cases
```sql
-- B-tree (default) — equality and range queries, sorting
CREATE INDEX users_email_idx ON users(email);
CREATE INDEX orders_created_at_idx ON orders(created_at DESC);

-- Composite — multi-column queries (put equality first, then range/sort)
CREATE INDEX orders_user_status_created_idx ON orders(user_id, status, created_at DESC);
-- Used for: WHERE user_id = ? AND status = ? ORDER BY created_at DESC

-- Partial — index subset of rows (smaller, faster for filtered queries)
CREATE INDEX posts_published_idx ON posts(published_at DESC)
WHERE published = true;
-- Used when you almost always filter on the same condition

-- GIN — JSONB queries, array containment, full-text search
CREATE INDEX products_attrs_idx ON products USING GIN(attributes);
CREATE INDEX posts_search_idx ON posts USING GIN(to_tsvector('english', title || ' ' || content));

-- GiST — geometric data, range types, nearest-neighbor
CREATE INDEX locations_geom_idx ON locations USING GIST(coordinates);

-- BRIN — very large tables with natural sort order (timestamp, ID)
CREATE INDEX events_created_at_brin_idx ON events USING BRIN(created_at);
-- Tiny index size but only useful for sequential scan correlation
```

## Drizzle ORM Optimized Queries
```ts
import { db } from "@/db";
import { users, orders, orderItems } from "@/db/schema";
import { eq, and, gte, desc, count, sql } from "drizzle-orm";

// Select only needed columns (avoid SELECT *)
const userList = await db
  .select({ id: users.id, name: users.name, email: users.email })
  .from(users)
  .where(eq(users.role, "admin"))
  .limit(20);

// JOIN with aggregation — avoids N+1
const usersWithOrderCounts = await db
  .select({
    id: users.id,
    name: users.name,
    orderCount: count(orders.id),
  })
  .from(users)
  .leftJoin(orders, eq(orders.userId, users.id))
  .groupBy(users.id, users.name)
  .orderBy(desc(count(orders.id)))
  .limit(20);

// Cursor-based pagination (more efficient than OFFSET for large tables)
async function getUsersPage(cursor?: string, limit = 20) {
  const conditions = cursor
    ? and(gte(users.id, cursor))  // cursor is last seen ID
    : undefined;

  const result = await db
    .select()
    .from(users)
    .where(conditions)
    .orderBy(users.id)
    .limit(limit + 1);  // fetch one extra to determine if there's a next page

  const hasNext = result.length > limit;
  const items = hasNext ? result.slice(0, limit) : result;
  const nextCursor = hasNext ? items[items.length - 1].id : null;
  return { items, nextCursor };
}

// Raw SQL for complex queries
const topProducts = await db.execute(sql`
  SELECT p.id, p.name, SUM(oi.quantity) as units_sold
  FROM products p
  JOIN order_items oi ON oi.product_id = p.id
  JOIN orders o ON o.id = oi.order_id
  WHERE o.created_at > NOW() - INTERVAL '30 days'
    AND o.status = 'delivered'
  GROUP BY p.id, p.name
  ORDER BY units_sold DESC
  LIMIT 10
`);
```

## MongoDB Aggregation Pipelines
```ts
// Efficient aggregation with proper index usage
const topCategories = await Product.aggregate([
  // $match first to reduce documents early
  { $match: { stock: { $gt: 0 }, "createdAt": { $gte: thirtyDaysAgo } } },
  // $unwind arrays carefully — only when needed
  { $unwind: "$categories" },
  // $group with $sum for counting
  { $group: { _id: "$categories", count: { $sum: 1 }, avgPrice: { $avg: "$price" } } },
  // $sort after aggregation
  { $sort: { count: -1 } },
  { $limit: 10 },
  // $project to rename fields for output
  { $project: { category: "$_id", count: 1, avgPrice: { $round: ["$avgPrice", 2] }, _id: 0 } },
]);

// Use $lookup for joins (equivalent to SQL JOIN)
const ordersWithUsers = await Order.aggregate([
  { $match: { status: "delivered" } },
  {
    $lookup: {
      from: "users",
      localField: "userId",
      foreignField: "_id",
      as: "user",
      // Use pipeline for filtered lookup (avoids loading entire collection)
      pipeline: [{ $project: { name: 1, email: 1 } }],
    },
  },
  { $unwind: { path: "$user", preserveNullAndEmpty: false } },
  { $limit: 100 },
]);
```

## N+1 Query Detection and Fix
```ts
// BAD — N+1: 1 query for posts + N queries for each author
const posts = await db.select().from(posts).limit(20);
const postsWithAuthors = await Promise.all(
  posts.map(async (post) => ({
    ...post,
    author: await db.select().from(users).where(eq(users.id, post.authorId)).limit(1),
  }))
);

// GOOD — Single JOIN query
const postsWithAuthors = await db
  .select({
    postId: posts.id,
    postTitle: posts.title,
    authorId: users.id,
    authorName: users.name,
  })
  .from(posts)
  .innerJoin(users, eq(users.id, posts.authorId))
  .limit(20);
```

## Query Performance Checklist
- [ ] Use `EXPLAIN (ANALYZE, BUFFERS)` before and after optimization
- [ ] Ensure WHERE columns have indexes, especially FK columns
- [ ] SELECT only needed columns, never `SELECT *` in production
- [ ] Use cursor pagination instead of OFFSET for tables > 10k rows
- [ ] Run `VACUUM ANALYZE` after large data changes
- [ ] Check for sequential scans on tables > 1000 rows
- [ ] Monitor slow query log (pg: `log_min_duration_statement = 200`)

## Common Query Anti-Patterns

| Anti-pattern | Fix |
|---|---|
| `SELECT *` in production queries | Specify exact columns needed |
| `OFFSET 1000` for pagination | Use cursor-based pagination |
| Function on indexed column: `WHERE LOWER(email)` | Use functional index or store lowercased value |
| Multiple sequential N+1 queries | JOIN or batch fetch with `WHERE id IN (...)` |
| Missing WHERE clause on DELETE/UPDATE | Always test UPDATE/DELETE with SELECT first |
| Non-sargable: `WHERE YEAR(created_at) = 2024` | Use range: `WHERE created_at BETWEEN '2024-01-01' AND '2025-01-01'` |
