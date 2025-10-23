package com.example.TruyenAudio.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity(name = "User")
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer UserId;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 50, message = "Tên đăng nhập phải ít hơn 50 ký tự")
    private String username;

    @Column(name = "password", length = 50, nullable = false)
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @Column(name = "HoTen")
    private String HoTen;

    @Column(name = "Email")
    private String Email;

    @Column(name = "Sdt")
    private String Sdt;

    @Column(name = "CapDo")
    private Integer CapDo;

    @OneToMany (mappedBy = "user",  cascade = CascadeType.ALL)
    private List<BoTruyen>  boTruyens;







}
