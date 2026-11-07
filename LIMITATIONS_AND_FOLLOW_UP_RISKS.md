# Scope Limitations & Follow-Up Risks

1. **In-Memory Store Limits**: State is stored in memory (`LinkedHashSet` and `LinkedList`). Node restarts reset data. Production implementation should back this with a relational DB (e.g., PostgreSQL) using explicit row-level locking (`SELECT ... FOR UPDATE`) or transactional isolation.
2. **Prerequisite System Expansion**: Prerequisites are evaluated as exact string matches. Complex expressions (e.g., `(CS101 OR CS102) AND MATH201`) require a dedicated policy/rules evaluation engine.
3. **Single Course Offering Scope**: Bounded strictly to one course offering per problem scope. Multi-course offerings require expanding keying structures in memory/persistence layer.
