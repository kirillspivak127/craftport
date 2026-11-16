package dev.craftport.domain;

public record Loader(LoaderType type, String version) {
    public Loader {
        if (type == null) {
            throw new IllegalArgumentException("loader type must not be null");
        }
        if (version != null && version.isBlank()) {
            throw new IllegalArgumentException("loader version must not be blank");
        }
    }
}
