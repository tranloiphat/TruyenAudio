package com.example.TruyenAudio.controller;

import com.example.TruyenAudio.model.Category;
import com.example.TruyenAudio.model.Product;
import com.example.TruyenAudio.service.CategoryService;
import com.example.TruyenAudio.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public HomeController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = categoryService.findAll();
        List<Product> latestProducts = productService.findLatestProducts();
        model.addAttribute("pageTitle", "Thiên Long Stationery");
        model.addAttribute("categories", categories);
        model.addAttribute("latestProducts", latestProducts);
        return "home";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("pageTitle", "Danh mục sản phẩm");
        model.addAttribute("categories", categoryService.findAll());
        return "categories";
    }

    @GetMapping("/categories/{id}")
    public String categoryDetail(@PathVariable Long id, Model model) {
        return categoryService.findById(id)
                .map(category -> {
                    List<Product> products = productService.findByCategory(id);
                    model.addAttribute("pageTitle", category.getName());
                    model.addAttribute("category", category);
                    model.addAttribute("products", products);
                    return "category-detail";
                }).orElse("redirect:/categories");
    }

    @GetMapping("/search")
    public String search(@RequestParam("q") String keyword, Model model) {
        List<Product> products = productService.searchByName(keyword);
        model.addAttribute("pageTitle", "Tìm kiếm: " + keyword);
        model.addAttribute("keyword", keyword);
        model.addAttribute("products", products);
        return "search";
    }
}
