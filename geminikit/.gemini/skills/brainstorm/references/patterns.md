# Architectural Patterns and Styles

Use these for inspiration during Task 1: Solution Brainstorming.

## Macro-Architectures

- **Monolithic Architecture**: Unified deployment, simple to manage for small teams. Best for MVP or low-complexity apps.
- **Microservices**: Independent services, scalable, complex communication. Best for large teams, independent scaling.
- **Serverless (FaaS)**: Event-driven, pay-per-use, no infrastructure management. Best for spikes, intermittent tasks.
- **Service-Oriented Architecture (SOA)**: Reusable services across organizations, enterprise-scale.

## Application Patterns

- **Layered (n-tier)**: Standard separation (UI, Business, Data).
- **Hexagonal / Clean / Onion**: Focus on domain core, external dependencies as adapters. Best for testability and longevity.
- **Event-Driven**: Asynchronous, pub/sub, highly decoupled. Best for high-performance, real-time systems.
- **Micro-kernel (Plug-in)**: Core functionality with extensible plugins. Best for modular apps (e.g., IDEs).

## Data Management

- **Database-per-Service**: Isolation, strict boundaries.
- **Shared Database**: Simplicity, potentially tight coupling.
- **CQRS (Command Query Responsibility Segregation)**: Split read/write models. Best for high-read/low-write or complex read optimizations.
- **Event Sourcing**: State as a series of events. Best for auditing, "time-travel" debugging.
