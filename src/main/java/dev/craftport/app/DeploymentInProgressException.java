package dev.craftport.app;

public class DeploymentInProgressException extends RuntimeException {
    public DeploymentInProgressException(String id) {
        super("deploy already in progress for server '" + id + "'");
    }
}
