# Database Schema Design Reference

Canonical patterns for PostgreSQL and MongoDB schema design.

## PostgreSQL Schema with Drizzle ORM
```ts
// src/db/schema/users.ts
import { pgTable, text, timestamp, pgEnum, boolean, index, uniqueIndex } from "drizzle-orm/pg-core";
import { createId } from "@paralleldrive/cuid2";

export const userRoleEnum = pgEnum("user_role", ["user", "admin", "moderator"]);

export const users = pgTable(
  "users",
  {
    id: text("id").primaryKey().$defaultFn(() => createId()),
    name: text("name").notNull(),
    email: text("email").notNull(),
    passwordHash: text("password_hash"),  // null for OAuth users
    role: userRoleEnum("role").notNull().default("user"),
    emailVerifiedAt: timestamp("email_verified_at", { withTimezone: true }),
    deletedAt: timestamp("deleted_at", { withTimezone: true }),  // soft delete
    createdAt: timestamp("created_at", { withTimezone: true }).notNull().defaultNow(),
    updatedAt: timestamp("updated_at", { withTimezone: true }).notNull().defaultNow(),
  },
  (table) => ({
    emailIdx: uniqueIndex("users_email_idx").on(table.email),
    roleIdx: index("users_role_idx").on(table.role),
    deletedAtIdx: index("users_deleted_at_idx").on(table.deletedAt),
  })
);
```

## Relational Schema (One-to-Many)
```ts
// src/db/schema/posts.ts
import { pgTable, text, timestamp, integer, boolean, index, foreignKey } from "drizzle-orm/pg-core";
import { users } from "./users";

export const posts = pgTable(
  "posts",
  {
    id: text("id").primaryKey().$defaultFn(() => createId()),
    authorId: text("author_id").notNull().references(() => users.id, { onDelete: "cascade" }),
    title: text("title").notNull(),
    slug: text("slug").notNull(),
    content: text("content").notNull(),
    excerpt: text("excerpt").notNull(),
    published: boolean("published").notNull().default(false),
    publishedAt: timestamp("published_at", { withTimezone: true }),
    viewCount: integer("view_count").notNull().default(0),
    createdAt: timestamp("created_at", { withTimezone: true }).notNull().defaultNow(),
    updatedAt: timestamp("updated_at", { withTimezone: true }).notNull().defaultNow(),
  },
  (table) => ({
    slugIdx: uniqueIndex("posts_slug_idx").on(table.slug),
    authorIdx: index("posts_author_id_idx").on(table.authorId),
    // Composite index for common query: published posts by author, newest first
    authorPublishedIdx: index("posts_author_published_idx").on(table.authorId, table.publishedAt),
    // Partial index: only for published posts (saves space, faster for common case)
    publishedIdx: index("posts_published_idx")
      .on(table.publishedAt)
      .where(sql`${table.published} = true`),
  })
);
```

## Many-to-Many (Junction Table)
```ts
// src/db/schema/post-tags.ts
import { pgTable, text, primaryKey, index } from "drizzle-orm/pg-core";
import { posts } from "./posts";
import { tags } from "./tags";

export const postTags = pgTable(
  "post_tags",
  {
    postId: text("post_id").notNull().references(() => posts.id, { onDelete: "cascade" }),
    tagId: text("tag_id").notNull().references(() => tags.id, { onDelete: "cascade" }),
  },
  (table) => ({
    pk: primaryKey({ columns: [table.postId, table.tagId] }),
    // Index for reverse lookup (find posts for a tag)
    tagIdx: index("post_tags_tag_id_idx").on(table.tagId),
  })
);
```

## MongoDB Schema (Mongoose)
```ts
// src/models/Product.ts
import { Schema, model, Document } from "mongoose";

interface IProduct extends Document {
  name: string;
  slug: string;
  price: number;
  stock: number;
  categories: string[];
  attributes: Record<string, string | number | boolean>;
  images: { url: string; alt: string; primary: boolean }[];
  createdAt: Date;
  updatedAt: Date;
}

const ProductSchema = new Schema<IProduct>(
  {
    name: { type: String, required: true, trim: true, maxlength: 200 },
    slug: { type: String, required: true, unique: true, lowercase: true },
    price: { type: Number, required: true, min: 0 },
    stock: { type: Number, required: true, min: 0, default: 0 },
    categories: [{ type: String, index: true }],
    attributes: { type: Map, of: Schema.Types.Mixed },
    images: [{
      url: { type: String, required: true },
      alt: { type: String, default: "" },
      primary: { type: Boolean, default: false },
    }],
  },
  {
    timestamps: true,  // auto-manages createdAt and updatedAt
    toJSON: { virtuals: true },
  }
);

// Compound index for product browsing (category + price range)
ProductSchema.index({ categories: 1, price: 1 });
// Text index for search
ProductSchema.index({ name: "text", description: "text" });

export const Product = model<IProduct>("Product", ProductSchema);
```

## Migration Pattern (Drizzle)
```ts
// drizzle/migrations/0003_add_orders_table.ts
import { sql } from "drizzle-orm";
import { pgTable, text, numeric, timestamp, index } from "drizzle-orm/pg-core";

// Always write reversible migrations
export async function up(db: any) {
  await db.execute(sql`
    CREATE TABLE orders (
      id TEXT PRIMARY KEY,
      user_id TEXT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
      status TEXT NOT NULL DEFAULT 'pending'
        CHECK (status IN ('pending', 'confirmed', 'shipped', 'delivered', 'cancelled')),
      total_cents INTEGER NOT NULL CHECK (total_cents >= 0),
      currency TEXT NOT NULL DEFAULT 'USD',
      created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
      updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    )
  `);
  await db.execute(sql`CREATE INDEX orders_user_id_idx ON orders(user_id)`);
  await db.execute(sql`CREATE INDEX orders_status_created_at_idx ON orders(status, created_at DESC)`);
}

export async function down(db: any) {
  await db.execute(sql`DROP TABLE IF EXISTS orders`);
}
```

## Schema Design Checklist
- [ ] All tables have `id` (CUID2 or UUID), `created_at`, `updated_at`
- [ ] Soft delete: `deleted_at TIMESTAMPTZ NULL` if record history needed
- [ ] Every FK column has an index
- [ ] Composite indexes ordered by most-selective column first
- [ ] NOT NULL constraints applied where field is always required
- [ ] UNIQUE constraints at DB level, not just application
- [ ] CHECK constraints for enum-like text columns
- [ ] Cascade behavior defined for FK relationships (`ON DELETE CASCADE|RESTRICT|SET NULL`)
- [ ] No unbounded TEXT arrays — use junction tables for many-to-many

## Common Pitfalls

| Pitfall | Fix |
|---|---|
| Using SERIAL/autoincrement as public ID | Use CUID2 or UUID to prevent enumeration attacks |
| Storing timestamps without timezone | Always use `TIMESTAMPTZ`, never `TIMESTAMP` |
| Missing `updated_at` trigger | Use DB trigger or ORM hook to auto-update |
| JSONB for structured data | JSONB for truly dynamic attributes only; structured data → columns |
| One giant table for everything | Normalize: each concept gets its own table |
