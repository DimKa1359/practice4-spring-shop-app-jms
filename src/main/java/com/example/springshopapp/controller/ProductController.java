package com.example.springshopapp.controller;

import com.example.springshopapp.entity.Product;
import com.example.springshopapp.entity.Category;
import com.example.springshopapp.service.ProductService;
import com.example.springshopapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // ---- LIST ----
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products";
    }

    // ---- ADD ----
    @PostMapping
    public String addProduct(
            @RequestParam String name,
            @RequestParam Double price,
            @RequestParam Long category_id
    ) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        Category category = categoryService.getCategoryById(category_id);
        product.setCategory(category);

        productService.addProduct(product);

        return "redirect:/products";
    }

    // ---- DELETE ----
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // ---- EDIT FORM ----
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit_product";
    }

    // ---- UPDATE ----
    @PostMapping("/update")
    public String updateProduct(
            @RequestParam Long id,
            @RequestParam String name,
            @RequestParam Double price,
            @RequestParam Long category_id
    ) {
        Product product = productService.getProductById(id);

        product.setName(name);
        product.setPrice(price);

        Category category = categoryService.getCategoryById(category_id);
        product.setCategory(category);

        productService.updateProduct(product);

        return "redirect:/products";
    }
}
