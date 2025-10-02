package com.example.demo.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "TheLoai")
public class TheLoai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TheLoaiId")
    private Integer TheLoaiId;

    @Column(name = "image")
    private String image;

    @NotBlank(message = "Tên thể loại không được để trống")
    @Size(max = 100, message = "Tên thể loại tối đa 100 ký tự")
    @Column(name = "TenTheLoai", length = 100, nullable = false)
    private String TenTheLoai;

    @Size(max = 100, message = "Mô tả tối đa 100 ký tự")
    @Column(name = "MoTa", length = 100)
    private String MoTa;

    @ManyToMany
    @JoinTable(
            name = "ChiTiet_TheLoai",
            joinColumns = @JoinColumn(name = "TheLoaiId"),
            inverseJoinColumns = @JoinColumn(name = "BoTruyenId")
    )
    private Set<BoTruyen> BoTruyens = new HashSet<>();

    // Nếu dùng Lombok @Data là đủ, nhưng có thể giữ các getter/setter tuỳ ý
}
