package com.example.TruyenAudio.Services;

import com.example.TruyenAudio.Entities.BoTruyen;
import com.example.TruyenAudio.Entities.User;
import com.example.TruyenAudio.Reponsitories.IUserReponsitory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private  IUserReponsitory userReponsitory;



    public  List<User> getAllUsers(){
        return userReponsitory.findAll();
    }
    public User getbyId(Integer id){
        return userReponsitory.findById(id).orElse(null);
    }
    public void delete(User user){
        userReponsitory.delete(user);
    }
    public void addUser(User user){
        userReponsitory.save(user);
    }
    public void updateUser(User user){
        userReponsitory.save(user);
    }

    public void save(@Valid User user) {
        userReponsitory.save(user);
    }
}
