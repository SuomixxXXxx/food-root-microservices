package org.chiches.repository;

import org.chiches.entity.CategoryEntity;
import org.chiches.entity.DishItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishItemRepository extends GeneralRepository<DishItemEntity, Long> {

    Page<DishItemEntity> findAllByCategoryAndIsDeletedFalse(CategoryEntity categoryEntity, Pageable pageable);

    List<DishItemEntity> findAllByNameContainingIgnoreCaseAndIsDeletedFalse(String name);

    @Query(value = "select d from DishItemEntity d where d.isDeleted = false")
    List<DishItemEntity> findAll();

    @Query(value = "select d from DishItemEntity d where d.id = :id and d.isDeleted = false")
    Optional<DishItemEntity> findById(@Param(value = "id") Long id);

    @Query("SELECT d FROM DishItemEntity d WHERE d.id IN :ids")
    List<DishItemEntity> findAllByIds(@Param("ids") List<Long> ids);

}
