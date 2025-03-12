package org.chiches.service.impl;

import jakarta.persistence.PersistenceException;
import org.chiches.dto.CategoryDTO;
import org.chiches.entity.CategoryEntity;
import org.chiches.exception.DatabaseException;
import org.chiches.exception.ResourceNotFoundException;
import org.chiches.exception.file.InvalidFileFormatException;
import org.chiches.repository.CategoryRepository;
import org.chiches.service.CategoryService;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
//    private final StorageService storageService;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper modelMapper
//            , StorageService storageService
    ) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
//        this.storageService = storageService;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "categories", allEntries = true)
    })
    public CategoryDTO createCategory(CategoryDTO categoryDTO, MultipartFile previewPicture, MultipartFile mainPicture) {
        try {
            CategoryEntity categoryEntity = modelMapper.map(categoryDTO, CategoryEntity.class);
            CategoryDTO savedDTO = saveCategory(categoryEntity, previewPicture, mainPicture);
            return savedDTO;
        } catch (DataAccessException | PersistenceException e) {
            throw new DatabaseException("Category was not created due to DB connection issues");
        }
    }
    @Override
    @Cacheable("category")
    public CategoryDTO findById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found"));
        CategoryDTO categoryDTO = modelMapper.map(categoryEntity, CategoryDTO.class);
        return categoryDTO;
    }
    @Override
    @Cacheable(value = "categories", key = "#active")
    public List<CategoryDTO> findAll(boolean active) {
        List<CategoryEntity> categoryEntities;
        if (active) {
            categoryEntities = categoryRepository.findAllNotDeleted();

        } else {
            categoryEntities = categoryRepository.findAll();
        }
        List<CategoryDTO> categoryDTOs = categoryEntities.stream()
                .map(categoryEntity -> modelMapper.map(categoryEntity, CategoryDTO.class))
                .toList();
        return categoryDTOs;
    }
    @Override
    @Caching(evict = {
            @CacheEvict(value = "category", key = "#categoryDTO.id"),
            @CacheEvict(value = "categories", allEntries = true)
    })
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, MultipartFile previewPicture, MultipartFile mainPicture) {
        CategoryEntity categoryEntity = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        try {
            categoryEntity.setName(categoryDTO.getName());
            CategoryDTO savedDTO = saveCategory(categoryEntity, previewPicture, mainPicture);
            return savedDTO;
        } catch (DataAccessException | PersistenceException e) {
            throw new DatabaseException("Category was not updated due to problems connecting to the database");
        }
    }

    private CategoryDTO saveCategory(CategoryEntity categoryEntity, MultipartFile previewPicture, MultipartFile mainPicture) {
        CategoryEntity savedEntity = categoryRepository.save(categoryEntity);
        CategoryDTO savedDTO = modelMapper.map(savedEntity, CategoryDTO.class);
        if (previewPicture != null) {
            String contentType = previewPicture.getContentType();
            if (contentType != null && Arrays.asList("image/jpeg", "image/png").contains(contentType)) {
                String name = String.format("categories/preview/%d.jpg", savedDTO.getId());
//                String url = storageService.uploadFile(previewPicture, name);
//                savedDTO.setPreviewPictureUrl(url);
            } else {
                throw new InvalidFileFormatException("File format not allowed: " + contentType);
            }
        }
        if (mainPicture != null) {
            String contentType = mainPicture.getContentType();
            if (contentType != null && Arrays.asList("image/jpeg", "image/png").contains(contentType)) {
                String name = String.format("categories/main/%d.jpg", savedDTO.getId());
//                String url = storageService.uploadFile(mainPicture, name);
//                savedDTO.setMainPictureUrl(url);
            } else {
                throw new InvalidFileFormatException("File format not allowed: " + contentType);
            }
        }
        return savedDTO;
    }
}