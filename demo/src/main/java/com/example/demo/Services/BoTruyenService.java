package com.example.demo.Services;

import com.example.demo.Entities.BoTruyen;
import com.example.demo.Reponsitories.IBoTruyenReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BoTruyenService {
    @Autowired
    private IBoTruyenReponsitory boTruyenReponsitory;
    @Value("${upload.path}")
    private String uploadPath;

    private static final Set<String> ALLOWED_EXT = Set.of("jpg","jpeg","png","webp","gif");

    public List<BoTruyen> getAllBoTruyens(){
        return boTruyenReponsitory.findAll();
    }
    public BoTruyen getbyId(Integer id){
        return boTruyenReponsitory.findById(id).orElse(null);
    }
    public void delete(BoTruyen boTruyen){
        boTruyenReponsitory.delete(boTruyen);
    }
    public void addBoTruyen(BoTruyen boTruyen){
        boTruyenReponsitory.save(boTruyen);
    }
    public void updateBoTruyen(BoTruyen boTruyen){
        boTruyenReponsitory.save(boTruyen);
    }

    public MultipartFile saveImage(MultipartFile image, BoTruyen boTruyen) throws IOException {
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        String originalFileName = image.getOriginalFilename();
        if (!ALLOWED_EXT.contains(originalFileName)) {
            throw new IllegalArgumentException("Chỉ cho phép ảnh: " + ALLOWED_EXT);
        }
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        Path filePath = uploadDir.resolve(uniqueFileName);
        Files.copy(image.getInputStream(), filePath);
        return image;
    }
}
