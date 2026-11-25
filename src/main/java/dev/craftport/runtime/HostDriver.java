package dev.craftport.runtime;

import dev.craftport.domain.ServerDefinition;

public interface HostDriver {
    void sync(ServerDefinition server);

    void up(String serverId);

    void down(String serverId);

    ContainerStatus status(String serverId);

    String logs(String serverId, int lines);
}
