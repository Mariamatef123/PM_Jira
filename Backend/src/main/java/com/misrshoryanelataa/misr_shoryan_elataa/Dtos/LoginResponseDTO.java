package com.misrshoryanelataa.misr_shoryan_elataa.Dtos;

import com.misrshoryanelataa.misr_shoryan_elataa.Enums.Role;

public class LoginResponseDTO {

    int id;
    String name;
    String email;
    Role role;
        String phone;

         public String getPhone() {
            return phone;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public String getEmail() {
            return email;
        }
        public int getId() {
            return id;
        }


        public Role getRole() {
            return role;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
    public LoginResponseDTO() {
    }

public void setRole(Role role) {
    this.role = role;
}
}