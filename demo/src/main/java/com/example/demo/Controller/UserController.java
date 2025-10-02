package com.example.demo.Controller;
import com.example.demo.Entities.User;
import com.example.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

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
