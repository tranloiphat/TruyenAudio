package com.example.TruyenAudio.controller;

import com.example.TruyenAudio.model.CheckoutForm;
import com.example.TruyenAudio.model.CustomerOrder;
import com.example.TruyenAudio.service.CartService;
import com.example.TruyenAudio.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping
    public String checkoutForm(Model model) {
        if (cartService.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("pageTitle", "Thanh toán");
        model.addAttribute("checkoutForm", new CheckoutForm());
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("total", cartService.getTotalAmount());
        return "checkout";
    }

    @PostMapping
    public String placeOrder(@Valid @ModelAttribute("checkoutForm") CheckoutForm form,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Thanh toán");
            model.addAttribute("items", cartService.getItems());
            model.addAttribute("total", cartService.getTotalAmount());
            return "checkout";
        }

        try {
            CustomerOrder order = orderService.createOrder(form, cartService);
            model.addAttribute("pageTitle", "Đặt hàng thành công");
            model.addAttribute("order", order);
            return "checkout-success";
        } catch (IllegalStateException e) {
            return "redirect:/cart";
        }
    }
}
