package dev.craftport.render;

public final class JavaVersionMapper {
    public static JavaVersion map(String minecraftVersion) {
        var parts = minecraftVersion.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("unsupported minecraft version: " + minecraftVersion);
        }

        int major;
        int minor;
        int patch;
        try {
            major = Integer.parseInt(parts[0]);
            minor = Integer.parseInt(parts[1]);
            patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "unsupported minecraft version: " + minecraftVersion, e);
        }

        if (major != 1) {
            throw new IllegalArgumentException("unsupported minecraft version: " + minecraftVersion);
        }

        if (minor > 20 || (minor == 20 && patch >= 5)) {
            return JavaVersion.JAVA_21;
        }

        if (minor >= 17) {
            return JavaVersion.JAVA_17;
        }

        throw new IllegalArgumentException(
                "minecraft " + minecraftVersion + " is not supported (requires 1.17+)");
    }

    private JavaVersionMapper() {
    }
}
