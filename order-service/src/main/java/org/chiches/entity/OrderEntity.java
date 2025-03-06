package org.chiches.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    private Long userId;
    private Double fullPrice;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private LocalDateTime dateOfCreation;
    private LocalDateTime dateOfCompletion;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderContentEntity> orderContents = new ArrayList<>();

    protected OrderEntity() {
    }

    public OrderEntity(Long userId) {
        this.userId = userId;
        this.status = OrderStatus.CREATED;
        this.dateOfCreation = LocalDateTime.now();
        this.fullPrice = 0d;
    }

    public void addOrderContent(Long dishItemId, Double price, int quantity) {
        OrderContentEntity orderContent = new OrderContentEntity(this, dishItemId, price, quantity);
        orderContents.add(orderContent);
        this.fullPrice += price * quantity;
    }

    public void changeStatus(OrderStatus newStatus) {
        if (this.status.equals(OrderStatus.CREATED)) {
            switch (newStatus) {
                case COMPLETED:
                    this.dateOfCompletion = LocalDateTime.now();
                    this.status = OrderStatus.COMPLETED;
                    break;
                case CANCELED:
                    this.status = OrderStatus.CANCELED;
                    break;
                default:
                    throw new IllegalStateException("Invalid status change");
            }
        } else {
            throw new IllegalStateException("Cannot change order status from " + this.status);
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getFullPrice() {
        return fullPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public LocalDateTime getDateOfCompletion() {
        return dateOfCompletion;
    }

    public List<OrderContentEntity> getOrderContents() {
        return orderContents;
    }
}