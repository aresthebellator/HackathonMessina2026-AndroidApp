# Architectural Evaluation Criteria

Use these for Task 2: Architectural Evaluation.

## Primary Criteria

| Criterion | Definition | Key Question |
|-----------|------------|--------------|
| **Scalability** | Ease of scaling with more users/data. | Can it handle 10x more users without a rewrite? |
| **Maintainability** | Effort required to update, fix bugs, and refactor. | Is it easy for new developers to understand? |
| **Cost** | Operational, licensing, and development time. | Is the ROI justified for this architecture? |
| **Performance** | Latency, throughput, and resource usage. | Does it meet our latency/response time targets? |
| **Availability** | Uptime and fault tolerance. | What happens if one component fails? |
| **Security** | Attack surface and data protection. | Are we adding more security risks? |

## Secondary Criteria

- **Interoperability**: Ease of integration with other systems.
- **Portability**: Effort to move to another cloud or platform.
- **Reusability**: Ability to reuse components in other projects.
- **Time-to-Market**: How fast can we release the first version?
- **Operational Complexity**: Infrastructure management overhead.
- **Skillset Alignment**: Does the team have the skills for this tech stack?

## "The Rule of 3" Evaluation

For any major architectural decision, evaluate at least 3 distinct options against these criteria.
Identify one "Ideal" option, one "Simple/Fast" option, and one "Advanced/Experimental" option.
