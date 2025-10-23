package com.example.TruyenAudio.controller;

import com.example.TruyenAudio.model.Category;
import com.example.TruyenAudio.service.CartService;
import com.example.TruyenAudio.service.CategoryService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {

    private final CartService cartService;
    private final CategoryService categoryService;

    public GlobalModelAttributes(CartService cartService, CategoryService categoryService) {
        this.cartService = cartService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("cartItemCount")
    public int cartItemCount() {
        return cartService.getTotalQuantity();
    }

    @ModelAttribute("navCategories")
    public List<Category> navCategories() {
        return categoryService.findAll();
    }
}
