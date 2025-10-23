package com.example.TruyenAudio.controller;

import com.example.TruyenAudio.model.Category;
import com.example.TruyenAudio.model.Product;
import com.example.TruyenAudio.service.CategoryService;
import com.example.TruyenAudio.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String productList(Model model) {
        model.addAttribute("pageTitle", "Tất cả sản phẩm");
        model.addAttribute("products", productService.findAll());
        return "products";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        return productService.findById(id)
                .map(product -> {
                    model.addAttribute("pageTitle", product.getName());
                    model.addAttribute("product", product);
                    List<Product> related;
                    if (product.getCategory() != null) {
                        related = productService.findByCategory(product.getCategory().getId()).stream()
                                .filter(p -> !p.getId().equals(product.getId()))
                                .limit(4)
                                .collect(Collectors.toList());
                    } else {
                        related = productService.findLatestProducts();
                    }
                    model.addAttribute("relatedProducts", related);
                    return "product-detail";
                }).orElse("redirect:/products");
    }

    @GetMapping("/admin")
    public String adminProducts(Model model) {
        model.addAttribute("pageTitle", "Quản lý sản phẩm");
        model.addAttribute("products", productService.findAll());
        return "admin/product-list";
    }

    @GetMapping("/admin/new")
    public String newProductForm(Model model) {
        model.addAttribute("pageTitle", "Thêm sản phẩm mới");
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    @PostMapping("/admin")
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("pageTitle", "Thêm sản phẩm mới");
            return "admin/product-form";
        }
        applyCategory(product);
        productService.save(product);
        return "redirect:/products/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        return productService.findById(id)
                .map(product -> {
                    model.addAttribute("pageTitle", "Cập nhật sản phẩm");
                    model.addAttribute("product", product);
                    model.addAttribute("categories", categoryService.findAll());
                    return "admin/product-form";
                }).orElse("redirect:/products/admin");
    }

    @PostMapping("/admin/{id}")
    public String updateProduct(@PathVariable Long id,
                                 @Valid @ModelAttribute("product") Product product,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("pageTitle", "Cập nhật sản phẩm");
            return "admin/product-form";
        }
        product.setId(id);
        applyCategory(product);
        productService.save(product);
        return "redirect:/products/admin";
    }

    @PostMapping("/admin/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products/admin";
    }

    private void applyCategory(Product product) {
        Long categoryId = Optional.ofNullable(product.getCategory())
                .map(Category::getId)
                .orElse(null);
        if (categoryId != null) {
            categoryService.findById(categoryId)
                    .ifPresent(product::setCategory);
        } else {
            product.setCategory(null);
        }
    }
}
