package dev.craftport.runtime;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import dev.craftport.domain.ServerDefinition;
import dev.craftport.render.ComposeRenderer;
import dev.craftport.render.ServerPropertiesRenderer;

@Component
public class LocalDockerDriver implements HostDriver {
    private final Path dataRoot;

    public LocalDockerDriver(Path dataRoot) {
        this.dataRoot = dataRoot;
    }

    @Override
    public void sync(ServerDefinition server) {
        var serverDir = dataRoot.resolve(server.id());
        var artifactsDir = serverDir.resolve("artifacts");
        var dataDir = serverDir.resolve("data");

        try {
            Files.createDirectories(artifactsDir);
            Files.createDirectories(dataDir);

            Files.writeString(
                    artifactsDir.resolve("docker-compose.yaml"),
                    ComposeRenderer.render(server),
                    StandardCharsets.UTF_8);

            Files.writeString(
                    dataDir.resolve("server.properties"),
                    ServerPropertiesRenderer.render(server),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DockerException(
                    "cannot sync server '" + server.id() + "': " + e.getMessage(), e);
        }

    }

    @Override
    public void up(String serverId) {
        var artifactsDir = dataRoot.resolve(serverId).resolve("artifacts");

        try {
            var log = Files.createTempFile("craftport-compose-", ".log");
            var process = new ProcessBuilder(
                    "docker", "compose", "-p", "craftport-" + serverId, "up", "-d", "--remove-orphans")
                    .directory(artifactsDir.toFile())
                    .redirectErrorStream(true)
                    .redirectOutput(log.toFile())
                    .start();

            if (!process.waitFor(10, TimeUnit.MINUTES)) {
                process.destroyForcibly();
                throw new DockerException("docker compose up timed out: " + serverId);
            }

            var output = Files.readString(log);
            if (process.exitValue() != 0) {
                throw new DockerException("docker compose up failed (" + process.exitValue() + "): " + output);
            }

        } catch (IOException | InterruptedException e) {
            throw new DockerException("cannot up server '" + serverId + "': " + e.getMessage(), e);
        }
    }

    @Override
    public void down(String serverId) {
        var artifactsDir = dataRoot.resolve(serverId).resolve("artifacts");

        try {
            var log = Files.createTempFile("craftport-compose-", ".log");
            var process = new ProcessBuilder(
                    "docker", "compose", "-p", "craftport-" + serverId, "down")
                    .directory(artifactsDir.toFile())
                    .redirectErrorStream(true)
                    .redirectOutput(log.toFile())
                    .start();

            if (!process.waitFor(10, TimeUnit.MINUTES)) {
                process.destroyForcibly();
                throw new DockerException("docker compose down timed out: " + serverId);
            }

            var output = Files.readString(log);
            if (process.exitValue() != 0) {
                throw new DockerException("docker compose down failed (" + process.exitValue() + "): " + output);
            }
        } catch (IOException | InterruptedException e) {
            throw new DockerException("cannot down server '" + serverId + "': " + e.getMessage(), e);
        }
    }

    @Override
    public ContainerStatus status(String serverId) {
        try {
            var log = Files.createTempFile("craftport-compose-", ".log");
            var process = new ProcessBuilder(
                    "docker", "inspect", "--format", "{{.State.Status}}", "craftport-" + serverId)
                    .redirectErrorStream(true)
                    .redirectOutput(log.toFile())
                    .start();

            if (!process.waitFor(30, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new DockerException("docker inspect timed out: " + serverId);
            }

            if (process.exitValue() != 0) {
                return ContainerStatus.NOT_FOUND;
            }

            var output = Files.readString(log);
            return switch (output.trim()) {
                case "running" -> ContainerStatus.RUNNING;
                case "created", "exited" -> ContainerStatus.EXITED;
                default -> ContainerStatus.UNKNOWN;
            };
        } catch (IOException | InterruptedException e) {
            throw new DockerException("cannot get server status '" + serverId + "': " + e.getMessage(), e);
        }
    }

    @Override
    public String logs(String serverId, int lines) {
        var artifactsDir = dataRoot.resolve(serverId).resolve("artifacts");

        try {
            var log = Files.createTempFile("craftport-compose-", ".log");
            var process = new ProcessBuilder(
                    "docker", "logs", "--tail", String.valueOf(lines), "craftport-" + serverId)
                    .directory(artifactsDir.toFile())
                    .redirectErrorStream(true)
                    .redirectOutput(log.toFile())
                    .start();
            if (!process.waitFor(30, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new DockerException("docker logs timed out: " + serverId);
            }

            var output = Files.readString(log);
            if (process.exitValue() != 0) {
                throw new DockerException("docker logs failed (" + process.exitValue() + "): " + output);
            }

            return output;
        } catch (IOException | InterruptedException e) {
            throw new DockerException("cannot get logs from server '" + serverId + "': " + e.getMessage(), e);
        }
    }

}
