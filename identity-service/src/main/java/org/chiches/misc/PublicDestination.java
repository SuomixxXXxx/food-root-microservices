package org.chiches.misc;

public enum PublicDestination implements StompDestination{
    ORDER_UPDATE("/ordersub/active-orders"),
    SYSTEM_MESSAGE("/general/system-message");
    private final String destination;

    PublicDestination(String destination) {
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
