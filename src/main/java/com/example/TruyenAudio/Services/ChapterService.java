package com.example.TruyenAudio.Services;

import com.example.TruyenAudio.Entities.Chapter;
import com.example.TruyenAudio.Reponsitories.IChapterReponsitory;
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

    private static final Set<String> ALLOW_PDF_MIME = Set.of("application/pdf");
    private static final Set<String> ALLOW_PDF_EXT  = Set.of("pdf");

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
            throw new IllegalArgumentException("Không tìm thấy chapter để xoá");
        }
        deleteFileIfExists(chapter.getPdfFile());
        chapterReponsitory.deleteById(id);
    }

    /** Upload PDF duy nhất */
    public String savePdf(MultipartFile pdf) throws IOException {
        if (pdf == null || pdf.isEmpty()) return null;
        String contentType = pdf.getContentType();
        String ext = getExtensionSafe(pdf.getOriginalFilename());
        if ((contentType == null || !ALLOW_PDF_MIME.contains(contentType)) || !ALLOW_PDF_EXT.contains(ext)) {
            throw new IOException("Chỉ cho phép upload file PDF (.pdf)");
        }
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
        String unique = UUID.randomUUID() + ".pdf";
        Path filePath = uploadDir.resolve(unique);
        Files.copy(pdf.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return unique;
    }

    /** Update dữ liệu + thay PDF nếu có */
    public Chapter updateWithPdf(Integer id, Chapter form, MultipartFile pdfFile) throws IOException {
        Chapter old = findById(id);
        if (old == null) throw new IllegalArgumentException("Không tìm thấy Chapter để cập nhật");
        old.setTenChapter(form.getTenChapter());
        old.setBoTruyen(form.getBoTruyen());
        String pdfName = old.getPdfFile();
        if (pdfFile != null && !pdfFile.isEmpty()) {
            String newPdf = savePdf(pdfFile);
            deleteFileIfExists(pdfName);
            pdfName = newPdf;
        }
        old.setPdfFile(pdfName);
        return chapterReponsitory.save(old);
    }

    private String getExtensionSafe(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) return "";
        return filename.substring(dot + 1).toLowerCase();
    }

    public void deleteFileIfExists(String fileName) {
        if (fileName == null || fileName.isBlank()) return;
        try {
            Path base = Paths.get(uploadPath).toAbsolutePath().normalize();
            Path p = base.resolve(fileName).normalize();
            if (!p.startsWith(base)) return; // chặn path traversal
            Files.deleteIfExists(p);
        } catch (Exception ignored) {}
    }
}
