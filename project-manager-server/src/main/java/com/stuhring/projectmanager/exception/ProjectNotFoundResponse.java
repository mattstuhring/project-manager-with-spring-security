package com.stuhring.projectmanager.exception;

public class ProjectNotFoundResponse {

    private String projectNotFound;

    public ProjectNotFoundResponse(String projectNotFound) {
        this.projectNotFound = projectNotFound;
    }

    public String getProjectNotFound() {
        return projectNotFound;
    }

    public void setProjectNotFound(String projectNotFound) {
        this.projectNotFound = projectNotFound;
    }
}
