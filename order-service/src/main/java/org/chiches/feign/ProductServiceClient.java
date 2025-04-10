package org.chiches.feign;

import org.chiches.dto.DishItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service", configuration = FeignClientConfiguration.class)
public interface ProductServiceClient {
    @GetMapping("/api/v1/orderservice/getPricesByIds")
    ResponseEntity<List<DishItemDTO>> getPricesByIds(@RequestParam("ids") List<Long> ids);
}
