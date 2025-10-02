package com.example.demo.Reponsitories;

import com.example.demo.Entities.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ITheLoaiReponsitory extends JpaRepository<TheLoai, Integer> {
    @Query("SELECT t.TheLoaiId FROM TheLoai t WHERE t.TheLoaiId= :id")
    String getListTenTheLoais(@Param("id") Integer id);


}
