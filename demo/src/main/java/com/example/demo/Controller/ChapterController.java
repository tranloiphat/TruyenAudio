package com.example.demo.Controller;

import com.example.demo.Entities.Chapter;
import com.example.demo.Services.ChapterService;
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
@RequestMapping("/chapter")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping
    public String danhSachChapter(Model model) {
        List<Chapter> chapters = chapterService.getAllChapters();
        model.addAttribute("chapters", chapters);
        return "chapter/index";
    }

    @GetMapping("/{id}")
    public String chiTietChapter(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        Chapter chapter = chapterService.findById(id);
        if (chapter == null) {
            ra.addFlashAttribute("error", "Không tìm thấy chapter");
            return "redirect:/chapter";
        }
        model.addAttribute("chapter", chapter);
        return "chapter/detail";
    }

    @GetMapping("/add")
    public String addChapter(Model model) {
        model.addAttribute("chapter", new Chapter());
        return "chapter/add";
    }

    @PostMapping("/add")
    public String addChapter(@Valid @ModelAttribute("chapter") Chapter chapter,
                             BindingResult br,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             RedirectAttributes ra) {
        if (br.hasErrors()) return "chapter/add";
        try {
            String imageName = chapterService.saveImage(imageFile);
            chapter.setImage(imageName);
            chapterService.save(chapter);
            ra.addFlashAttribute("msg", "Thêm chapter thành công");
            return "redirect:/chapter";
        } catch (IOException e) {
            br.rejectValue("image", "uploadError", e.getMessage());
            return "chapter/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        Chapter chapter = chapterService.findById(id);
        if (chapter == null) {
            ra.addFlashAttribute("error", "Không tìm thấy chapter");
            return "redirect:/chapter";
        }
        model.addAttribute("chapter", chapter);
        return "chapter/edit";
    }

    @PostMapping("/edit/{id}")
    public String editChapter(@PathVariable Integer id,
                              @Valid @ModelAttribute("chapter") Chapter form,
                              BindingResult br,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes ra) {
        if (br.hasErrors()) return "chapter/edit";
        try {
            chapterService.updateWithImage(id, form, imageFile);
            ra.addFlashAttribute("msg", "Cập nhật thành công");
            return "redirect:/chapter";
        } catch (IOException e) {
            br.rejectValue("image", "uploadError", e.getMessage());
            return "chapter/edit";
        } catch (IllegalArgumentException notFound) {
            ra.addFlashAttribute("error", notFound.getMessage());
            return "redirect:/chapter";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteChapter(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            chapterService.deleteChapterById(id);
            ra.addFlashAttribute("msg", "Đã xóa chapter");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/chapter";
    }
}
