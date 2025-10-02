package com.example.demo.Services;

import com.example.demo.Entities.BoTruyen;
import com.example.demo.Entities.User;
import com.example.demo.Reponsitories.IUserReponsitory;
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
}
