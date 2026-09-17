# Session Management and RBAC Patterns Reference

Canonical patterns for server-side sessions and role-based access control.

## Secure Session Configuration
```ts
// src/lib/session.ts — server-side session with Redis
import { Redis } from "ioredis";
import { createId } from "@paralleldrive/cuid2";
import { serialize, parse } from "cookie";

const redis = new Redis(process.env.REDIS_URL!);
const SESSION_TTL = 7 * 24 * 60 * 60;  // 7 days in seconds
const COOKIE_NAME = "__session";

interface SessionData {
  userId: string;
  role: "user" | "admin";
  createdAt: number;
  lastActiveAt: number;
  ipAddress?: string;
  userAgent?: string;
}

export async function createSession(data: Omit<SessionData, "createdAt" | "lastActiveAt">): Promise<string> {
  const sessionId = createId();
  const session: SessionData = {
    ...data,
    createdAt: Date.now(),
    lastActiveAt: Date.now(),
  };
  await redis.setex(`session:${sessionId}`, SESSION_TTL, JSON.stringify(session));
  return sessionId;
}

export async function getSession(sessionId: string): Promise<SessionData | null> {
  const raw = await redis.get(`session:${sessionId}`);
  if (!raw) return null;
  const session: SessionData = JSON.parse(raw);
  // Slide expiry on activity
  await redis.expire(`session:${sessionId}`, SESSION_TTL);
  return session;
}

export async function destroySession(sessionId: string): Promise<void> {
  await redis.del(`session:${sessionId}`);
}

// Cookie serialization — always HttpOnly, Secure, SameSite=Strict
export function serializeSessionCookie(sessionId: string): string {
  return serialize(COOKIE_NAME, sessionId, {
    httpOnly: true,
    secure: process.env.NODE_ENV === "production",
    sameSite: "strict",
    maxAge: SESSION_TTL,
    path: "/",
  });
}

export function parseSessionId(cookieHeader: string | null): string | null {
  if (!cookieHeader) return null;
  return parse(cookieHeader)[COOKIE_NAME] ?? null;
}
```

## Session Middleware (Hono)
```ts
// src/middleware/session.ts
import { createMiddleware } from "hono/factory";
import { getSession, parseSessionId } from "@/lib/session";

export const sessionMiddleware = createMiddleware(async (c, next) => {
  const cookieHeader = c.req.header("Cookie") ?? null;
  const sessionId = parseSessionId(cookieHeader);

  if (sessionId) {
    const session = await getSession(sessionId);
    if (session) {
      c.set("session", session);
      c.set("sessionId", sessionId);
    }
  }
  await next();
});

export const requireSession = createMiddleware(async (c, next) => {
  const session = c.get("session");
  if (!session) {
    // For API requests — return JSON
    if (c.req.header("Accept")?.includes("application/json")) {
      return c.json({ type: "about:blank", title: "Unauthorized", status: 401 }, 401);
    }
    // For browser requests — redirect to login
    return c.redirect(`/login?next=${encodeURIComponent(c.req.url)}`);
  }
  await next();
});
```

## RBAC — Permission-Based Authorization
```ts
// src/lib/permissions.ts
// Permissions: resource:action pattern
type Permission =
  | "posts:read"
  | "posts:write"
  | "posts:delete"
  | "users:read"
  | "users:write"
  | "users:delete"
  | "admin:access";

type Role = "user" | "moderator" | "admin";

// Role → permissions mapping (single source of truth)
const ROLE_PERMISSIONS: Record<Role, Permission[]> = {
  user: ["posts:read"],
  moderator: ["posts:read", "posts:write", "posts:delete", "users:read"],
  admin: ["posts:read", "posts:write", "posts:delete", "users:read", "users:write", "users:delete", "admin:access"],
};

export function hasPermission(role: Role, permission: Permission): boolean {
  return ROLE_PERMISSIONS[role]?.includes(permission) ?? false;
}

export function requirePermission(permission: Permission) {
  return createMiddleware(async (c, next) => {
    const session = c.get("session");
    if (!session || !hasPermission(session.role as Role, permission)) {
      return c.json({ type: "about:blank", title: "Forbidden", status: 403 }, 403);
    }
    await next();
  });
}

// Usage:
// app.delete("/posts/:id", requireSession, requirePermission("posts:delete"), handler);
// app.get("/admin", requireSession, requirePermission("admin:access"), adminHandler);
```

## Resource-Level Authorization
```ts
// src/routes/posts.ts — owner check (not just role)
.patch("/:id", requireSession, async (c) => {
  const session = c.get("session")!;
  const postId = c.req.param("id");

  const post = await db.posts.findUnique({ where: { id: postId } });
  if (!post) return c.json({ status: 404 }, 404);

  // Owner OR admin can edit
  const canEdit = post.authorId === session.userId || hasPermission(session.role as Role, "posts:write");
  if (!canEdit) return c.json({ type: "about:blank", title: "Forbidden", status: 403 }, 403);

  const data = await c.req.json();
  const updated = await db.posts.update({ where: { id: postId }, data });
  return c.json(updated);
})
```

## CSRF Protection
```ts
// src/lib/csrf.ts — Double-submit cookie pattern
import { randomBytes, timingSafeEqual } from "crypto";

export function generateCsrfToken(): string {
  return randomBytes(32).toString("hex");
}

export function validateCsrfToken(cookieToken: string | undefined, headerToken: string | undefined): boolean {
  if (!cookieToken || !headerToken) return false;
  // Use timing-safe comparison to prevent timing attacks
  try {
    return timingSafeEqual(Buffer.from(cookieToken), Buffer.from(headerToken));
  } catch {
    return false;  // Buffers different lengths
  }
}

// Apply to all state-changing requests (POST, PUT, PATCH, DELETE)
export const csrfProtection = createMiddleware(async (c, next) => {
  if (["GET", "HEAD", "OPTIONS"].includes(c.req.method)) {
    return next();
  }
  const cookieToken = getCookie(c, "__csrf");
  const headerToken = c.req.header("X-CSRF-Token");
  if (!validateCsrfToken(cookieToken, headerToken)) {
    return c.json({ type: "about:blank", title: "CSRF validation failed", status: 403 }, 403);
  }
  await next();
});
```

## Session Invalidation Patterns
```ts
// Logout — destroy session and clear cookie
.post("/logout", requireSession, async (c) => {
  const sessionId = c.get("sessionId")!;
  await destroySession(sessionId);
  deleteCookie(c, "__session", { path: "/" });
  return c.json({ success: true });
})

// Invalidate all sessions for a user (e.g., password change)
async function invalidateAllUserSessions(userId: string): Promise<void> {
  // Pattern: store session IDs per user for mass invalidation
  const sessionIds = await redis.smembers(`user_sessions:${userId}`);
  const pipeline = redis.pipeline();
  sessionIds.forEach((id) => pipeline.del(`session:${id}`));
  pipeline.del(`user_sessions:${userId}`);
  await pipeline.exec();
}
```

## Security Checklist
- [ ] Sessions in Redis, not server memory (survives restart)
- [ ] Session ID is cryptographically random (CUID2 or crypto.randomBytes)
- [ ] Cookies: HttpOnly + Secure + SameSite=Strict
- [ ] CSRF tokens on all state-changing endpoints
- [ ] Session fixation prevention: regenerate session ID after login
- [ ] Session timeout: TTL in Redis + sliding expiry on activity
- [ ] Resource-level auth: check ownership, not just role
- [ ] Permissions defined server-side, not trusted from client
- [ ] Invalidate all sessions on password change or account compromise
