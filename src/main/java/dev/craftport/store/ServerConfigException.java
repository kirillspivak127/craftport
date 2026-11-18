package dev.craftport.store;

public class ServerConfigException extends RuntimeException {
    private final String serverId;

    public ServerConfigException(String serverId, String message, Throwable cause) {
        super(message, cause);
        this.serverId = serverId;
    }

    public String serverId() {
        return this.serverId;
    }
}
