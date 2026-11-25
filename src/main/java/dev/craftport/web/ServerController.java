package dev.craftport.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.craftport.app.DeploymentService;
import dev.craftport.app.ServerService;
import dev.craftport.domain.ServerDefinition;
import dev.craftport.runtime.ContainerStatus;

@RestController
@RequestMapping("/api/servers")
public class ServerController {
    private final ServerService serverService;
    private final DeploymentService deploymentService;

    public ServerController(ServerService serverService, DeploymentService deploymentService) {
        this.serverService = serverService;
        this.deploymentService = deploymentService;
    }

    @GetMapping
    List<ServerDefinition> list() {
        return serverService.list();
    }

    @GetMapping("/{id}")
    ServerDefinition one(@PathVariable String id) {
        return serverService.get(id);
    }

    @PostMapping("/{id}/deploy")
    void deploy(@PathVariable String id) {
        deploymentService.deploy(id);
    }

    @PostMapping("/{id}/restart")
    void restart(@PathVariable String id) {
        deploymentService.restart(id);
    }

    @PostMapping("/{id}/stop")
    void stop(@PathVariable String id) {
        deploymentService.stop(id);
    }

    @GetMapping("/{id}/logs")
    String logs(@PathVariable String id, @RequestParam(defaultValue = "200") int lines) {
        return deploymentService.logs(id, lines);
    }

    @GetMapping("/{id}/status")
    ContainerStatus status(@PathVariable String id) {
        return deploymentService.status(id);
    }
}
