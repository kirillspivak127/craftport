package dev.craftport.render;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class JavaVersionMapperTest {
    @ParameterizedTest
    @CsvSource({
            "1.21.1, JAVA_21",
            "1.20.5, JAVA_21",
            "1.20.4, JAVA_17",
            "1.17, JAVA_17",
            "1.20, JAVA_17",
            "1.21, JAVA_21"
    })
    void mapVersionToJava(String minecraft, JavaVersion expected) {
        assertThat(JavaVersionMapper.map(minecraft)).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = { "1.16.5", "1.12.2", "2.0", "abc", "1" })
    void rejectsUnsupportedOrMalformed(String minecraft) {
        assertThatThrownBy(() -> JavaVersionMapper.map(minecraft))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
