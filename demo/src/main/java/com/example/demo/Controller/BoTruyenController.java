package com.example.demo.Controller;
import com.example.demo.Entities.Chapter;
import com.example.demo.Services.ChapterService;
import org.springframework.ui.Model;
import com.example.demo.Entities.BoTruyen;
import com.example.demo.Services.BoTruyenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/botruyen")
public class BoTruyenController {
    @Autowired
    public BoTruyenService boTruyenService;
    @Autowired
    private ChapterService chapterService;

    @GetMapping
    public String danhSachTruyen(Model model) {
        List<BoTruyen> boTruyens = boTruyenService.getAllBoTruyens();
        model.addAttribute("boTruyens", boTruyens);
        return "botruyen/index";
    }

    @GetMapping("/{id}")
    public String chiTietTruyen(@PathVariable Integer id, Model model) {
        BoTruyen boTruyen = boTruyenService.getbyId(id);
        model.addAttribute("boTruyen", boTruyen);

//        List<Chapter> chapters = chapterService.findById(id);
//        model.addAttribute("chapters", chapters);
        return "chapter/index";
    }

    @GetMapping("/add")
    public String addBoTruyen(Model model) {
        model.addAttribute("boTruyen", new BoTruyen());
        return "botruyen/add";
    }
    @PostMapping("/add")
    public String addBoTruyen(BoTruyen boTruyen){
        boTruyenService.addBoTruyen(boTruyen);
        return "redirect:/botruyen";
    }

    @GetMapping("/delete")
    public String deleteBoTruyen(BoTruyen boTruyen){
        boTruyenService.delete(boTruyen);
        return "redirect:/botruyen";
    }

    @GetMapping("/edit/{id}")
    public String editBoTruyen(@PathVariable Integer id, Model model) {
        BoTruyen boTruyen = boTruyenService.getbyId(id);
        if (boTruyen == null) return "redirect:/botruyen";
        model.addAttribute("boTruyen", boTruyen);
        return "botruyen/edit";
    }
    @PostMapping ("/edit/{id}")
    public String editBoTruyen(BoTruyen boTruyen){
        boTruyenService.updateBoTruyen(boTruyen);
        return "redirect:/botruyen";
    }


}
