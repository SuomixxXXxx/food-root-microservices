package org.chiches.repository;

import org.chiches.entity.OrderContentEntity;
import org.chiches.entity.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderContentRepository extends GeneralRepository<OrderContentEntity, Long>{
    @Query(value = "select sum(oc.quantity) from OrderContentEntity oc " +
            "where oc.dishItemId = :dishItemId and " +
            "oc.order in :orders")
    Integer findCount(@Param("dishItemId") Long dishItemId, @Param("orders")List<OrderEntity> orders);
}
