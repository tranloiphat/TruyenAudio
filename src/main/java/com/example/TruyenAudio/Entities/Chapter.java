package com.example.TruyenAudio.Entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name ="Chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ChapterId")
    private Integer ChapterId;

    // Nếu là số thứ tự, tên gợi ý "SoThuTu" sẽ rõ nghĩa hơn
    @Column(name = "TenChapter") // hoặc đổi tên cột DB => "SoThuTu"
    private Integer TenChapter;

    @Column(name = "PdfFile")
    private String PdfFile;

    @ManyToOne
    @JoinColumn (name = "BoTruyen",nullable = true )
    private BoTruyen BoTruyen;




}
