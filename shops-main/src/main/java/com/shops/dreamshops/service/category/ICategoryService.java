package com.shops.dreamshops.service.category;

import com.shops.dreamshops.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category getCategoryByName(String name);
    Category addCategory(Category category);
    Category updateCategory(Category category, long id);

    void deleteCategory(long id);




}
