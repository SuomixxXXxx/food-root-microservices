package org.chiches.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderContentDTO {
    @NotNull(message = "Order cannot be empty")
    private OrderDTO orderDTO;
    private Long dishItemId;
    private Double dishItemPrice;
    @NotNull(message = "Quantity cannot be empty")
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Max(value = 10, message = "Quantity must be less than 10")
    private Integer quantity;

    public OrderContentDTO() {
    }

    public OrderContentDTO(OrderDTO orderDTO, Long dishItemId, Double dishItemPrice, Integer quantity) {
        this.orderDTO = orderDTO;
        this.dishItemId = dishItemId;
        this.dishItemPrice = dishItemPrice;
        this.quantity = quantity;
    }

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    public Long getDishItemId() {
        return dishItemId;
    }

    public void setDishItemId(Long dishItemId) {
        this.dishItemId = dishItemId;
    }

    public Double getDishItemPrice() {
        return dishItemPrice;
    }

    public void setDishItemPrice(Double dishItemPrice) {
        this.dishItemPrice = dishItemPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
