package org.chiches.service;


import org.chiches.dto.OrderDTO;
import org.chiches.entity.OrderStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface OrderService {
    public ResponseEntity<OrderDTO> createOrder(OrderDTO orderDTO, Principal principal);

    public ResponseEntity<OrderDTO> updateOrderStatus(Long id, OrderStatus orderStatus);

    public ResponseEntity<OrderDTO> findById(Long id);

    public ResponseEntity<?> findAllActive();
}
