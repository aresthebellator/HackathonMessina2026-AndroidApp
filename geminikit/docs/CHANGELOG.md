# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- **Extended Skill Library**: Expanded from 15 core skills to 55 total skills including specialized domains (frontend, backend, mobile, auth, database, CMS, i18n, analytics, media, performance, security, and more)
- **Bridge Pipeline Features**: Enhanced task lifecycle with detailed JSON schema, retry logic (max 3 retries), and configurable timeout handling
- **Skill Lifecycle Management**: Formal skill discovery, registration, versioning, and validation pipeline with `.skill-state.json` tracking
- **Documentation Enhancements**: Completed ONBOARDING.md with prerequisites, hooks overview, and security matrix; added troubleshooting and platform-specific setup guides; documented full bridge architecture and task JSON schema
- **Native Hooks Improvements**: Clarified BeforeAgent hook behavior and context injection mechanism; documented hook library modules and TTL management

### Changed
- **Skills Quality Baseline**: All 55 skills now conform to v2.0.0 structure (Tools, Output Schema, Error Recovery, Gemini-Specific Optimizations)
- **Agent Permission Matrix**: Extended from 7 to 14 agents with complete permission definitions for all agent types
- **Command Reference**: Enhanced documentation of `gk bridge init`, `gk doctor --fix`, and token command options; fixed skill command naming inconsistencies

### Fixed
- **Documentation Accuracy**: Verified all CLI commands against actual implementation (bin/gk.js, src/commands/); confirmed bridge is live production feature
- **Permission Matrix Completeness**: Added missing agents (Comparator, Designer, Documenter, MCP Manager, Maintenance, Support, Tester) to registry
- **Setup Guide Coverage**: Added Windows/Mac/Linux platform-specific notes, troubleshooting section, and upgrade instructions

## [1.2.2] - 2026-04-26
### Changed
- **Skills Quality Rebuild (Phase 05)**: Rebuilt 15 core skills to v2.0.0 with Gemini-native optimizations
  - All skills now include explicit Tools section (Gemini CLI tools with when-to-use guidance)
  - All skills now include Output Schema (structured JSON responses)
  - All skills now include Error Recovery tables (error codes, causes, mitigation)
  - Added Gemini-Specific Optimizations section (long context, google_search, code execution)
- **Research Skill**: Now requires ≥3 google_search queries with citation format; mandatory URL attribution
- **Review Skill**: Added real-time CVE checking via google_search; added modes (--strict, --quick, --api, --security, --perf, --openapi)
- **Deploy Skill**: Auto-detects deployment platforms (Cloudflare, Vercel, Netlify, Docker, Fly.io, Railway, GCP, GitHub Actions)
- **Git Skill**: Enhanced content-pattern secret scanning before commit; improved clean output format
- **Audit Skill**: Added real-time CVE enrichment via google_search for dependency security
- **Ask Skill**: google_search now mandatory for external/library questions; improved information grounding
- **Bug-Fixer Skill**: Added code execution verification for fixes; enhanced Stack Overflow equivalent error lookup
- **Plan Skill**: Now reads entire codebase via long context before planning; leverages 1M token window
- **Debug, Refactor, Analyze, Brainstorm, Document, Intake, Gk-Execute Skills**: Enhanced to leverage Gemini's native capabilities

## [1.2.1] - 2026-04-05
### Added
- **Native Hooks System**: Five lifecycle hooks (SessionStart, AfterModel, PreCompress, AfterTool, SessionEnd) implemented as Node.js scripts in `.gemini/hooks/`
- **Hook Infrastructure**: Three library modules (memory-manager, gemini-summarizer, logger) for hook operations
- **Auto-Summarization**: Background conversation summarization via Gemini API when token threshold (25K) or turn interval (10) reached
- **Execution Audit Trail**: Tool invocation logging in execution.md with timestamp, name, status, and duration
- **Memory Management**: Automatic short-term to long-term memory promotion, entry trimming, and long-term compression (>15 entries)
- **Sensitive Field Redaction**: AfterTool hook automatically masks sensitive keys (token, secret, password, auth, credential)

## [1.2.0] - 2026-03-24
### Added
- **Memory System**: Implementation of auto-persistence, silent summarization, implicit export, and self-healing.
- **Security Framework**: Agent Permission Matrix, forbidden path blacklisting, tool access control, and pre-emptive secret masking.
- **Context Economy**: Token optimization as a core system principle across all tools and skills.

### Changed
- Refactored Layer 5 (Memory) in system architecture to support advanced persistence.
- Updated `SKILLS_GUIDE.md` with security and economy requirements for skill development.

## [0.1.0] - 2026-03-21
### Added
- Initial release of Gemini Kit framework.
