package org.chiches.controller;

import org.chiches.dto.OrderDTO;
import org.chiches.entity.OrderStatus;
import org.chiches.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO, Principal principal) {
        ResponseEntity<OrderDTO> responseEntity;
        responseEntity = orderService.createOrder(orderDTO, principal);
        return responseEntity;
    }

    @GetMapping(path = "get/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("id") Long id) {
        ResponseEntity<OrderDTO> responseEntity;
        responseEntity = orderService.findById(id);
        return responseEntity;
    }

    @PatchMapping(path = "/complete")
    public ResponseEntity<OrderDTO> completeOrder(@RequestParam Long id) {
        ResponseEntity<OrderDTO> responseEntity;
        responseEntity = orderService.updateOrderStatus(id, OrderStatus.COMPLETED);
        return responseEntity;
    }

    @PatchMapping(path = "/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@RequestParam Long id) {
        ResponseEntity<OrderDTO> responseEntity;
        responseEntity = orderService.updateOrderStatus(id, OrderStatus.CANCELED);
        return responseEntity;
    }
    @GetMapping(path = "/getFaggot")
    @PostAuthorize("hasAuthority('service')")
    public ResponseEntity<String> getFaggot(@RequestParam Long id){
        return ResponseEntity.ok("You are a faggot number: " + id);
    }

}
