# SAST Patterns and Secrets Detection Reference

Static analysis patterns for finding security vulnerabilities in code.

## Secrets Detection — Regex Patterns

### High-Confidence Patterns (Critical)
```ts
// src/lib/secrets-scanner.ts
const SECRET_PATTERNS = [
  // AWS
  { name: "AWS Access Key", pattern: /AKIA[0-9A-Z]{16}/g, severity: "critical" },
  { name: "AWS Secret Key", pattern: /[a-zA-Z0-9+/]{40}(?=\s|$)/, severity: "critical" },

  // Generic API Keys
  { name: "Generic API Key", pattern: /(?:api[_-]?key|apikey)\s*[=:]\s*["']([a-zA-Z0-9_\-]{20,})["']/gi, severity: "critical" },
  { name: "Generic Secret", pattern: /(?:secret|password|passwd|pwd)\s*[=:]\s*["']([^"']{8,})["']/gi, severity: "high" },
  { name: "Bearer Token", pattern: /Bearer\s+[a-zA-Z0-9\-._~+/]+=*/g, severity: "high" },

  // Service-specific
  { name: "GitHub Token", pattern: /gh[pousr]_[a-zA-Z0-9]{36}/g, severity: "critical" },
  { name: "Stripe Key", pattern: /sk_(?:live|test)_[0-9a-zA-Z]{24}/g, severity: "critical" },
  { name: "JWT Token", pattern: /eyJ[a-zA-Z0-9_-]+\.eyJ[a-zA-Z0-9_-]+\.[a-zA-Z0-9_-]+/g, severity: "high" },
  { name: "Private Key", pattern: /-----BEGIN (?:RSA |EC )?PRIVATE KEY-----/g, severity: "critical" },
  { name: "Database URL", pattern: /(?:postgres|mysql|mongodb):\/\/[^:]+:[^@]+@/g, severity: "critical" },
  { name: "Slack Token", pattern: /xox[baprs]-[0-9]{10,}-[a-zA-Z0-9]{32}/g, severity: "critical" },
];

// Files to exclude from scan (legitimate test/fixture files)
const EXCLUDE_PATTERNS = [
  /\.test\.[tj]s$/,
  /\.spec\.[tj]s$/,
  /test-fixtures/,
  /node_modules/,
  /\.env\.example$/,
];
```

### Grep Commands for Secret Scanning
```bash
# Hardcoded passwords
grep -rn "password\s*=\s*['\"]" src/ --include="*.ts" --include="*.js"

# AWS keys
grep -rn "AKIA[0-9A-Z]\{16\}" src/

# Private keys
grep -rn "BEGIN.*PRIVATE KEY" src/

# Database URLs with credentials
grep -rn "postgresql://\|mysql://\|mongodb://" src/ | grep -v ".env"

# Hardcoded localhost URLs (may indicate missing env vars)
grep -rn "localhost:5432\|127.0.0.1" src/ --include="*.ts"
```

## Injection Pattern Detection

### SQL Injection Grep Patterns
```bash
# Template literals in SQL queries
grep -rn "SELECT.*\${" src/ --include="*.ts"
grep -rn "INSERT.*\${" src/ --include="*.ts"
grep -rn "WHERE.*\${" src/ --include="*.ts"
grep -rn 'execute(`' src/ --include="*.ts"  # Raw execute with template literal

# String concatenation in queries
grep -rn "SELECT.*+.*req\." src/ --include="*.ts"
grep -rn "\.query(.*+.*params" src/ --include="*.ts"
```

### Command Injection Patterns
```ts
// VULNERABLE: Patterns to flag
const dangerousExecPatterns = [
  /exec\s*\(\s*`[^`]*\$\{/,           // exec with template literal
  /execSync\s*\(\s*`[^`]*\$\{/,       // execSync with template literal
  /spawn\s*\([^,]+,\s*\[[^\]]*\+/,    // spawn with concatenated args
  /eval\s*\(\s*(?:req|res|body|params)/,  // eval with request data
  /Function\s*\(\s*(?:req|res|body)/,     // new Function with request data
];

// SECURE alternatives:
// exec → execFile (no shell expansion)
// eval → never; JSON.parse for data, import for modules
// child_process with user input → validate against allowlist first
```

## Prototype Pollution Detection
```bash
# Grep patterns for prototype pollution
grep -rn "__proto__" src/ --include="*.ts"
grep -rn "constructor\[" src/ --include="*.ts"
grep -rn "prototype\[" src/ --include="*.ts"

# Dangerous merge patterns
grep -rn "Object.assign\|_.merge\|deepmerge" src/ --include="*.ts"
```

```ts
// VULNERABLE: Deep merge with user input
import merge from "deepmerge";
const config = merge(defaultConfig, userInput);  // userInput: {"__proto__": {"admin": true}}

// SECURE: Use structured clone or sanitize input
const config = structuredClone(defaultConfig);
Object.assign(config, sanitizedInput);  // Only assign known keys

// OR: Use json-merge-patch which is safe by design
import { mergePatch } from "json-merge-patch";
```

## ReDoS (Regular Expression Denial of Service)
```ts
// VULNERABLE: Catastrophic backtracking patterns
const emailRegex = /^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/;
// Attack: "aaa...@aaa...aaa..." takes exponential time

// Patterns to flag:
// - Nested quantifiers: (a+)+, (a*)*, (a|b)+
// - Alternation with overlapping patterns: (a|ab)+
// - Long input validation without length limit

// SECURE: Add input length limit before regex
const MAX_EMAIL_LENGTH = 254;  // RFC 5321
if (email.length > MAX_EMAIL_LENGTH) throw new Error("Email too long");
// Then apply regex
```

## Path Traversal Detection
```bash
# Grep for path construction with user input
grep -rn "path\.join\|path\.resolve\|readFileSync\|createReadStream" src/ | grep "req\.\|param\|query\|body"

# Missing path validation
grep -rn "readFileSync\|readFile\b" src/ --include="*.ts" -A2 | grep "req\.\|params\."
```

```ts
// VULNERABLE patterns to detect:
// res.sendFile(`./uploads/${req.params.filename}`)
// fs.readFileSync(`./data/${userInput}`)

// Detection function:
function isPathTraversalVulnerable(code: string): boolean {
  const patterns = [
    /(?:readFile|sendFile|createReadStream)\s*\([^)]*\$\{[^}]*(?:req\.|param|query|body)/,
    /path\.join\s*\([^)]*(?:req\.|param|query|body)/,
  ];
  return patterns.some((p) => p.test(code));
}
```

## XSS Pattern Detection (for Node.js/SSR)
```ts
// VULNERABLE: Unescaped user input in HTML template
const html = `<div>${req.query.name}</div>`;  // XSS via query param
res.send(html);

// VULNERABLE: dangerouslySetInnerHTML in React
<div dangerouslySetInnerHTML={{ __html: userContent }} />

// GREP PATTERNS:
// grep -rn "dangerouslySetInnerHTML" src/ — flag all instances for review
// grep -rn "innerHTML\s*=" src/ — flag DOM manipulation
// grep -rn "\.html(" src/ — jQuery html() method

// SECURE: Use text encoding
import he from "he";  // HTML entity encoder
const safe = he.encode(userInput);
const html = `<div>${safe}</div>`;

// For rich content: Use DOMPurify (browser) or sanitize-html (Node.js)
import sanitizeHtml from "sanitize-html";
const clean = sanitizeHtml(userContent, {
  allowedTags: ["b", "i", "em", "strong", "a", "p"],
  allowedAttributes: { a: ["href"] },
});
```

## Open Redirect Detection
```ts
// VULNERABLE: Redirect to user-provided URL without validation
const redirectUrl = req.query.next as string;
res.redirect(redirectUrl);  // Attack: next=https://evil.com

// GREP PATTERN:
// grep -rn "redirect\|res\.redirect" src/ | grep "req\.\|query\.\|params\."

// SECURE: Validate redirect is relative (starts with /)
function isSafeRedirect(url: string): boolean {
  return url.startsWith("/") && !url.startsWith("//");
}

const next = req.query.next as string;
const redirectTo = isSafeRedirect(next) ? next : "/dashboard";
res.redirect(redirectTo);
```

## SAST Scan Report Template
```markdown
# Security Scan Report

**Date:** {date}
**Target:** {path}
**Scanner:** gk-security-scan

## Executive Summary
- Critical: {count}
- High: {count}
- Medium: {count}
- Total: {count}

## Findings

### [CRITICAL] SQL Injection — src/routes/users.ts:47
**Category:** A03:2021 Injection
**Evidence:** `db.execute(\`SELECT * FROM users WHERE id = ${req.params.id}\`)`
**Exploitability:** High — directly exploitable without auth
**Remediation:**
\`\`\`ts
// Replace with parameterized query:
db.execute("SELECT * FROM users WHERE id = $1", [req.params.id]);
\`\`\`

## Recommendations
1. Immediate: Fix all Critical findings before next deployment
2. Short-term: Address High findings within 1 sprint
3. Process: Add pre-commit secret scanning (Trufflehog/Gitleaks)
```
