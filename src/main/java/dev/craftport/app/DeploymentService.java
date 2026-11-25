package dev.craftport.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import dev.craftport.runtime.ContainerStatus;
import dev.craftport.runtime.HostDriver;
import dev.craftport.store.ServerRepository;

@Service
public class DeploymentService {
    private final ServerRepository serverRepository;
    private final HostDriver driver;
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public DeploymentService(ServerRepository serverRepository, HostDriver driver) {
        this.serverRepository = serverRepository;
        this.driver = driver;
    }

    public void deploy(String serverId) {
        var server = serverRepository.find(serverId).orElseThrow(() -> new ServerNotFoundException(serverId));
        withLock(serverId, () -> {
            driver.sync(server);
            driver.up(serverId);
        });
    }

    public void stop(String serverId) {
        serverRepository.find(serverId).orElseThrow(() -> new ServerNotFoundException(serverId));
        withLock(serverId, () -> driver.down(serverId));
    }

    public void restart(String serverId) {
        var server = serverRepository.find(serverId).orElseThrow(() -> new ServerNotFoundException(serverId));
        withLock(serverId, () -> {
            driver.sync(server);
            driver.down(serverId);
            driver.up(serverId);
        });
    }

    public String logs(String serverId, int lines) {
        serverRepository.find(serverId).orElseThrow(() -> new ServerNotFoundException(serverId));
        return driver.logs(serverId, lines);
    }

    public ContainerStatus status(String serverId) {
        serverRepository.find(serverId).orElseThrow(() -> new ServerNotFoundException(serverId));
        return driver.status(serverId);
    }

    private void withLock(String id, Runnable action) {
        var lock = locks.computeIfAbsent(id, key -> new ReentrantLock());
        if (!lock.tryLock()) {
            throw new DeploymentInProgressException(id);
        }
        try {
            action.run();
        } finally {
            lock.unlock();
        }
    }
}
