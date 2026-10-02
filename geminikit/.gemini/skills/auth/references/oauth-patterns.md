# OAuth2 and JWT Patterns Reference

Canonical patterns for OAuth2 PKCE flow and JWT authentication.

## JWT Authentication (Hono + jose)
```ts
// src/lib/jwt.ts
import { SignJWT, jwtVerify, type JWTPayload } from "jose";

const ACCESS_SECRET = new TextEncoder().encode(process.env.JWT_SECRET!);
const REFRESH_SECRET = new TextEncoder().encode(process.env.JWT_REFRESH_SECRET!);

interface TokenPayload extends JWTPayload {
  userId: string;
  role: "user" | "admin";
}

// Access token: short-lived (15 minutes)
export async function signAccessToken(payload: Omit<TokenPayload, "iat" | "exp">): Promise<string> {
  return new SignJWT(payload)
    .setProtectedHeader({ alg: "HS256" })
    .setIssuedAt()
    .setExpirationTime("15m")
    .setAudience(process.env.JWT_AUDIENCE!)
    .setIssuer(process.env.JWT_ISSUER!)
    .sign(ACCESS_SECRET);
}

// Refresh token: longer-lived (7 days)
export async function signRefreshToken(userId: string): Promise<string> {
  return new SignJWT({ userId, type: "refresh" })
    .setProtectedHeader({ alg: "HS256" })
    .setIssuedAt()
    .setExpirationTime("7d")
    .sign(REFRESH_SECRET);
}

export async function verifyAccessToken(token: string): Promise<TokenPayload> {
  const { payload } = await jwtVerify(token, ACCESS_SECRET, {
    audience: process.env.JWT_AUDIENCE!,
    issuer: process.env.JWT_ISSUER!,
  });
  return payload as TokenPayload;
}

export async function verifyRefreshToken(token: string): Promise<{ userId: string }> {
  const { payload } = await jwtVerify(token, REFRESH_SECRET);
  if (payload.type !== "refresh") throw new Error("Invalid token type");
  return { userId: payload.userId as string };
}
```

## JWT Middleware (Hono)
```ts
// src/middleware/require-auth.ts
import { createMiddleware } from "hono/factory";
import { verifyAccessToken } from "@/lib/jwt";

export const requireAuth = createMiddleware(async (c, next) => {
  const authHeader = c.req.header("Authorization");
  if (!authHeader?.startsWith("Bearer ")) {
    return c.json({ type: "about:blank", title: "Unauthorized", status: 401 }, 401);
  }

  try {
    const token = authHeader.slice(7);
    const payload = await verifyAccessToken(token);
    c.set("user", payload);
    await next();
  } catch (err) {
    return c.json(
      { type: "about:blank", title: "Unauthorized", status: 401, detail: "Token expired or invalid" },
      401
    );
  }
});

export const requireRole = (role: "admin" | "user") =>
  createMiddleware(async (c, next) => {
    const user = c.get("user");
    if (!user || user.role !== role) {
      return c.json({ type: "about:blank", title: "Forbidden", status: 403 }, 403);
    }
    await next();
  });
```

## OAuth2 PKCE Flow
```ts
// src/lib/oauth/pkce.ts
import { randomBytes, createHash } from "crypto";

// Step 1: Generate code verifier (random, 43-128 chars)
export function generateCodeVerifier(): string {
  return randomBytes(32).toString("base64url");
}

// Step 2: Generate code challenge (SHA-256 hash of verifier)
export function generateCodeChallenge(verifier: string): string {
  return createHash("sha256").update(verifier).digest("base64url");
}

// src/routes/auth/github.ts
import { Hono } from "hono";
import { getCookie, setCookie, deleteCookie } from "hono/cookie";
import { generateCodeVerifier, generateCodeChallenge } from "@/lib/oauth/pkce";

const GITHUB_CLIENT_ID = process.env.GITHUB_CLIENT_ID!;
const GITHUB_CLIENT_SECRET = process.env.GITHUB_CLIENT_SECRET!;
const REDIRECT_URI = `${process.env.APP_URL}/auth/github/callback`;

export const githubAuthRouter = new Hono()
  // Step 1: Initiate OAuth flow
  .get("/github", async (c) => {
    const verifier = generateCodeVerifier();
    const challenge = generateCodeChallenge(verifier);
    const state = randomBytes(16).toString("hex");

    // Store verifier and state in secure session cookie
    setCookie(c, "oauth_verifier", verifier, {
      httpOnly: true,
      secure: true,
      sameSite: "Lax",  // Lax (not Strict) to allow redirect from provider
      maxAge: 600,       // 10 minutes
      path: "/",
    });
    setCookie(c, "oauth_state", state, { httpOnly: true, secure: true, sameSite: "Lax", maxAge: 600 });

    const authUrl = new URL("https://github.com/login/oauth/authorize");
    authUrl.searchParams.set("client_id", GITHUB_CLIENT_ID);
    authUrl.searchParams.set("redirect_uri", REDIRECT_URI);
    authUrl.searchParams.set("scope", "read:user user:email");
    authUrl.searchParams.set("state", state);
    // Note: GitHub doesn't support PKCE yet; use state for CSRF protection
    // For providers that support PKCE (Google, etc.):
    // authUrl.searchParams.set("code_challenge", challenge);
    // authUrl.searchParams.set("code_challenge_method", "S256");

    return c.redirect(authUrl.toString());
  })

  // Step 2: Handle callback
  .get("/github/callback", async (c) => {
    const { code, state, error } = c.req.query();

    // CSRF validation
    const storedState = getCookie(c, "oauth_state");
    if (!storedState || storedState !== state) {
      return c.json({ error: "Invalid OAuth state" }, 400);
    }

    if (error) return c.redirect(`/login?error=${encodeURIComponent(error)}`);
    if (!code) return c.json({ error: "Missing authorization code" }, 400);

    deleteCookie(c, "oauth_state");
    deleteCookie(c, "oauth_verifier");

    // Exchange code for access token
    const tokenRes = await fetch("https://github.com/login/oauth/access_token", {
      method: "POST",
      headers: { "Content-Type": "application/json", Accept: "application/json" },
      body: JSON.stringify({ client_id: GITHUB_CLIENT_ID, client_secret: GITHUB_CLIENT_SECRET, code, redirect_uri: REDIRECT_URI }),
    });
    const { access_token } = await tokenRes.json();

    // Fetch user profile
    const userRes = await fetch("https://api.github.com/user", {
      headers: { Authorization: `Bearer ${access_token}`, "User-Agent": "MyApp/1.0" },
    });
    const githubUser = await userRes.json();

    // Upsert user in database
    const user = await db.users.upsert({
      where: { githubId: String(githubUser.id) },
      create: { githubId: String(githubUser.id), name: githubUser.name, email: githubUser.email },
      update: { name: githubUser.name },
    });

    // Issue app tokens
    const accessToken = await signAccessToken({ userId: user.id, role: user.role });
    const refreshToken = await signRefreshToken(user.id);

    // Store refresh token in HttpOnly cookie
    setCookie(c, "refresh_token", refreshToken, {
      httpOnly: true,
      secure: true,
      sameSite: "Strict",
      maxAge: 7 * 24 * 60 * 60,
      path: "/",
    });

    return c.redirect(`/dashboard?token=${accessToken}`);
  });
```

## Token Refresh Endpoint
```ts
// src/routes/auth/refresh.ts
.post("/refresh", async (c) => {
  const refreshToken = getCookie(c, "refresh_token");
  if (!refreshToken) return c.json({ error: "No refresh token" }, 401);

  try {
    const { userId } = await verifyRefreshToken(refreshToken);
    const user = await db.users.findUnique({ where: { id: userId } });
    if (!user) return c.json({ error: "User not found" }, 401);

    // Rotate refresh token on each use (prevents replay attacks)
    const newRefreshToken = await signRefreshToken(userId);
    const newAccessToken = await signAccessToken({ userId: user.id, role: user.role });

    setCookie(c, "refresh_token", newRefreshToken, {
      httpOnly: true, secure: true, sameSite: "Strict",
      maxAge: 7 * 24 * 60 * 60, path: "/",
    });

    return c.json({ accessToken: newAccessToken });
  } catch {
    deleteCookie(c, "refresh_token");
    return c.json({ error: "Invalid refresh token" }, 401);
  }
})
```

## Better Auth Integration
```ts
// src/lib/auth.ts (Better Auth v1)
import { betterAuth } from "better-auth";
import { drizzleAdapter } from "better-auth/adapters/drizzle";
import { db } from "@/db";

export const auth = betterAuth({
  database: drizzleAdapter(db, { provider: "pg" }),
  emailAndPassword: { enabled: true, requireEmailVerification: true },
  socialProviders: {
    github: { clientId: process.env.GITHUB_CLIENT_ID!, clientSecret: process.env.GITHUB_CLIENT_SECRET! },
    google: { clientId: process.env.GOOGLE_CLIENT_ID!, clientSecret: process.env.GOOGLE_CLIENT_SECRET! },
  },
  session: {
    expiresIn: 60 * 60 * 24 * 7,   // 7 days
    updateAge: 60 * 60 * 24,         // Refresh if older than 1 day
    cookieCache: { enabled: true, maxAge: 60 * 5 },  // 5 min client-side cache
  },
});

// Mount in route handler
// app.all("/api/auth/*", (c) => auth.handler(c.req.raw));
```

## Security Checklist
- [ ] Access tokens ≤ 15 minutes expiry
- [ ] Refresh tokens in HttpOnly + Secure + SameSite=Strict cookie
- [ ] PKCE or state parameter for CSRF protection on OAuth
- [ ] Token rotation on refresh (invalidate old refresh token)
- [ ] Rate limiting on /login and /refresh endpoints
- [ ] JWT signed with strong secret (≥ 256-bit entropy)
- [ ] Audience and issuer claims validated on JWT verification
- [ ] No sensitive data in JWT payload (only userId and role)
