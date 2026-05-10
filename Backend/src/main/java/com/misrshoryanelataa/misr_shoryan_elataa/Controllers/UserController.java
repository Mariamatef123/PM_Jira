package com.misrshoryanelataa.misr_shoryan_elataa.Controllers;
import org.springframework.web.bind.annotation.RestController;
import com.misrshoryanelataa.misr_shoryan_elataa.Models.UserEntity;
import com.misrshoryanelataa.misr_shoryan_elataa.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
public class UserController {
    @Autowired
    UserService UserService;

    @PostMapping("/users")
    public Object createUser(@RequestBody UserEntity user
    )  {

        return  UserService.createUser(user);
    }

    @GetMapping("/users/{id}")
    public Object getUserById(@RequestParam int id) {
        return UserService.getUserById(id);
    }

    @PutMapping("/users/{id}")
    public Object updateUser(@PathVariable int id, @RequestBody UserEntity user) {
     return UserService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public Object deleteUser(@PathVariable int id) {
        return UserService.deleteUser(id);
    }
    
    
    public UserController() {
       
    }
}