package org.chiches.controller;

import org.chiches.dto.CategoryDTO;
import org.chiches.service.CategoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
//@RequestMapping(path = "categories")
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryDTO> createCategory(@RequestPart CategoryDTO categoryDTO,
                                                      @RequestPart(value = "previewPicture", required = false) MultipartFile previewPicture,
                                                      @RequestPart(value = "mainPicture", required = false) MultipartFile mainPicture) {
        CategoryDTO savedDTO = categoryService.createCategory(categoryDTO, previewPicture, mainPicture);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable("id") Long id) {
        CategoryDTO savedDTO = categoryService.findById(id);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping(path = "/get")
    public ResponseEntity<List<CategoryDTO>> getCategories(@RequestParam boolean active) {
        List<CategoryDTO> categoryDTOS = categoryService.findAll(active);
        return ResponseEntity.ok(categoryDTOS);
    }

    @PatchMapping(path = "/update")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestPart CategoryDTO categoryDTO,
                                                      @RequestPart(value = "previewPicture", required = false) MultipartFile previewPicture,
                                                      @RequestPart(value = "mainPicture", required = false) MultipartFile mainPicture) {
        CategoryDTO savedDTO = categoryService.updateCategory(categoryDTO, previewPicture, mainPicture);
        return ResponseEntity.ok(savedDTO);
    }


}
