package com.example.demo.Services;

import com.example.demo.Entities.Chapter;
import com.example.demo.Reponsitories.IChapterReponsitory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ChapterService {

    private final IChapterReponsitory chapterReponsitory;

    public ChapterService(IChapterReponsitory chapterReponsitory) {
        this.chapterReponsitory = chapterReponsitory;
    }

    @Value("${upload.path}")
    private String uploadPath;

    public static final Set<String> ALLOW = Set.of("image/png", "image/jpeg", "image/gif", "image/webp");

    public List<Chapter> getAllChapters(){
        return chapterReponsitory.findAll();
    }

    public Chapter findById(Integer id){
        return chapterReponsitory.findById(id).orElse(null);
    }

    public Chapter save(Chapter chapter){
        return chapterReponsitory.save(chapter);
    }

    public void deleteChapterById(Integer id){
        Chapter chapter = findById(id);
        if (chapter == null){
            throw new IllegalArgumentException("Không tìm thấy chapter để xóa");
        }
        deleteImageIfExists(chapter.getImage());
        chapterReponsitory.deleteById(id);
    }

    // (Có thể bỏ nếu không dùng riêng)
    public void addChapter(Chapter chapter){ chapterReponsitory.save(chapter); }

    public void updateChapter(Chapter chapter){ chapterReponsitory.save(chapter); }

    // GỢI Ý: đổi tên rõ nghĩa hoặc xóa nếu chưa có repo method tương ứng
    // public List<Chapter> findByBoTruyenId(Integer boTruyenId){
    //     return chapterReponsitory.findByBoTruyenId(boTruyenId);
    // }

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

    private String getExtensionSafe(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) return "";
        return filename.substring(dot + 1).toLowerCase();
    }

    public void deleteImageIfExists(String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        try {
            Path p = Paths.get(uploadPath).resolve(fileName);
            Files.deleteIfExists(p);
        } catch (Exception ignored) {}
    }

    /** Cập nhật + xử lý ảnh gói gọn trong Service */
    public Chapter updateWithImage(Integer id, Chapter form, MultipartFile imageFile) throws IOException {
        Chapter old = findById(id);
        if (old == null) throw new IllegalArgumentException("Không tìm thấy Chapter để cập nhật");

        // 1) Cập nhật các field cơ bản từ form
        old.setTenChapter(form.getTenChapter());
        // nếu sau này có thêm các field khác thì set tương tự...

        // 2) Ảnh: giữ ảnh cũ, nếu có file mới thì lưu mới và xóa cũ
        String imageName = old.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            String newName = saveImage(imageFile);
            deleteImageIfExists(imageName);
            imageName = newName;
        }
        old.setImage(imageName);

        return chapterReponsitory.save(old);
    }
}
