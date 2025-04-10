package org.chiches.controller;

import org.chiches.dto.DishItemDTO;
import org.chiches.service.DishItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping(path = "/api/v1/orderservice")
public class OrderServiceController {
    private final DishItemService dishItemService;

    public OrderServiceController(DishItemService dishItemService) {
        this.dishItemService = dishItemService;
    }

    @GetMapping("/getPricesByIds")
    public ResponseEntity<List<DishItemDTO>> getItemsByIds(@RequestParam("ids") List<Long> ids) {
        List<DishItemDTO> dishItemDTOS = dishItemService.findPricesByIds(ids);
        return ResponseEntity.ok(dishItemDTOS);
    }
}
