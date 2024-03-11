package com.turingSecApp.turingSec.Request;

public class BugBountyReportDTO {
    private Long id;
    private Long userId; // User ID associated with the report
    // Other fields as needed

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    // Other getters and setters for additional fields
}
