package com.example.TruyenAudio.Services;

import com.example.TruyenAudio.Entities.TheLoai;
import com.example.TruyenAudio.Reponsitories.ITheLoaiReponsitory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class TheLoaiService {
    private final ITheLoaiReponsitory theLoaiReponsitory;
    public TheLoaiService(ITheLoaiReponsitory theLoaiReponsitory) {
        this.theLoaiReponsitory = theLoaiReponsitory;
    }

    @Value("${upload.path}")
    private String uploadPath;
    private static final Set<String> ALLOW = Set.of(
            "image/png", "image/jpeg", "image/webp", "image/gif"
    );

    public List<TheLoai> getAllTheLoais() {
        return theLoaiReponsitory.findAll();
    }
    public TheLoai getTheLoaiById(Integer id) {
        return theLoaiReponsitory.findById(id).orElse(null);
    }
    public TheLoai getWithBoTruyens(Integer id) {
        return theLoaiReponsitory.findWithBoTruyens(id).orElse(null);
    }

    public TheLoai createTheLoai(TheLoai theLoai) {
        return theLoaiReponsitory.save(theLoai);
    }
    public void updateTheLoai(TheLoai theLoai){
        if(theLoai != null)
            theLoaiReponsitory.save(theLoai);
    }

    public void deleteById(Integer id) {
        TheLoai theLoai = getTheLoaiById(id);
        if (theLoai == null) throw new IllegalArgumentException("Không tìm thấy thể loại để xóa");
        deleteImageIfExists(theLoai.getImage());
        theLoaiReponsitory.deleteById(id);
    }

    public String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) return null;

        String contentType = image.getContentType();
        if (contentType == null || !ALLOW.contains(contentType)) {
            throw new IOException("Định dạng không hợp lệ (chỉ hỗ trợ: jpg/jpeg, png, webp, gif)");
        }
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
        String original = image.getOriginalFilename();
        String ext = getExtensionSafe(original);
        String uniqueFileName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path filePath = uploadDir.resolve(uniqueFileName);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    public void deleteImageIfExists(String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        try {
            Path p = Paths.get(uploadPath).resolve(fileName);
            Files.deleteIfExists(p);
        } catch (Exception ignored) {}
    }

    private String getExtensionSafe(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) return "";
        return filename.substring(dot + 1).toLowerCase();
    }

    public java.util.List<TheLoai> findAllByIds(java.util.Collection<Integer> ids){
        return theLoaiReponsitory.findAllById(ids);
    }
}
