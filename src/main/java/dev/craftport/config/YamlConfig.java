package dev.craftport.config;

import java.nio.file.Path;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.MapperFeature;
import tools.jackson.dataformat.yaml.YAMLMapper;

@Configuration
public class YamlConfig {
    @Bean
    public YAMLMapper yamlMapper() {
        return YAMLMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
    }

    @Bean
    Path dataRoot(CraftportProperties properties) {
        return properties.dataRoot();
    }
}
