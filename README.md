# Course Waitlist Engineering Assessment Solution

This repository contains a full Java (Spring Boot) and Angular solution for the Course Waitlist Assessment.

## Project Structure
- `course-waitlist-service/`: Java 17 + Spring Boot backend REST application.
- `course-waitlist-ui/`: Angular 17 standalone frontend application.

## 1. Prerequisites
- **JDK 17** or higher
- **Maven 3.8+**
- **Node.js 18+** & **npm**

## 2. Running the Java Spring Boot Backend
Navigate to `course-waitlist-service/`:
```bash
cd course-waitlist-service
mvn clean test        # Run automated unit and concurrency tests
mvn spring-boot:run   # Start service at http://localhost:8080
```

## 3. Running the Angular Frontend
Navigate to `course-waitlist-ui/`:
```bash
cd course-waitlist-ui
npm install
npm start            # Starts dev server at http://localhost:4200
```

## 4. Default Seed Data for Testing
- **Course**: `CS301` (Capacity: 2, Prerequisite: `CS101`)
- **Learners**:
  - `L1` (Alice): Has `CS101`
  - `L2` (Bob): Has `CS101`
  - `L3` (Charlie): Has `CS101`
  - `L4` (Diana): Missing `CS101` (Ineligible)
  - `L5` (Eve): Has `CS101`

## 5. Automated Test Execution & Coverage
Run tests via Maven:
```bash
mvn test
```
**Tests Cover:**
- Available capacity enrollment
- Full capacity waitlisting
- Duplicate enrollment and waitlist request prevention
- Prerequisite check failure handling
- FIFO waitlist promotion policy & ineligible learner skipping
- Invalid and repeated drop requests
- Multi-threaded concurrency safety
