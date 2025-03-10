package org.chiches.repository;

import org.chiches.entity.OrderEntity;
import org.chiches.entity.OrderStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends GeneralRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByDateOfCreationBetween(LocalDateTime start, LocalDateTime finish);

    List<OrderEntity> findByStatus(OrderStatus orderStatus);
}
