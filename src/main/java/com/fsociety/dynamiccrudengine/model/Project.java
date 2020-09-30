package com.fsociety.dynamiccrudengine.model;

public class Project {
    private String nameProject;
    private String groupId;
    private String artifactId;
    private String version;
    private String description;
    private String namePackage;

    public Project() {
    }

    public Project(String nameProject, String groupId, String artifactId, String version, String description, String namePackage) {
        this.nameProject = nameProject;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.description = description;
        this.namePackage = namePackage;
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNamePackage() {
        return namePackage;
    }

    public void setNamePackage(String namePackage) {
        this.namePackage = namePackage;
    }
}
