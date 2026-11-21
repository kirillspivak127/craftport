package dev.craftport.render;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import dev.craftport.domain.Loader;
import dev.craftport.domain.LoaderType;
import dev.craftport.domain.ServerDefinition;

class ComposeRendererTest {
    @Test
    void rendersComposeForDemo() throws Exception {
        var demo = new ServerDefinition("demo", "Demo", "main", 25565, "1.21.1",
                new Loader(LoaderType.FABRIC, null), "6G", "motd", 10,
                List.of(), List.of(), List.of(), Map.of());

        var expected = Files.readString(
                Path.of("src/test/resources/snapshots/docker-compose-demo.yaml"));

        assertThat(ComposeRenderer.render(demo)).isEqualTo(expected);
    }
}
