package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        return productRepository.save(product);
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @CachePut(value = "products", key = "#id")
    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(Long id, ProductRequest request) {
        return productRepository.findById(id)
                .map(existing -> {
                    productMapper.updateEntity(request, existing);
                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
