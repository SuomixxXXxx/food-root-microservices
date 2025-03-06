package org.chiches.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_contents")
public class OrderContentEntity extends BaseEntity {
    private Long dishItemId;
    private Double price;
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    protected OrderContentEntity() {
    }

    public OrderContentEntity(OrderEntity order, Long dishItemId, Double price, Integer quantity) {
        this.order = order;
        this.dishItemId = dishItemId;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public Long getDishItemId() {
        return dishItemId;
    }

    public void setDishItemId(Long dishItemId) {
        this.dishItemId = dishItemId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}