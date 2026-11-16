package dev.craftport.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public record ServerDefinition(String id, String name, String host, int port, String minecraft, Loader loader,
        String memory, String motd, int maxPlayers, List<String> whitelist, List<String> ops,
        List<ModEntry> mods, Map<String, Object> serverProperties) {

    private static final Pattern ID_PATTERN = Pattern.compile("[a-z0-9-]{1,32}");

    public ServerDefinition {
        if (id == null || !ID_PATTERN.matcher(id).matches()) {
            throw new IllegalArgumentException(
                    "invalid server id: '" + id + "' (expected [a-z0-9-]{1,32})");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("server name must not be blank: " + id);
        }
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("server host must not be blank: " + id);
        }
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("invalid port: " + port);
        }
        if (minecraft == null || minecraft.isBlank()) {
            throw new IllegalArgumentException("minecraft version must not be blank: " + id);
        }
        if (loader == null) {
            throw new IllegalArgumentException("loader must not be null: " + id);
        }
        if (memory == null || memory.isBlank()) {
            throw new IllegalArgumentException("memory must not be blank: " + id);
        }
        if (maxPlayers < 1) {
            throw new IllegalArgumentException("maxPlayers must be positive: " + maxPlayers);
        }

        if (mods == null) {
            mods = List.of();
        }

        for (ModEntry mod : mods) {
            if (mod == null) {
                throw new IllegalArgumentException("mods must not contain null: " + id);
            }
        }

        mods = List.copyOf(mods);
        whitelist = whitelist == null ? List.of() : List.copyOf(whitelist);
        ops = ops == null ? List.of() : List.copyOf(ops);
        serverProperties = serverProperties == null ? Map.of() : Map.copyOf(serverProperties);

        var seen = new HashSet<String>();
        for (ModEntry mod : mods) {
            if (!seen.add(mod.slug())) {
                throw new IllegalArgumentException("duplicate mod slug: " + mod.slug());
            }
        }
    }
}
