package com.misrshoryanelataa.misr_shoryan_elataa.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.misrshoryanelataa.misr_shoryan_elataa.Models.UserEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Repos.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public Object createUser(UserEntity user) {
        try {
            return userRepo.save(user);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Object getUserById(int id) {
        try {
            return userRepo.findById(id).orElse(null);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Object updateUser(int id, UserEntity user) {
        try {
            if (userRepo.existsById(id)) {
                user.setId(id);
                return userRepo.save(user);
            } else {
                return "User not found";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Object deleteUser(int id) {
        try {
            if (userRepo.existsById(id)) {
                userRepo.deleteById(id);
                return "User deleted successfully";
            } else {
                return "User not found";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    

}