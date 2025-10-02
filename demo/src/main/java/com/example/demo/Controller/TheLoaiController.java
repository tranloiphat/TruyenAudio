package com.example.demo.Controller;

import com.example.demo.Entities.TheLoai;
import com.example.demo.Services.TheLoaiService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/theloai")
public class TheLoaiController {

    private final TheLoaiService theLoaiService;

    public TheLoaiController(TheLoaiService theLoaiService) {
        this.theLoaiService = theLoaiService;
    }

    @GetMapping
    public String danhSachTheLoai(Model model) {
        List<TheLoai> danhSach = theLoaiService.getAllTheLoais();
        model.addAttribute("theloais", danhSach);
        return "theloai/index";
    }

    @GetMapping("/{id}")
    public String chiTietTheLoai(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        TheLoai theLoai = theLoaiService.findById(id);
        if (theLoai == null) {
            ra.addFlashAttribute("error", "Không tìm thấy thể loại.");
            return "redirect:/theloai";
        }
        model.addAttribute("theloai", theLoai);
        return "theloai/detail";
    }

    @GetMapping("/add")
    public String addTheLoaiForm(Model model) {
        model.addAttribute("theloai", new TheLoai());
        return "theloai/add";
    }

    @PostMapping("/add")
    public String addTheLoai(@Valid @ModelAttribute("theloai") TheLoai theLoai,
                             BindingResult br,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             RedirectAttributes ra) {
        if (br.hasErrors()) return "theloai/add";
        try {
            String imageName = theLoaiService.saveImage(imageFile);
            theLoai.setImage(imageName);
            theLoaiService.save(theLoai);
            ra.addFlashAttribute("msg", "Thêm thể loại thành công");
            return "redirect:/theloai";
        } catch (IOException e) {
            br.rejectValue("image", "uploadError", e.getMessage());
            return "theloai/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editTheLoaiForm(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        TheLoai theLoai = theLoaiService.findById(id);
        if (theLoai == null) {
            ra.addFlashAttribute("error", "Không tìm thấy thể loại.");
            return "redirect:/theloai";
        }
        model.addAttribute("theloai", theLoai);
        return "theloai/edit";
    }

    @PostMapping ("/edit/{id}")
    public String editTheLoai(@PathVariable Integer id,
                              @Valid @ModelAttribute("theloai") TheLoai form,
                              BindingResult br,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes ra) {
        if (br.hasErrors()) return "theloai/edit";

        try {
            theLoaiService.updateWithImage(id, form, imageFile);
            ra.addFlashAttribute("msg", "Cập nhật thành công");
            return "redirect:/theloai";
        } catch (IOException e) {
            br.rejectValue("image", "uploadError", e.getMessage());
            return "theloai/edit";
        } catch (IllegalArgumentException notFound) {
            ra.addFlashAttribute("error", notFound.getMessage());
            return "redirect:/theloai";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteTheLoai(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            theLoaiService.deleteById(id);
            ra.addFlashAttribute("msg", "Đã xóa thể loại");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/theloai";
    }
}
