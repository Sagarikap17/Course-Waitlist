package com.assessment.course.model;

public class CourseOffering {
    private final String courseId;
    private final String title;
    private final int capacity;
    private final String requiredPrerequisiteId;

    public CourseOffering(String courseId, String title, int capacity, String requiredPrerequisiteId) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Course capacity must be at least 1");
        }
        this.courseId = courseId;
        this.title = title;
        this.capacity = capacity;
        this.requiredPrerequisiteId = requiredPrerequisiteId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getRequiredPrerequisiteId() {
        return requiredPrerequisiteId;
    }
}
