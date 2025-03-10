package org.chiches.exception.order;

public abstract class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
