package dev.craftport.store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import dev.craftport.domain.ServerDefinition;
import tools.jackson.core.JacksonException;
import tools.jackson.dataformat.yaml.YAMLMapper;

@Repository
public class ServerRepository {
    private final Path dataRoot;
    private final YAMLMapper mapper;

    public ServerRepository(Path dataRoot, YAMLMapper mapper) {
        this.dataRoot = dataRoot;
        this.mapper = mapper;
    }

    public Optional<ServerDefinition> find(String id) {
        Path file = dataRoot.resolve(id).resolve("server.yaml");
        if (!Files.exists(file))
            return Optional.empty();

        try {
            ServerDefinition def = mapper.readValue(file.toFile(), ServerDefinition.class);
            return Optional.of(def);
        } catch (JacksonException | IllegalArgumentException e) {
            throw new ServerConfigException(id, "cannot read " + file + ": " + e.getMessage(), e);
        }
    }

    public List<ServerDefinition> findAll() {
        if (!Files.exists(dataRoot))
            return List.of();

        try (Stream<Path> stream = Files.list(dataRoot)) {
            return stream.filter(Files::isDirectory)
                    .map(dir -> find(dir.getFileName().toString()))
                    .flatMap(Optional::stream)
                    .sorted(Comparator.comparing(ServerDefinition::id))
                    .toList();

        } catch (IOException e) {
            throw new ServerConfigException(null, "cannot list " + dataRoot + ": " + e.getMessage(), e);
        }
    }

    public void save(ServerDefinition server) {
        Path dir = dataRoot.resolve(server.id());
        Path file = dir.resolve("server.yaml");
        try {
            Files.createDirectories(dir);

            mapper.writeValue(file.toFile(), server);
        } catch (IOException | JacksonException e) {
            throw new ServerConfigException(server.id(),
                    "cannot save server '" + server.id() + "' to " + file + ": " + e.getMessage(), e);
        }
    }
}
