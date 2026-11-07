package com.assessment.course.model;

import java.util.List;
import java.util.Set;

public class CourseStateView {
    private final String courseId;
    private final String title;
    private final int capacity;
    private final String requiredPrerequisiteId;
    private final Set<String> enrolledLearnerIds;
    private final List<String> waitlistedLearnerIds;

    public CourseStateView(String courseId, String title, int capacity, String requiredPrerequisiteId,
                           Set<String> enrolledLearnerIds, List<String> waitlistedLearnerIds) {
        this.courseId = courseId;
        this.title = title;
        this.capacity = capacity;
        this.requiredPrerequisiteId = requiredPrerequisiteId;
        this.enrolledLearnerIds = enrolledLearnerIds;
        this.waitlistedLearnerIds = waitlistedLearnerIds;
    }

    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public int getCapacity() { return capacity; }
    public String getRequiredPrerequisiteId() { return requiredPrerequisiteId; }
    public Set<String> getEnrolledLearnerIds() { return enrolledLearnerIds; }
    public List<String> getWaitlistedLearnerIds() { return waitlistedLearnerIds; }
}
