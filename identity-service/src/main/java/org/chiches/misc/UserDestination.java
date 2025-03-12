package org.chiches.misc;

public enum UserDestination implements StompDestination {
    NOTIFICATION("/user/general/notifications"),
    MESSAGE("/user/general/message");


    private final String destination;

    UserDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return destination;
    }
}
