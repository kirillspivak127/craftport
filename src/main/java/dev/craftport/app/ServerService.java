package dev.craftport.app;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.craftport.domain.ServerDefinition;
import dev.craftport.store.ServerRepository;

@Service
public class ServerService {
    private final ServerRepository serverRepository;

    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public List<ServerDefinition> list() {
        return serverRepository.findAll();
    }

    public ServerDefinition get(String id) {
        return serverRepository.find(id).orElseThrow(() -> new ServerNotFoundException(id));
    }
}
