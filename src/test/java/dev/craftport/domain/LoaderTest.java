package dev.craftport.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LoaderTest {
    @Test
    void createsValidLoader() {
        var loader = new Loader(LoaderType.FABRIC, "0.16.5");
        assertThat(loader.type()).isEqualTo(LoaderType.FABRIC);
        assertThat(loader.version()).isEqualTo("0.16.5");
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "   " })
    void rejectsInvalidVersion(String version) {
        assertThatThrownBy(() -> new Loader(LoaderType.FABRIC, version))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("loader version");
    }

    @Test
    void rejectsInvalidType() {
        assertThatThrownBy(() -> new Loader(null, "0.16.5"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("loader type");
    }

    @Test
    void allowsNullVersion() {
        assertThat(new Loader(LoaderType.FABRIC, null).version()).isNull();
    }
}
