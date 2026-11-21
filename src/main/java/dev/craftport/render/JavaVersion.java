package dev.craftport.render;

public enum JavaVersion {
    JAVA_17("java17"),
    JAVA_21("java21");

    private final String imageTag;

    JavaVersion(String imageTag) {
        this.imageTag = imageTag;
    }

    public String imageTag() {
        return imageTag;
    }
}