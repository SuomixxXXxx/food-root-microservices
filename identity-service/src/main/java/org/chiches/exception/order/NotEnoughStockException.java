package org.chiches.exception.order;

public class NotEnoughStockException extends OrderException {
    public NotEnoughStockException(String message) {
        super(message);
    }
}
