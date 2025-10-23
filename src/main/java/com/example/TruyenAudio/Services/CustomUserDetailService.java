package com.example.TruyenAudio.Services;

import com.example.TruyenAudio.Entities.CustomUserDetail;
import com.example.TruyenAudio.Entities.User;
import com.example.TruyenAudio.Reponsitories.IUserReponsitory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private IUserReponsitory userReponsitory;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userReponsitory.findUserByUsername(username);
        if (user != null) {
            throw  new UsernameNotFoundException(username);
        }
        return new CustomUserDetail(user, userReponsitory);
    }
}
