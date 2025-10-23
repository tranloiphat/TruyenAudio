package com.example.TruyenAudio.Reponsitories;

import com.example.TruyenAudio.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserReponsitory extends JpaRepository<User, Integer> {
    @Query ("""
select u from User u where u.username = ?1""")

    User user(String username);

    User findUserByUsername(String username);
}
