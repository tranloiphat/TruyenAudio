package com.example.TruyenAudio.Services;

import com.example.TruyenAudio.Entities.BoTruyen;
import com.example.TruyenAudio.Reponsitories.IBoTruyenReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BoTruyenService {
    @Autowired
    private IBoTruyenReponsitory boTruyenReponsitory;

    @Value("${upload.path}")
    private String uploadPath;

    private static final Set<String> ALLOWED_MIME = Set.of("image/jpeg","image/png","image/webp","image/gif");
    private static final Set<String> ALLOWED_EXT  = Set.of("jpg","jpeg","png","webp","gif");

    public List<BoTruyen> getAllBoTruyens(){
        return boTruyenReponsitory.findAll(); }

    public BoTruyen getbyId(Integer id){
        return boTruyenReponsitory.findById(id).orElse(null); }

    public void deleteBoTruyenById(Integer id){
        BoTruyen boTruyen = getbyId(id);
        if (boTruyen == null) throw new IllegalArgumentException("Không tìm thấy bộ truyện để xóa");
        deleteImageIfExists(boTruyen.getImage());
        boTruyenReponsitory.deleteById(id);
    }

    public void deleteImageIfExists(String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        try {
            Path base = Paths.get(uploadPath).toAbsolutePath().normalize();
            Path p = base.resolve(fileName).normalize();
            if (!p.startsWith(base)) return; // chặn path traversal
            Files.deleteIfExists(p);
        } catch (IOException ignored) {}
    }

    public void addBoTruyen(BoTruyen boTruyen){
        boTruyenReponsitory.save(boTruyen); }

    public void updateBoTruyen(BoTruyen boTruyen){ boTruyenReponsitory.save(boTruyen); }

    public String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) return null;

        String contentType = image.getContentType();
        String original = image.getOriginalFilename();
        String ext = getExtensionSafe(original);

        if ((contentType == null || !ALLOWED_MIME.contains(contentType)) && !ALLOWED_EXT.contains(ext)) {
            throw new IOException("Định dạng không hợp lệ (jpg/jpeg, png, webp, gif)");
        }

        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

        String uniqueFileName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path filePath = uploadDir.resolve(uniqueFileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private String getExtensionSafe(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) return "";
        return filename.substring(dot + 1).toLowerCase();
    }

    public BoTruyen getByIdWithChaptersAndGenres(Integer id){
        return boTruyenReponsitory.findWithChaptersAndGenres(id).orElse(null);
    }



}
