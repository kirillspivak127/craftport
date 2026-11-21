package dev.craftport.render;

import dev.craftport.domain.ServerDefinition;

public final class ComposeRenderer {

	public static String render(ServerDefinition server) {
		var javaTag = JavaVersionMapper.map(server.minecraft()).imageTag();

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
				      MEMORY: %s
				    volumes:
				      - ../data:/data
				""".stripIndent().formatted(
				javaTag,
				server.id(),
				server.port(),
				server.loader().type(),
				server.minecraft(),
				server.memory());
	}
}
