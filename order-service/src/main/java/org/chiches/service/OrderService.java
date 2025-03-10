package org.chiches.service;


import org.chiches.dto.OrderDTO;
import org.chiches.entity.OrderStatus;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    public ResponseEntity<OrderDTO> createOrder(OrderDTO orderDTO);

    public ResponseEntity<OrderDTO> updateOrderStatus(Long id, OrderStatus orderStatus);

    public ResponseEntity<OrderDTO> findById(Long id);

    public ResponseEntity<?> findAllActive();
}
