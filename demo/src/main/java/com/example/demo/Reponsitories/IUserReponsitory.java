package com.example.demo.Reponsitories;

import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserReponsitory extends JpaRepository<User, Integer> {
}
