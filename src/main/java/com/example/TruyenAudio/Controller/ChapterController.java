package com.example.TruyenAudio.Controller;

import com.example.TruyenAudio.Entities.Chapter;
import com.example.TruyenAudio.Services.ChapterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.example.TruyenAudio.Entities.BoTruyen;
import com.example.TruyenAudio.Services.BoTruyenService;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;


@Controller
@RequestMapping("/chapter")
public class ChapterController {

    private final ChapterService chapterService;
    private final BoTruyenService boTruyenService;

    public ChapterController(ChapterService chapterService, BoTruyenService boTruyenService) {
        this.chapterService = chapterService;
        this.boTruyenService = boTruyenService;
    }

    // luôn luôn KHÔNG bind file vào String pdfFile của entity
    @InitBinder("chapter")
    public void initBinder(WebDataBinder binder) { binder.setDisallowedFields("pdfFile"); }

    // cho mọi view trong controller này đều có sẵn danh sách bộ truyện
    @ModelAttribute("boTruyenList")
    public List<BoTruyen> loadBoTruyen() {
        return boTruyenService.getAllBoTruyens();
    }

    @GetMapping({"", "/"})
    public String danhSachChapter(Model model) {
        model.addAttribute("chapters", chapterService.getAllChapters());
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
        return "chapter/detail"; // View sẽ render từng trang PDF
    }

    @GetMapping("/add")
    public String addChapter(Model model) {
        model.addAttribute("chapter", new Chapter());
        return "chapter/add";
    }

    @PostMapping("/add")
    public String addChapter(@Valid @ModelAttribute("chapter") Chapter chapter,
                             BindingResult br,
                             @RequestParam("boTruyenId") Integer boTruyenId,
                             @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
                             RedirectAttributes ra) {
        if (br.hasErrors()) return "chapter/add";
        try {
            if (pdfFile == null || pdfFile.isEmpty()) {
                br.rejectValue("pdfFile", "required", "Vui lòng chọn file PDF");
                return "chapter/add";
            }
            BoTruyen bt = boTruyenService.getbyId(boTruyenId);
            if (bt == null) {
                br.rejectValue("boTruyen", "invalid", "Bộ truyện không hợp lệ");
                return "chapter/add";
            }
            chapter.setBoTruyen(bt); // <<< QUAN TRỌNG
            chapter.setPdfFile(chapterService.savePdf(pdfFile));
            chapterService.save(chapter);
            ra.addFlashAttribute("msg", "Thêm chapter thành công");
            return "redirect:/chapter";
        } catch (IOException e) {
            br.rejectValue("pdfFile", "uploadError", e.getMessage());
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
                              @RequestParam("boTruyenId") Integer boTruyenId,
                              @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
                              RedirectAttributes ra) {
        if (br.hasErrors()) return "chapter/edit";
        try {
            BoTruyen bt = boTruyenService.getbyId(boTruyenId);
            if (bt == null) {
                br.rejectValue("boTruyen", "invalid", "Bộ truyện không hợp lệ");
                return "chapter/edit";
            }
            form.setBoTruyen(bt); // <<< QUAN TRỌNG
            chapterService.updateWithPdf(id, form, pdfFile);
            ra.addFlashAttribute("msg", "Cập nhật thành công");
            return "redirect:/chapter";
        } catch (IOException e) {
            br.rejectValue("pdfFile", "uploadError", e.getMessage());
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
            ra.addFlashAttribute("msg", "Đã xoá chapter");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/chapter";
    }

    // Stream file PDF để PDF.js đọc
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/file/{name:.+}")
    public ResponseEntity<Resource> getPdf(@PathVariable("name") String name) {
        try {
            Path base = Paths.get(uploadPath).toAbsolutePath().normalize();
            Path file = base.resolve(name).normalize();
            if (!file.startsWith(base) || !Files.exists(file)) return ResponseEntity.notFound().build();

            Resource resource = new UrlResource(file.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
