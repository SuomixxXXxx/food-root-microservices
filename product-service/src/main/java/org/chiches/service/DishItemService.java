package org.chiches.service;


import org.chiches.dto.DishItemDTO;
import org.chiches.dto.FileUploadDTO;
import org.chiches.dto.UrlDTO;

import java.util.List;

public interface DishItemService {
    DishItemDTO createDishItem(DishItemDTO dishItemDTO);

    DishItemDTO findById(Long id);

    List<DishItemDTO> findAll();

    DishItemDTO update(DishItemDTO dishItemDTO);

    List<DishItemDTO> getAllByCategory(Long categoryId, int page, int size);

    List<DishItemDTO> getAllByName(String name);

    void delete(Long id);

    UrlDTO uploadImage(FileUploadDTO fileUploadDTO);

}
