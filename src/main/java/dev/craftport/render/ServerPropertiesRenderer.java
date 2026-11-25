package dev.craftport.render;

import java.util.TreeMap;
import java.util.stream.Collectors;

import dev.craftport.domain.ServerDefinition;

public final class ServerPropertiesRenderer {
    public static String render(ServerDefinition server) {
        var props = new TreeMap<String, String>();

        if (server.motd() != null && !server.motd().isBlank()) {
            props.put("motd", server.motd());
        }

        props.put("max-players", String.valueOf(server.maxPlayers()));
        props.put("white-list", server.whitelist().isEmpty() ? "false" : "true");

        server.serverProperties().forEach((property, value) -> props.put(property, String.valueOf(value)));

        return props.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("\n", "", "\n"));
    }

    private ServerPropertiesRenderer() {
    }
}
