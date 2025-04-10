package org.chiches.controller;

import org.chiches.dto.DishItemDTO;
import org.chiches.dto.FileUploadDTO;
import org.chiches.dto.UrlDTO;
import org.chiches.service.DishItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/dishItems")
public class DishItemController {

    private final DishItemService dishItemService;

    public DishItemController(DishItemService dishItemService) {
        this.dishItemService = dishItemService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<DishItemDTO> createDishItem(@ModelAttribute DishItemDTO dishItemDTO) {
        DishItemDTO savedDTO =  dishItemService.createDishItem(dishItemDTO);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<DishItemDTO> getDishItem(@PathVariable("id") Long id) {
        DishItemDTO dishItemDTO = dishItemService.findById(id);
        return ResponseEntity.ok(dishItemDTO);
    }

    @GetMapping(path = "/getByCategory")
    public ResponseEntity<List<DishItemDTO>> getAllByCategory (@RequestParam Long categoryId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "12") int size) {
        List<DishItemDTO> dishItemDTOs = dishItemService.getAllByCategory(categoryId, page, size);
        return ResponseEntity.ok(dishItemDTOs);
    }

    @GetMapping(path = "/get")
    public ResponseEntity<List<DishItemDTO>> getDishItems() {
        List<DishItemDTO> dishItemDTOs = dishItemService.findAll();
        return ResponseEntity.ok(dishItemDTOs);
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<DishItemDTO> updateDishItem(@ModelAttribute DishItemDTO dishItemDTO) {
        DishItemDTO savedDTO = dishItemService.update(dishItemDTO);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping(path = "/getByName")
    public ResponseEntity<List<DishItemDTO>> getAllByName(@RequestParam String name) {
        List<DishItemDTO> dishItemDTOs = dishItemService.getAllByName(name);
        return ResponseEntity.ok(dishItemDTOs);
    }

    @DeleteMapping(path = "/delete")
    public void delete(@RequestParam Long id) {
        dishItemService.delete(id);
    }

    @PostMapping(path = "/upload-picture")
    public ResponseEntity<UrlDTO> uploadPicture(@ModelAttribute FileUploadDTO fileUploadDTO) {
        UrlDTO urlDTO = dishItemService.uploadImage(fileUploadDTO);
        return ResponseEntity.ok(urlDTO);
    }

}
