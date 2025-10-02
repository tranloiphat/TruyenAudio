package com.example.demo.Reponsitories;

import com.example.demo.Entities.BoTruyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBoTruyenReponsitory extends JpaRepository<BoTruyen, Integer> {
}
