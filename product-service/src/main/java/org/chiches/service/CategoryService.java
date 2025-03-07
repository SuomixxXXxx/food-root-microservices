package org.chiches.service;

import org.chiches.dto.CategoryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO, MultipartFile previewPicture, MultipartFile mainPicture);

    CategoryDTO findById(Long id);

    List<CategoryDTO> findAll(boolean active);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, MultipartFile previewPicture, MultipartFile mainPicture);

}
