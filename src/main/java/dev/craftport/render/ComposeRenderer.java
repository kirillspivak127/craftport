package dev.craftport.render;

import dev.craftport.domain.ServerDefinition;

public final class ComposeRenderer {

	public static String render(ServerDefinition server) {
		var javaTag = JavaVersionMapper.map(server.minecraft()).imageTag();

		var extraEnv = new StringBuilder();
		if (!server.whitelist().isEmpty()) {
			extraEnv.append("\n      WHITELIST: \"%s\"".formatted(String.join(",", server.whitelist())));
		}
		if (!server.ops().isEmpty()) {
			extraEnv.append("\n      OPS: \"%s\"".formatted(String.join(",", server.ops())));
		}

		var loaderVersion = server.loader().version();
		if (loaderVersion != null && !loaderVersion.isBlank()) {
			switch (server.loader().type()) {
				case FABRIC -> extraEnv.append("\n      FABRIC_LOADER_VERSION: \"%s\""
						.formatted(loaderVersion));
				case NEOFORGE -> extraEnv.append("\n      NEOFORGE_VERSION: \"%s\""
						.formatted(loaderVersion));
				case FORGE -> extraEnv.append("\n      FORGE_VERSION: \"%s\""
						.formatted(loaderVersion));
				default -> {
				}
			}
		}

		return """
				services:
				  minecraft:
				    image: itzg/minecraft-server:%s
				    container_name: craftport-%s
				    restart: unless-stopped
				    ports:
				      - "%d:25565"
				    environment:
				      EULA: "TRUE"
				      TYPE: %s
				      VERSION: "%s"
				      MEMORY: %s%s
				    volumes:
				      - ../data:/data
				""".stripIndent().formatted(
				javaTag,
				server.id(),
				server.port(),
				server.loader().type(),
				server.minecraft(),
				server.memory(),
				extraEnv);
	}

	private ComposeRenderer() {
	}
}
