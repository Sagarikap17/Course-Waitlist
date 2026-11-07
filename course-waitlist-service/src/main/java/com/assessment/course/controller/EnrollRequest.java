package com.assessment.course.controller;

import jakarta.validation.constraints.NotBlank;

public class EnrollRequest {
    @NotBlank(message = "Learner ID is required")
    private String learnerId;

    public String getLearnerId() { return learnerId; }
    public void setLearnerId(String learnerId) { this.learnerId = learnerId; }
}
