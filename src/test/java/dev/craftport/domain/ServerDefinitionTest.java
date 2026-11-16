package dev.craftport.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ServerDefinitionTest {
    private static ServerDefinition def(String id, int port, List<ModEntry> mods) {
        return new ServerDefinition(
                id, "friends", "main", port, "1.16.5",
                new Loader(LoaderType.FABRIC, "0.16.5"),
                "6G", "Заходите", 10, List.of("Notch"), List.of("Notch"), mods,
                Map.of("difficulty", "normal"));
    }

    @Test
    void createsValidServer() {
        var definition = def("friends", 25565, List.of(new ModEntry("sodium", ModSide.CLIENT, null)));
        assertThat(definition.id()).isEqualTo("friends");
        assertThat(definition.port()).isEqualTo(25565);
        assertThat(definition.mods()).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = { "Server for friends", "UPPER", "", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" })
    @NullSource
    void rejectsInvalidId(String id) {
        assertThatThrownBy(() -> def(id, 25565, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("server id");
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1, 65536 })
    void rejectsInvalidPort(int port) {
        assertThatThrownBy(() -> def("friends", port, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("invalid port");
    }

    @Test
    void treatsNullCollectionsAsEmpty() {
        var definition = new ServerDefinition("friends", "Friends", "main", 25565, "1.21.1",
                new Loader(LoaderType.FABRIC, "0.16.5"), "6GB", "motd", 10, null, null, null, null);
        assertThat(definition.mods()).isEmpty();
        assertThat(definition.ops()).isEmpty();
        assertThat(definition.whitelist()).isEmpty();
        assertThat(definition.serverProperties()).isEmpty();
    }

    @Test
    void rejectsNullModElement() {
        var mods = new ArrayList<ModEntry>();
        mods.add(new ModEntry("sodium", ModSide.CLIENT, "0.16.5"));
        mods.add(null);
        assertThatThrownBy(() -> new ServerDefinition("friends", "Friends", "main", 25565, "1.21.1",
                new Loader(LoaderType.FABRIC, "0.16.5"), "6G", "motd", 10,
                List.of(), List.of(), mods, Map.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mods must");

    }

    @Test
    void rejectsDuplicateModSlug() {
        var mods = List.of(
                new ModEntry("sodium", ModSide.CLIENT, null),
                new ModEntry("sodium", ModSide.BOTH, null));
        assertThatThrownBy(() -> def("friends", 25565, mods))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duplicate mod slug");
    }

    @Test
    void doesNotExposeMutableList() {
        var whitelist = new ArrayList<>(List.of("Notch"));
        var ops = new ArrayList<>(List.of("Notch"));
        var definition = new ServerDefinition("friends", "Friends", "main", 25565, "1.21.1",
                new Loader(LoaderType.FABRIC, "0.16.5"), "6G", "motd", 10,
                whitelist, ops, List.of(), Map.of());

        whitelist.add("Herobrine");
        ops.add("Herobrine");

        assertThat(definition.whitelist()).containsExactly("Notch");
        assertThat(definition.ops()).containsExactly("Notch");
    }
}
