package com.misrshoryanelataa.misr_shoryan_elataa.Dtos;
public class StaffDTO {

    private int id;
    private String name;
    private String email;
    private String role;
    private String phone;
    private String officialEmail;
    


    public StaffDTO(int id, String name, String email, String role, String phone, String officialEmail ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.officialEmail = officialEmail;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
    public String getPhone() {
        return phone;
    }   
    public String getOfficialEmail() {
        return officialEmail;
    }
}