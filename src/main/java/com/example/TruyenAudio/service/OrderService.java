package com.example.TruyenAudio.service;

import com.example.TruyenAudio.model.*;
import com.example.TruyenAudio.repository.CustomerOrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CustomerOrderRepository orderRepository;

    public OrderService(CustomerOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CustomerOrder createOrder(CheckoutForm form, CartService cartService) {
        if (cartService.isEmpty()) {
            throw new IllegalStateException("Giỏ hàng trống");
        }

        CustomerOrder order = new CustomerOrder();
        order.setCustomerName(form.getFullName());
        order.setEmail(form.getEmail());
        order.setPhone(form.getPhone());
        order.setAddress(form.getAddress());
        order.setNote(form.getNote());
        order.setTotal(cartService.getTotalAmount());

        cartService.getItems().forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setProductName(item.getProductName());
            orderItem.setProductImage(item.getImageUrl());
            orderItem.setUnitPrice(item.getUnitPrice());
            orderItem.setQuantity(item.getQuantity());
            order.addItem(orderItem);
        });

        CustomerOrder saved = orderRepository.save(order);
        cartService.clear();
        return saved;
    }
}
