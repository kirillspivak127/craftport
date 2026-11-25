package dev.craftport.render;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import dev.craftport.domain.Loader;
import dev.craftport.domain.LoaderType;
import dev.craftport.domain.ServerDefinition;

class ComposeRendererTest {
    @Test
    void rendersComposeForDemo() throws Exception {
        var demo = new ServerDefinition("demo", "Demo", "main", 25565, "1.21.1",
                new Loader(LoaderType.FABRIC, null), "6G", "motd", 10,
                List.of("Notch", "Herobrine"), List.of("Notch"), List.of(), Map.of());

        var expected = Files.readString(
                Path.of("src/test/resources/snapshots/docker-compose-demo.yaml"));

        assertThat(ComposeRenderer.render(demo)).isEqualTo(expected);
    }

    @Test
    void addsWhitelistOpsAndLoaderVersion() {
        var def = new ServerDefinition("demo", "Demo", "main", 25565, "1.21.1",
                new Loader(LoaderType.FABRIC, "0.16.5"), "6G", "motd", 10,
                List.of("Notch", "Herobrine"), List.of("Notch"), List.of(), Map.of());

        var yaml = ComposeRenderer.render(def);

        assertThat(yaml).contains("WHITELIST: \"Notch,Herobrine\"");
        assertThat(yaml).contains("OPS: \"Notch\"");
        assertThat(yaml).contains("FABRIC_LOADER_VERSION: \"0.16.5\"");
    }

    @Test
    void omitsOptionalEnvWhenListsEmpty() {
        var demo = new ServerDefinition("demo", "Demo", "main", 25565, "1.21.1",
                new Loader(LoaderType.FABRIC, null), "6G", "motd", 10,
                List.of(), List.of(), List.of(), Map.of());

        var yaml = ComposeRenderer.render(demo);

        assertThat(yaml).doesNotContain("WHITELIST");
        assertThat(yaml).doesNotContain("OPS");
        assertThat(yaml).doesNotContain("FABRIC_LOADER_VERSION");
    }

    @ParameterizedTest
    @CsvSource({
            "FABRIC,   FABRIC_LOADER_VERSION",
            "NEOFORGE, NEOFORGE_VERSION",
            "FORGE,    FORGE_VERSION"
    })
    void mapsLoaderVersionToImageEnv(LoaderType type, String envName) {
        var def = new ServerDefinition("demo", "Demo", "main", 25565, "1.21.1",
                new Loader(type, "1.2.3"), "6G", "motd", 10,
                List.of(), List.of(), List.of(), Map.of());

        assertThat(ComposeRenderer.render(def))
                .contains(envName + ": \"1.2.3\"");
    }
}
