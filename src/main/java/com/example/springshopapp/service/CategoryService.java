package com.example.springshopapp.service;

import com.example.springshopapp.entity.Category;
import com.example.springshopapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JmsAuditService jmsAuditService;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public Category addCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);

        // Отправляем JMS сообщение о создании
        jmsAuditService.sendCategoryEvent(
                "CREATE",
                savedCategory.getId(),
                savedCategory.getName(),
                "Category created: name='" + savedCategory.getName() + "'"
        );

        return savedCategory;
    }

    @Transactional
    public Category updateCategory(Category category) {
        Category existingCategory = getCategoryById(category.getId());
        if (existingCategory == null) {
            return null;
        }

        // Сохраняем старое имя
        String oldName = existingCategory.getName();

        Category updatedCategory = categoryRepository.save(category);

        // Отправляем JMS сообщение об обновлении
        String changeDetails = String.format(
                "Category updated: ID=%d\nOld name: '%s'\nNew name: '%s'",
                category.getId(),
                oldName,
                updatedCategory.getName()
        );

        jmsAuditService.sendCategoryEvent(
                "UPDATE",
                updatedCategory.getId(),
                updatedCategory.getName(),
                changeDetails
        );

        return updatedCategory;
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        if (category == null) {
            return;
        }

        // Сохраняем информацию перед удалением
        String categoryInfo = "Category: name='" + category.getName() + "'";

        categoryRepository.deleteById(id);

        // Отправляем JMS сообщение об удалении
        jmsAuditService.sendCategoryEvent(
                "DELETE",
                id,
                category.getName(),
                "Category deleted: " + categoryInfo
        );
    }
}