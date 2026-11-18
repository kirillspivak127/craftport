package dev.craftport.config;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record CraftportProperties(Path dataRoot) {
}
