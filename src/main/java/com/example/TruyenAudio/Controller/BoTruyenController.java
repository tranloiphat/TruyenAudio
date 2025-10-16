package com.example.TruyenAudio.Controller;

import com.example.TruyenAudio.Entities.BoTruyen;
import com.example.TruyenAudio.Entities.TheLoai;
import com.example.TruyenAudio.Services.BoTruyenService;
import com.example.TruyenAudio.Services.TheLoaiService;
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
@RequestMapping("/botruyen")
public class BoTruyenController {

    private final BoTruyenService boTruyenService;
    private final TheLoaiService theLoaiService;

    public BoTruyenController(BoTruyenService boTruyenService, TheLoaiService theLoaiService) {
        this.boTruyenService = boTruyenService;
        this.theLoaiService = theLoaiService;
    }

    @GetMapping({"", "/"})
    public String danhSachTruyen(Model model) {
        List<BoTruyen> boTruyens = boTruyenService.getAllBoTruyens();
        model.addAttribute("boTruyens", boTruyens);
        return "botruyen/index";
    }

    @GetMapping("/{id}")
    public String chiTietTruyen(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        BoTruyen boTruyen = boTruyenService.getByIdWithChaptersAndGenres(id);
        if (boTruyen == null) {
            ra.addFlashAttribute("error", "Không tìm thấy bộ truyện");
            return "redirect:/botruyen";
        }
        model.addAttribute("boTruyen", boTruyen);
        model.addAttribute("chapters", boTruyen.getChapters()); // ok nếu bật OSIV mặc định
        return "botruyen/detail";
    }

    @GetMapping("/add")
    public String addBoTruyen(Model model) {
        model.addAttribute("boTruyen", new BoTruyen());
        model.addAttribute("theLoais", theLoaiService.getAllTheLoais());
        return "botruyen/add";
    }

    @PostMapping("/add")
    public String addBoTruyen(@Valid @ModelAttribute("boTruyen") BoTruyen boTruyen, // <<< tên thống nhất
                              BindingResult br,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              @RequestParam(value = "theLoaiIds", required = false) java.util.List<Integer> theLoaiIds,
                              RedirectAttributes ra) {
        if (br.hasErrors()) return "botruyen/add";
        try {
            String imageName = boTruyenService.saveImage(imageFile);
            boTruyen.setImage(imageName);

            if (theLoaiIds != null && !theLoaiIds.isEmpty() ) {
                java.util.Set<TheLoai> selected = new java.util.HashSet<>(theLoaiService.findAllByIds(theLoaiIds));
                boTruyen.setTheLoais(selected);

            }
            boTruyenService.addBoTruyen(boTruyen);

            ra.addFlashAttribute("msg", "Thêm bộ truyện thành công");
            return "redirect:/botruyen";
        } catch (Exception e) {
            br.rejectValue("image", "uploadError", e.getMessage());
            return "botruyen/add"; // << không redirect để hiện lỗi
        }
    }

    // Khuyến nghị dùng POST để xóa (tránh xóa bằng GET)
    @PostMapping("/delete/{id}")
    public String deleteBoTruyen(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            boTruyenService.deleteBoTruyenById(id);
            ra.addFlashAttribute("msg", "Đã xóa bộ truyện");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/botruyen";
    }

    @GetMapping("/edit/{id}")
    public String editBoTruyen(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        // Dùng fetch-join để nạp sẵn theLoais => tránh lazy trong view
        BoTruyen boTruyen = boTruyenService.getByIdWithChaptersAndGenres(id);
        if (boTruyen == null) {
            ra.addFlashAttribute("error", "Không tìm thấy bộ truyện");
            return "redirect:/botruyen";
        }

        model.addAttribute("boTruyen", boTruyen);
        model.addAttribute("theLoais", theLoaiService.getAllTheLoais());

        // Snapshot danh sách ID thể loại đã chọn => view không cần gọi .contains() trên Set
        List<Integer> selectedGenreIds = boTruyen.getTheLoais()
                .stream()
                .map(tl -> tl.getTheLoaiId())
                .toList();
        model.addAttribute("selectedGenreIds", selectedGenreIds);

        return "botruyen/edit";
    }

    @PostMapping ("/edit/{id}")
    public String editBoTruyen(@PathVariable Integer id,
                               @Valid @ModelAttribute("boTruyen") BoTruyen form,
                               BindingResult br,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               @RequestParam(value = "theLoaiIds", required = false) java.util.List<Integer> theLoaiIds,
                               RedirectAttributes ra) {
        if (br.hasErrors()) return "botruyen/edit";
        try {
            BoTruyen old = boTruyenService.getbyId(id);
            if (old == null) throw new IllegalArgumentException("Không timg thấy bộ truyện để cập nhập");

            // cập nhật tên (và các trường khác nếu bạn muốn)
            old.setTenTruyen(form.getTenTruyen());
            old.setMoTaNgan(form.getMoTaNgan());
            old.setMoTaDai(form.getMoTaDai());
            old.setTrangThai(form.getTrangThai());
            old.setThoiGianPhatHanh(form.getThoiGianPhatHanh());

            // ảnh
            if (imageFile != null && !imageFile.isEmpty()) {
                String newName = boTruyenService.saveImage(imageFile);
                boTruyenService.deleteImageIfExists(old.getImage()); //
                old.setImage(newName);
            }

            // thể loại
            if (theLoaiIds != null) {
                java.util.Set<TheLoai> selected = new java.util.HashSet<>(theLoaiService.findAllByIds(theLoaiIds));
                old.setTheLoais(selected);
            }

            boTruyenService.updateBoTruyen(old);
            ra.addFlashAttribute("msg", "Cập nhập thành công");
            return "redirect:/botruyen";

        } catch (IOException e) {
            br.rejectValue("image","uploadError",e.getMessage());
            return "botruyen/edit";
        } catch (IllegalArgumentException notFound) {
            ra.addFlashAttribute("error", notFound.getMessage());
            return "redirect:/botruyen";

    }

}
}
