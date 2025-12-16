package com.example.springshopapp.service;

import com.example.springshopapp.entity.Product;
import com.example.springshopapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JmsAuditService jmsAuditService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public Product addProduct(Product product) {
        Product savedProduct = productRepository.save(product);

        // Отправляем JMS сообщение о создании
        String changeDetails = String.format(
                "Product created: name='%s', price=%.2f, category='%s'",
                savedProduct.getName(),
                savedProduct.getPrice(),
                savedProduct.getCategory() != null ? savedProduct.getCategory().getName() : "null"
        );

        jmsAuditService.sendProductEvent(
                "CREATE",
                savedProduct.getId(),
                savedProduct.getName(),
                changeDetails
        );

        return savedProduct;
    }

    @Transactional
    public Product updateProduct(Product product) {
        Product existingProduct = getProductById(product.getId());
        if (existingProduct == null) {
            return null;
        }

        // Сохраняем старые значения для логов
        String oldName = existingProduct.getName();
        Double oldPrice = existingProduct.getPrice();
        String oldCategory = existingProduct.getCategory() != null
                ? existingProduct.getCategory().getName() : "null";

        Product updatedProduct = productRepository.save(product);

        // Отправляем JMS сообщение об обновлении
        String changeDetails = String.format(
                "Product updated: ID=%d\n" +
                        "Old: name='%s', price=%.2f, category='%s'\n" +
                        "New: name='%s', price=%.2f, category='%s'",
                product.getId(),
                oldName, oldPrice, oldCategory,
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                updatedProduct.getCategory() != null ? updatedProduct.getCategory().getName() : "null"
        );

        jmsAuditService.sendProductEvent(
                "UPDATE",
                updatedProduct.getId(),
                updatedProduct.getName(),
                changeDetails
        );

        return updatedProduct;
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        if (product == null) {
            return;
        }

        // Сохраняем информацию перед удалением
        String productInfo = String.format(
                "Product: name='%s', price=%.2f, category='%s'",
                product.getName(),
                product.getPrice(),
                product.getCategory() != null ? product.getCategory().getName() : "null"
        );

        productRepository.deleteById(id);

        // Отправляем JMS сообщение об удалении
        jmsAuditService.sendProductEvent(
                "DELETE",
                id,
                product.getName(),
                "Product deleted: " + productInfo
        );
    }
}