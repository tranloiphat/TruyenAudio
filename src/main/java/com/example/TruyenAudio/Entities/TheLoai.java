package com.example.TruyenAudio.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"BoTruyens"})
@Entity
@Table(name = "TheLoai")
public class TheLoai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TheLoaiId")
    @EqualsAndHashCode.Include              // chỉ ID tham gia equals/hashCode
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

    @ManyToMany(mappedBy = "theLoais")     // bên bị chi phối
    private Set<BoTruyen> BoTruyens = new HashSet<>();
}
