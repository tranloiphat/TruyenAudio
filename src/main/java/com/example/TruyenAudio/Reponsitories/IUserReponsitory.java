package com.example.TruyenAudio.Reponsitories;

import com.example.TruyenAudio.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserReponsitory extends JpaRepository<User, Integer> {
}
