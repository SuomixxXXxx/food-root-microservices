package org.chiches.feign;

import org.chiches.dto.DishItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "order-service", configuration = FeignClientConfiguration.class)
public interface OrderServiceClient {
    @GetMapping("/api/v1/order/getFaggot")
    ResponseEntity<String> getFaggot(@RequestParam Long id);
}
