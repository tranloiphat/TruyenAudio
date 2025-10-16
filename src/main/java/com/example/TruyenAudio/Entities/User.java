package com.example.TruyenAudio.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "User")
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer UserId;

    @Column(name = "HoTen")
    private String HoTen;

    @Column(name = "Email")
    private String Email;

    @Column(name = "Sdt")
    private String Sdt;

    @Column(name = "CapDo")
    private Integer CapDo;




}
