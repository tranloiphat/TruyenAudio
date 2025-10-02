package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table (name = "ChiTiet_BoTruyen")
public class ChiTiet_BoTruyen {
    @Id
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ChiTietBoTruyenId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChapterId")
    private Chapter chapter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BoTruyenId")
    private BoTruyen boTruyen;


    // Constructors
    public ChiTiet_BoTruyen() {}

    public ChiTiet_BoTruyen(BoTruyen boTruyen, Chapter chapter) {
        this.boTruyen = boTruyen;
        this.chapter = chapter;
    }

    @Column(name = "ThoiGianCapNhap")
    private Date ThoiGianCapNhap;


    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public BoTruyen getBoTruyen() {
        return boTruyen;
    }

    public void setBoTruyen(BoTruyen boTruyen) {
        this.boTruyen = boTruyen;
    }
}
