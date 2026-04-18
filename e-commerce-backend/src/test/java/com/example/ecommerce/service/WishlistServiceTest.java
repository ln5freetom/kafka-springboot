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

import static org.junit.jupiter.api.Assertions.*;
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
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        wishlistService.addProductToWishlist(1L, 1L);

        assertEquals(1, user.getWishlist().size());
        verify(userRepository).save(user);
    }

    @Test
    void removeProductFromWishlist_ShouldRemoveProduct_WhenExists() {
        user.getWishlist().add(product);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        wishlistService.removeProductFromWishlist(1L, 1L);

        assertTrue(user.getWishlist().isEmpty());
        verify(userRepository).save(user);
    }

    @Test
    void clearWishlist_ShouldEmptyWishlist() {
        user.getWishlist().add(product);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        wishlistService.clearWishlist(1L);

        assertTrue(user.getWishlist().isEmpty());
        verify(userRepository).save(user);
    }

    @Test
    void addProductToWishlist_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                wishlistService.addProductToWishlist(99L, 1L));
    }
}
