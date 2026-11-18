package dev.craftport.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.craftport.app.ServerService;
import dev.craftport.domain.ServerDefinition;

@RestController
@RequestMapping("/api/servers")
public class ServerController {
    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping
    List<ServerDefinition> list() {
        return serverService.list();
    }

    @GetMapping("/{id}")
    ServerDefinition one(@PathVariable String id) {
        return serverService.get(id);
    }
}
