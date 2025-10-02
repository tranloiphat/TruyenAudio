package com.example.demo.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name= "BoTruyen")
@Table(name= "BoTruyen")
public class BoTruyen {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BoTruyenId")
    private Integer BoTruyenId;

    @NotNull(message= "Tên truyện không được để trống")
    @Column(name = "TenTruyen")
    private String TenTruyen;

    @Column (name = "Image")
    private String Image;

    @Column(name = "MoTaNgan")
    private String MoTaNgan;

    @Column(name = "MoTaDai")
    private String MoTaDai;


    @Column(name = "Views")
    private Integer Views;


    @Column(name = "TrangThai")
    private String TrangThai="Đang Phát Hành";


    @Column(name = "ThoiGianPhatHanh")
    private LocalDate ThoiGianPhatHanh;

    @ManyToMany
    @JoinTable(name = "ChiTiet_TheLoai",
            joinColumns = {@JoinColumn (name = "BoTruyenId")},
    inverseJoinColumns ={@JoinColumn (name = "TheLoaiId")} )
    private Set<TheLoai> theLoais= new HashSet();

    @OneToMany(mappedBy = "boTruyen", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTiet_BoTruyen> ChiTiet_BoTruyens = new HashSet<>();

}
