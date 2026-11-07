# Design Specification: Course Waitlist & Registration Service

## 1. System & Service Boundaries
The `CourseWaitlistService` provides a bounded context for course enrollment and FIFO waitlist promotion. 
- **Backend Architecture**: Spring Boot 3.x backend REST service (`/api/course`) handling atomic state transitions.
- **Frontend Architecture**: Angular 17 single-page application providing real-time visibility into course capacity and FIFO queue state.

## 2. Data & API Model
### 2.1 Entities
- **Learner**: `id`, `name`, `completedPrerequisites` (`Set<String>`).
- **CourseOffering**: `courseId`, `title`, `capacity`, `requiredPrerequisiteId`.
- **CourseStateView**: Snapshot of current capacity, enrolled set, and ordered waitlist queue.

### 2.2 API Endpoints
- `GET /api/course/state`: Returns the `CourseStateView`.
- `POST /api/course/enroll`: Body `{ "learnerId": "L1" }`. Returns `OperationResult`.
- `POST /api/course/drop`: Body `{ "learnerId": "L1" }`. Returns `OperationResult` (includes `promotedLearnerId` if a waitlisted learner was promoted).

## 3. Prerequisite & Waitlist Policy Assumptions
1. **Prerequisite Rule**: Learners missing the required prerequisite (`CS101`) are rejected immediately during enrollment requests. They are neither enrolled nor placed on the waitlist.
2. **Ineligible Waitlist Skipping Rule**: If an ineligible learner enters the waitlist (e.g., via administrative backchannel or prerequisite revocation), when a seat opens up upon a drop, the system inspects waitlisted learners in strict FIFO order.
   - If the head of the waitlist is **ineligible**, the system **skips** that learner, **retains** them in their current waitlist relative position for potential future eligibility resolution, and inspects the next waitlisted candidate.
   - The **earliest eligible learner** is promoted to `ENROLLED`.
3. **Idempotency & Duplicate Guards**:
   - Re-enrolling an already enrolled learner returns `ALREADY_ENROLLED` without modifying state.
   - Re-enrolling a waitlisted learner returns `ALREADY_WAITLISTED` without modifying queue order or creating duplicate records.
   - Dropping an unenrolled/non-waitlisted learner returns `NOT_ENROLLED` without affecting capacity or queue state.

## 4. Concurrency & Synchronization Model
- Operations (`enroll`, `drop`, `getCourseState`) are synchronized at the service thread level (`synchronized` monitors on the service instance).
- State transitions (enrolling, waitlisting, and FIFO promotion during drops) execute atomically, preventing race conditions (e.g., over-subscribing capacity under concurrent requests).
