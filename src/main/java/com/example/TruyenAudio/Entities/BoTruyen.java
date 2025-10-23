package com.example.TruyenAudio.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"theLoais","chapters"})
@Entity
@Table(name= "BoTruyen")
public class BoTruyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BoTruyenId")
    @EqualsAndHashCode.Include
    private Integer BoTruyenId;

    @Column(name = "TenTruyen")
    private String TenTruyen;

    @Column(name = "Image")
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
    @JoinTable(
            name = "ChiTiet_TheLoai",
            joinColumns = @JoinColumn(name = "BoTruyenId"),
            inverseJoinColumns = @JoinColumn(name = "TheLoaiId")
    )
    private Set<TheLoai> theLoais = new HashSet<>();

    @OneToMany(mappedBy = "BoTruyen", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chapter> chapters;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

}
