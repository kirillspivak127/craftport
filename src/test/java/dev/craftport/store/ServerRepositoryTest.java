package dev.craftport.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import dev.craftport.config.YamlConfig;
import dev.craftport.domain.LoaderType;

class ServerRepositoryTest {

    @TempDir
    Path tempDir;

    private ServerRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ServerRepository(tempDir, new YamlConfig().yamlMapper());
    }

    @Test
    void readsServerDefinition() throws Exception {
        write("demo", """
                id: demo
                name: Demo
                host: main
                port: 25565
                minecraft: "1.21.1"
                loader:
                  type: FABRIC
                  version: null
                memory: 4G
                motd: hi
                maxPlayers: 10
                whitelist: [Notch]
                ops: [Notch]
                mods:
                  - slug: sodium
                    side: client
                serverProperties:
                  difficulty: normal
                """);

        var found = repository.find("demo");

        assertThat(found).isPresent();
        var server = found.get();
        assertThat(server.id()).isEqualTo("demo");
        assertThat(server.port()).isEqualTo(25565);
        assertThat(server.loader().type()).isEqualTo(LoaderType.FABRIC);
        assertThat(server.mods()).hasSize(1);
        assertThat(server.serverProperties()).containsEntry("difficulty", "normal");
    }

    @Test
    void returnsEmptyWhenServerDoesNotExist() {
        assertThat(repository.find("missing")).isEmpty();
    }

    private void write(String id, String yaml) throws Exception {
        Path dir = Files.createDirectories(tempDir.resolve(id));
        Files.writeString(dir.resolve("server.yaml"), yaml);
    }
}