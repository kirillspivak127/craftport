package dev.craftport.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class HostDefinitionTest {
    @Test
    void createsValidHost() {
        var host = new HostDefinition("main", "main", "127.0.0.1", 8, HostDriverType.LOCAL);
        assertThat(host.id()).isEqualTo("main");
        assertThat(host.name()).isEqualTo("main");
        assertThat(host.address()).isEqualTo("127.0.0.1");
        assertThat(host.maxMemoryGb()).isEqualTo(8);
        assertThat(host.driver()).isEqualTo(HostDriverType.LOCAL);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsInvalidId(String id) {
        assertThatThrownBy(() -> new HostDefinition(id, "main", "127.0.0.1", 8, HostDriverType.LOCAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("host id");

    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsInvalidName(String name) {
        assertThatThrownBy(() -> new HostDefinition("main", name, "127.0.0.1", 8, HostDriverType.LOCAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("host name");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsInvalidAddress(String address) {
        assertThatThrownBy(() -> new HostDefinition("main", "main", address, 8, HostDriverType.LOCAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("host address");
    }

    @ParameterizedTest
    @ValueSource(ints = { -5, 0 })
    void rejectsInvalidMaxMemory(int maxMemoryGb) {
        assertThatThrownBy(() -> new HostDefinition("main", "main", "127.0.0.1", maxMemoryGb, HostDriverType.LOCAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("host maxMemoryGb");
    }

    @Test
    void rejectsInvalidDriver() {
        assertThatThrownBy(() -> new HostDefinition("main", "main", "127.0.0.1", 8, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("host driver");
    }
}
