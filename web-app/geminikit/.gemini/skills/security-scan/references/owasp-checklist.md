# OWASP Top 10 Security Checklist

Reference for scanning code against OWASP Top 10 (2021) vulnerability categories.

## A01:2021 — Broken Access Control

### What to look for:
```ts
// VULNERABLE: Missing authorization check
app.delete("/posts/:id", async (req, res) => {
  await db.posts.delete({ where: { id: req.params.id } }); // No ownership check!
});

// VULNERABLE: IDOR — using user-provided ID for sensitive data
app.get("/invoices/:id", authenticate, async (req, res) => {
  const invoice = await db.invoices.findUnique({ where: { id: req.params.id } }); // Any user can access any invoice
  return res.json(invoice);
});

// SECURE: Add ownership check
app.get("/invoices/:id", authenticate, async (req, res) => {
  const invoice = await db.invoices.findUnique({
    where: { id: req.params.id, userId: req.user.id }  // Filter by current user
  });
  if (!invoice) return res.status(404).json({ error: "Not found" });
  return res.json(invoice);
});
```

### Grep patterns to detect:
```
grep: findUnique|findById without user ownership filter
grep: req.params.id used directly in DB query
grep: role check on frontend (client-side only)
```

## A02:2021 — Cryptographic Failures

### What to look for:
```ts
// VULNERABLE: MD5/SHA1 for passwords
const hash = crypto.createHash("md5").update(password).digest("hex");
const hash = crypto.createHash("sha1").update(password).digest("hex");

// VULNERABLE: Weak random for security tokens
const token = Math.random().toString(36);  // Not cryptographically secure

// VULNERABLE: Sensitive data in URL query params (logged by servers)
res.redirect(`/reset?token=${resetToken}&email=${email}`);

// SECURE: Use bcrypt/argon2 for passwords; crypto.randomBytes for tokens
import { hash } from "bcrypt";
const passwordHash = await hash(password, 12);  // cost factor 12

import { randomBytes } from "crypto";
const token = randomBytes(32).toString("hex");  // 256 bits of entropy
```

## A03:2021 — Injection

### SQL Injection:
```ts
// VULNERABLE: String concatenation
const result = await db.execute(`SELECT * FROM users WHERE email = '${email}'`);
// Attack: email = "'; DROP TABLE users; --"

// VULNERABLE: Template literal
const query = `SELECT * FROM posts WHERE author = '${req.query.author}'`;

// SECURE: Parameterized queries
const result = await db.execute("SELECT * FROM users WHERE email = $1", [email]);
// OR using ORM
const user = await db.users.findUnique({ where: { email } });
```

### Command Injection:
```ts
// VULNERABLE: exec with user input
const { exec } = require("child_process");
exec(`convert ${req.body.filename} output.jpg`);  // Attack: filename = "; rm -rf /"

// SECURE: Use execFile (no shell expansion) with explicit args
const { execFile } = require("child_process");
execFile("convert", [sanitizedFilename, "output.jpg"]);
```

### Path Traversal:
```ts
// VULNERABLE: User-controlled file path
const content = fs.readFileSync(`./uploads/${req.params.filename}`);
// Attack: filename = "../../../etc/passwd"

// SECURE: Resolve and validate path
const base = path.resolve("./uploads");
const target = path.resolve(base, req.params.filename);
if (!target.startsWith(base)) throw new Error("Invalid path");
const content = fs.readFileSync(target);
```

## A07:2021 — Identification and Authentication Failures

### What to look for:
```ts
// VULNERABLE: Weak session ID (short, predictable)
const sessionId = Date.now().toString();
const sessionId = Math.random().toString();

// VULNERABLE: Missing rate limiting on login
app.post("/login", async (req, res) => {  // No rate limiting = brute force possible
  const user = await authenticateUser(req.body);
  // ...
});

// VULNERABLE: Password comparison vulnerable to timing attack
if (storedPassword === providedPassword) { ... }  // Timing attack possible

// SECURE: Timing-safe comparison
import { timingSafeEqual } from "crypto";
const match = timingSafeEqual(Buffer.from(storedHash), Buffer.from(providedHash));

// SECURE: bcrypt compare (constant time)
import { compare } from "bcrypt";
const match = await compare(providedPassword, storedHash);
```

## A09:2021 — Security Logging and Monitoring Failures

### What NOT to log:
```ts
// VULNERABLE: Logging sensitive data
console.log(`Login attempt: user=${email}, password=${password}`);
console.log(`Token generated: ${accessToken}`);
logger.info({ request: req });  // Logs entire request including auth headers

// SECURE: Log events without sensitive values
logger.info({ event: "login_attempt", email, success: false, ip: req.ip });
logger.info({ event: "token_issued", userId, tokenType: "access" });
```

## Security Headers Checklist
```ts
// src/middleware/security-headers.ts
import { secureHeaders } from "hono/secure-headers";

app.use(secureHeaders({
  contentSecurityPolicy: {
    defaultSrc: ["'self'"],
    scriptSrc: ["'self'", "'nonce-{NONCE}'"],  // Use nonce for inline scripts
    styleSrc: ["'self'", "'unsafe-inline'"],
    imgSrc: ["'self'", "data:", "https:"],
    connectSrc: ["'self'", "https://api.example.com"],
    frameAncestors: ["'none'"],
  },
  xFrameOptions: "DENY",
  xContentTypeOptions: "nosniff",
  referrerPolicy: "strict-origin-when-cross-origin",
  strictTransportSecurity: "max-age=31536000; includeSubDomains; preload",
  permissionsPolicy: "camera=(), microphone=(), geolocation=()",
}));
```

## CORS Configuration
```ts
// VULNERABLE: Wildcard CORS with credentials
app.use(cors({ origin: "*", credentials: true }));  // Allows any site to make credentialed requests

// VULNERABLE: Trusting Origin header blindly
const origin = req.headers.origin;
res.setHeader("Access-Control-Allow-Origin", origin);  // Reflects any origin

// SECURE: Allowlist-based CORS
const ALLOWED_ORIGINS = ["https://app.example.com", "https://admin.example.com"];
app.use(cors({
  origin: (origin) => ALLOWED_ORIGINS.includes(origin ?? "") ? origin : undefined,
  credentials: true,
  methods: ["GET", "POST", "PUT", "PATCH", "DELETE"],
}));
```

## Severity Mapping

| OWASP Category | Common Severity | Notes |
|---|---|---|
| A01 Broken Access Control | Critical/High | IDOR, missing auth → always Critical if user data exposed |
| A02 Crypto Failures | Critical/High | Plaintext passwords = Critical |
| A03 Injection | Critical | SQL/Command injection = always Critical |
| A04 Insecure Design | Medium/High | Architecture issues, business logic flaws |
| A05 Security Misconfiguration | Medium/High | CORS, headers, debug mode in prod |
| A06 Vulnerable Components | Varies | Check CVE score for exact severity |
| A07 Auth Failures | Critical/High | Brute force, weak session = High minimum |
| A08 Software Integrity Failures | High | Supply chain, unsigned packages |
| A09 Logging Failures | Medium | Missing audit trail |
| A10 SSRF | High/Critical | Can lead to internal network access |
