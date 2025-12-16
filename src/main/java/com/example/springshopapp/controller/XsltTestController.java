package com.example.springshopapp.controller;

import com.example.springshopapp.dto.ProductDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/xslt-test")
public class XsltTestController {

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_XML_VALUE)
    public List<ProductDTO> getTestProducts() {
        // Тестовые данные для XSLT
        ProductDTO product1 = new ProductDTO(1L, "Test Laptop", 999.99, 1L, "Electronics");
        ProductDTO product2 = new ProductDTO(2L, "Test Book", 19.99, 2L, "Books");
        ProductDTO product3 = new ProductDTO(3L, "Test Phone", 499.99, 1L, "Electronics");

        return Arrays.asList(product1, product2, product3);
    }

    @GetMapping(value = "/product", produces = MediaType.APPLICATION_XML_VALUE)
    public ProductDTO getTestProduct() {
        return new ProductDTO(1L, "Test Product", 100.0, 1L, "Test Category");
    }
}