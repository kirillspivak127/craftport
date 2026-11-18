package dev.craftport.app;

public class ServerNotFoundException extends RuntimeException {
    private static final String MESSAGE_TEMPLATE = "server %s not found";

    public ServerNotFoundException(String id) {
        super(MESSAGE_TEMPLATE.formatted(id));
    }
}
