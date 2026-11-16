package dev.craftport.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class ModEntryTest {
    @Test
    void createsValidMod() {
        var mod = new ModEntry("sodium", ModSide.CLIENT, null);
        assertThat(mod.slug()).isEqualTo("sodium");
        assertThat(mod.side()).isEqualTo(ModSide.CLIENT);
        assertThat(mod.version()).isNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsInvalidSlug(String slug) {
        assertThatThrownBy(() -> new ModEntry(slug, ModSide.BOTH, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mod slug");

    }

    @Test
    void rejectsInvalidModSide() {
        assertThatThrownBy(() -> new ModEntry("sodium", null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mod side");
    }
}
