package org.chiches.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseEntity {
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DishItemEntity> dishItems;

    protected CategoryEntity() {
    }

    public CategoryEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DishItemEntity> getDishItems() {
        return dishItems;
    }

    public void setDishItems(List<DishItemEntity> dishItems) {
        this.dishItems = dishItems;
    }
}