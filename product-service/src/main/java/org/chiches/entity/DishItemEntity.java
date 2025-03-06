package org.chiches.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dish_items")
public class DishItemEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    private String name;
    private Double weight;
    private Double price;
    private Integer quantity;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    protected DishItemEntity() {
    }

    public DishItemEntity(CategoryEntity category, String name, Double weight, Double price, Integer quantity) {
        this.category = category;
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.quantity = quantity;
        this.isDeleted = false;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}