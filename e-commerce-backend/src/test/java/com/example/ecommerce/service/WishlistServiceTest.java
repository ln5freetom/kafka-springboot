package com.example.ecommerce.service;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private WishlistService wishlistService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setWishlist(new ArrayList<>());

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
    }

    @Test
    void addProductToWishlist_ShouldAddProduct_WhenNotAlreadyAdded() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When
        wishlistService.addProductToWishlist(1L, 1L);

        // Then
        assertThat(user.getWishlist()).hasSize(1);
        assertThat(user.getWishlist()).contains(product);
        verify(userRepository).save(user);
    }

    @Test
    void addProductToWishlist_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> wishlistService.addProductToWishlist(999L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
        verify(userRepository, never()).save(any());
    }

    @Test
    void addProductToWishlist_ShouldThrowException_WhenProductNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> wishlistService.addProductToWishlist(1L, 999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found");
        verify(userRepository, never()).save(any());
    }

    @Test
    void removeProductFromWishlist_ShouldRemoveProduct_WhenProductExistsInWishlist() {
        // Given
        user.getWishlist().add(product);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When
        wishlistService.removeProductFromWishlist(1L, 1L);

        // Then
        assertThat(user.getWishlist()).isEmpty();
        verify(userRepository).save(user);
    }

    @Test
    void clearWishlist_ShouldEmptyWishlistCompletely() {
        // Given
        user.getWishlist().add(product);
        user.getWishlist().add(new Product());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        wishlistService.clearWishlist(1L);

        // Then
        assertThat(user.getWishlist()).isEmpty();
        verify(userRepository).save(user);
    }

    @Test
    void getWishlist_ShouldReturnUserWishlist() {
        // Given
        user.getWishlist().add(product);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        var result = wishlistService.getWishlist(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).contains(product);
    }

    @Test
    void getWishlist_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> wishlistService.getWishlist(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }
}
