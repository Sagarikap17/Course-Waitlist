package com.assessment.course.service;

import com.assessment.course.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseWaitlistService {
    private final CourseOffering courseOffering;
    private final Map<String, Learner> learnerRegistry = new HashMap<>();

    private final Set<String> enrolledLearners = new LinkedHashSet<>();
    private final List<String> waitlist = new LinkedList<>();

    public CourseWaitlistService() {
        this.courseOffering = new CourseOffering("CS301", "Advanced Distributed Systems", 2, "CS101");
        
        // Seed default test learners
        registerLearner(new Learner("L1", "Alice", Set.of("CS101")));
        registerLearner(new Learner("L2", "Bob", Set.of("CS101")));
        registerLearner(new Learner("L3", "Charlie", Set.of("CS101")));
        registerLearner(new Learner("L4", "Diana", Set.of())); // Ineligible
        registerLearner(new Learner("L5", "Eve", Set.of("CS101")));
    }

    public CourseWaitlistService(CourseOffering offering, List<Learner> learners) {
        this.courseOffering = offering;
        if (learners != null) {
            for (Learner learner : learners) {
                registerLearner(learner);
            }
        }
    }

    public synchronized void registerLearner(Learner learner) {
        learnerRegistry.put(learner.getId(), learner);
    }

    public synchronized OperationResult enroll(String learnerId) {
        if (learnerId == null || learnerId.trim().isEmpty()) {
            return new OperationResult(ActionStatus.INVALID_REQUEST, "Learner ID cannot be null or empty.");
        }

        Learner learner = learnerRegistry.get(learnerId);
        if (learner == null) {
            return new OperationResult(ActionStatus.INVALID_REQUEST, "Learner non-existent in system registry.");
        }

        if (enrolledLearners.contains(learnerId)) {
            return new OperationResult(ActionStatus.ALREADY_ENROLLED, "Learner is already enrolled in this course.");
        }

        if (!learner.hasPrerequisite(courseOffering.getRequiredPrerequisiteId())) {
            return new OperationResult(ActionStatus.PREREQUISITE_MISSING, 
                "Learner does not meet required prerequisite: " + courseOffering.getRequiredPrerequisiteId());
        }

        if (enrolledLearners.size() < courseOffering.getCapacity()) {
            enrolledLearners.add(learnerId);
            return new OperationResult(ActionStatus.ENROLLED, "Learner successfully enrolled.");
        }

        if (waitlist.contains(learnerId)) {
            return new OperationResult(ActionStatus.ALREADY_WAITLISTED, "Learner is already on the waitlist.");
        }

        waitlist.add(learnerId);
        return new OperationResult(ActionStatus.WAITLISTED, "Course at capacity. Learner added to waitlist.");
    }

    public synchronized OperationResult drop(String learnerId) {
        if (learnerId == null || learnerId.trim().isEmpty()) {
            return new OperationResult(ActionStatus.INVALID_REQUEST, "Learner ID cannot be null or empty.");
        }

        if (waitlist.contains(learnerId)) {
            waitlist.remove(learnerId);
            return new OperationResult(ActionStatus.DROPPED, "Learner removed from waitlist.");
        }

        if (!enrolledLearners.contains(learnerId)) {
            return new OperationResult(ActionStatus.NOT_ENROLLED, "Learner is not currently enrolled or waitlisted.");
        }

        enrolledLearners.remove(learnerId);
        String promotedId = promoteEarliestEligibleWaitlistedLearner();

        return new OperationResult(ActionStatus.DROPPED, "Learner dropped successfully.", promotedId);
    }

    private String promoteEarliestEligibleWaitlistedLearner() {
        Iterator<String> iterator = waitlist.iterator();
        while (iterator.hasNext()) {
            String candidateId = iterator.next();
            Learner candidate = learnerRegistry.get(candidateId);
            if (candidate != null && candidate.hasPrerequisite(courseOffering.getRequiredPrerequisiteId())) {
                iterator.remove();
                enrolledLearners.add(candidateId);
                return candidateId;
            }
            // Retain ineligible learners in waitlist per documented skipping policy
        }
        return null;
    }

    public synchronized CourseStateView getCourseState() {
        return new CourseStateView(
            courseOffering.getCourseId(),
            courseOffering.getTitle(),
            courseOffering.getCapacity(),
            courseOffering.getRequiredPrerequisiteId(),
            Collections.unmodifiableSet(new LinkedHashSet<>(enrolledLearners)),
            Collections.unmodifiableList(new ArrayList<>(waitlist))
        );
    }
}
