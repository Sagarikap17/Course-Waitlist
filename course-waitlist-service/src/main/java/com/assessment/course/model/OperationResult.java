package com.assessment.course.model;

public class OperationResult {
    private final ActionStatus status;
    private final String message;
    private final String promotedLearnerId;

    public OperationResult(ActionStatus status, String message) {
        this(status, message, null);
    }

    public OperationResult(ActionStatus status, String message, String promotedLearnerId) {
        this.status = status;
        this.message = message;
        this.promotedLearnerId = promotedLearnerId;
    }

    public ActionStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPromotedLearnerId() {
        return promotedLearnerId;
    }
}
