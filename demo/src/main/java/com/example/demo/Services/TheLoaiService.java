package com.example.demo.Services;

import com.example.demo.Entities.TheLoai;
import com.example.demo.Reponsitories.ITheLoaiReponsitory;
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

    public TheLoai findById(Integer id) {
        return theLoaiReponsitory.findById(id).orElse(null);
    }

    public TheLoai save(TheLoai theLoai) {
        return theLoaiReponsitory.save(theLoai);
    }

    public void deleteById(Integer id) {
        TheLoai theLoai = findById(id);
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

    /** Cập nhật + xử lý ảnh gói gọn trong Service */
    public TheLoai updateWithImage(Integer id, TheLoai form, MultipartFile imageFile) throws IOException {
        TheLoai old = findById(id);
        if (old == null) throw new IllegalArgumentException("Không tìm thấy thể loại để cập nhật");

        // Giữ nguyên image cũ mặc định
        String imageName = old.getImage();

        // Nếu có upload ảnh mới thì lưu mới và xóa ảnh cũ
        if (imageFile != null && !imageFile.isEmpty()) {
            String newName = saveImage(imageFile);
            deleteImageIfExists(imageName);
            imageName = newName;
        }

        // Cập nhật field
        old.setTenTheLoai(form.getTenTheLoai());
        old.setMoTa(form.getMoTa());
        old.setImage(imageName);

        return theLoaiReponsitory.save(old);
    }
}
