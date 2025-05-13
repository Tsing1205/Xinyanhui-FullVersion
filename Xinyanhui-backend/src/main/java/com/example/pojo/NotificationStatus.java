package com.example.pojo;

public enum NotificationStatus {
    New("New"),
    Read("Read"),
    Deleted("Deleted");

    private final String status;

    NotificationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
