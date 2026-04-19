package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductRequest productRequest;
    private Product product;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(BigDecimal.valueOf(99.99));
        productRequest.setStock(10);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setStock(10);
    }

    @Test
    void createProduct_ShouldCreateProduct() {
        // Given
        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        var created = productService.createProduct(productRequest);

        // Then
        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("Test Product");
        assertThat(created.getPrice()).isEqualTo(BigDecimal.valueOf(99.99));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When
        var result = productService.getProductById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("Test Product");
    }

    @Test
    void getProductById_ShouldReturnEmpty_WhenProductDoesNotExist() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        var result = productService.getProductById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        var products = List.of(product, new Product());
        when(productRepository.findAll()).thenReturn(products);

        // When
        var result = productService.getAllProducts();

        // Then
        assertThat(result).hasSize(2);
        verify(productRepository).findAll();
    }

    @Test
    void getAllProducts_ShouldReturnEmptyList_WhenNoProducts() {
        // Given
        when(productRepository.findAll()).thenReturn(List.of());

        // When
        var result = productService.getAllProducts();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void searchProductsByName_ShouldReturnMatchingProducts() {
        // Given
        when(productRepository.findByNameContainingIgnoreCase("phone"))
                .thenReturn(List.of());

        // When
        var result = productService.searchProductsByName("phone");

        // Then
        assertThat(result).isEmpty();
        verify(productRepository).findByNameContainingIgnoreCase("phone");
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenProductExists() {
        // Given
        var updateRequest = new ProductRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setPrice(BigDecimal.valueOf(199.99));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productMapper).updateEntity(updateRequest, any(Product.class));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        var updated = productService.updateProduct(1L, updateRequest);

        // Then
        assertThat(updated).isNotNull();
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductDoesNotExist() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> productService.updateProduct(999L, productRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found with id: 999");
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_ShouldDeleteProduct() {
        // Given
        doNothing().when(productRepository).deleteById(1L);

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository).deleteById(1L);
    }
}
