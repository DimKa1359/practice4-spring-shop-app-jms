package com.example.springshopapp.controller;

import com.example.springshopapp.entity.Category;
import com.example.springshopapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // ---- LIST ----
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    // ---- ADD ----
    @PostMapping
    public String addCategory(@RequestParam String name) {
        Category category = new Category();
        category.setName(name);
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    // ---- DELETE ----
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }

    // ---- EDIT FORM ----
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "edit_category";
    }

    // ---- UPDATE ----
    @PostMapping("/update")
    public String updateCategory(
            @RequestParam Long id,
            @RequestParam String name
    ) {
        Category category = categoryService.getCategoryById(id);
        category.setName(name);
        categoryService.updateCategory(category);
        return "redirect:/categories";
    }
}
