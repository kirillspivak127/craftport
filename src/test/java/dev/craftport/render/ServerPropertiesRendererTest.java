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

class ServerPropertiesRendererTest {
    @Test
    void rendersServerProperties() throws Exception {
        var demo = new ServerDefinition("demo", "Demo", "main", 25565, "1.21.1",
                new Loader(LoaderType.FABRIC, null), "6G", "motd", 10,
                List.of(), List.of(), List.of(), Map.of("online-mode", false));

        var expected = Files.readString(Path.of("src/test/resources/snapshots/server-properties-demo.properties"));

        assertThat(ServerPropertiesRenderer.render(demo)).isEqualTo(expected);
    }

    @Test
    void enablesWhitelistWhenNamesPresent() {
        var def = new ServerDefinition("demo", "Demo", "main", 25565, "1.21.1",
                new Loader(LoaderType.FABRIC, null), "6G", "motd", 10,
                List.of("Notch"), List.of(), List.of(), Map.of());

        assertThat(ServerPropertiesRenderer.render(def))
                .contains("white-list=true\n")
                .doesNotContain("Notch");
    }
}
