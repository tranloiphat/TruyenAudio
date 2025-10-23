package com.example.TruyenAudio.controller;

import com.example.TruyenAudio.service.CartService;
import com.example.TruyenAudio.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("pageTitle", "Giỏ hàng của bạn");
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("total", cartService.getTotalAmount());
        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                             @RequestParam(defaultValue = "1") int quantity,
                             @RequestHeader(value = "Referer", required = false) String referer,
                             RedirectAttributes redirectAttributes) {
        return productService.findById(productId)
                .map(product -> {
                    cartService.addProduct(product, quantity);
                    redirectAttributes.addFlashAttribute("message", "Đã thêm sản phẩm vào giỏ hàng");
                    return "redirect:" + (referer != null ? referer : "/products");
                }).orElse("redirect:/products");
    }

    @PostMapping("/update/{productId}")
    public String updateQuantity(@PathVariable Long productId,
                                  @RequestParam int quantity) {
        cartService.updateQuantity(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeItem(@PathVariable Long productId) {
        cartService.remove(productId);
        return "redirect:/cart";
    }
}
