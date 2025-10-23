package com.example.TruyenAudio.Controller;
import com.example.TruyenAudio.Entities.User;
import com.example.TruyenAudio.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                model.addAttribute(error.getField() + "_error",
                        error.getDefaultMessage());
            }
            return "user/register";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping
    public String danhSachUser(Model model) {
        List<User> danhSach = userService.getAllUsers();
        model.addAttribute("users", danhSach);
        return "user/index"; // view nên để riêng thư mục user
    }

    @GetMapping("/{id}")
    public String chiTietUser(@PathVariable Integer id, Model model) {
        User user = userService.getbyId(id);
        model.addAttribute("user", user);
        return "user/detail";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "user/add";
    }
    @PostMapping("/add")
    public String addUser(User user){
        userService.addUser(user);
        return "redirect:/user";
    }

    @GetMapping("/delete")
    public String deleteUser(User user){
        userService.delete(user);
        return "redirect:/user";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Integer id, Model model) {
        User user = userService.getbyId(id);
        if (user == null) return "redirect:/user";
        model.addAttribute("user", user);
        return "user/edit";
    }
    @PostMapping ("/edit/{id}")
    public String editUser(User user){
        userService.updateUser(user);
        return "redirect:/user";
    }

}
