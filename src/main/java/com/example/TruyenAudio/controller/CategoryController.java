package com.example.TruyenAudio.controller;

import com.example.TruyenAudio.model.Category;
import com.example.TruyenAudio.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories/admin")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String manageCategories(Model model) {
        model.addAttribute("pageTitle", "Quản lý danh mục");
        model.addAttribute("categories", categoryService.findAll());
        return "admin/category-list";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        model.addAttribute("pageTitle", "Thêm danh mục mới");
        model.addAttribute("category", new Category());
        return "admin/category-form";
    }

    @PostMapping
    public String createCategory(@Valid @ModelAttribute("category") Category category,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Thêm danh mục mới");
            return "admin/category-form";
        }
        categoryService.save(category);
        return "redirect:/categories/admin";
    }

    @GetMapping("/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
        return categoryService.findById(id)
                .map(category -> {
                    model.addAttribute("pageTitle", "Cập nhật danh mục");
                    model.addAttribute("category", category);
                    return "admin/category-form";
                }).orElse("redirect:/categories/admin");
    }

    @PostMapping("/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @Valid @ModelAttribute("category") Category category,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Cập nhật danh mục");
            return "admin/category-form";
        }
        category.setId(id);
        categoryService.save(category);
        return "redirect:/categories/admin";
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/categories/admin";
    }
}
