package com.example.demo.Entities;

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

    @Column(name = "Image")
    private String Image;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTiet_BoTruyen> chiTietBoTruyens = new HashSet<>();
}
