package com.misrshoryanelataa.misr_shoryan_elataa.DTO;
public class DonorDTO {
    private int id;
    private String name;
    private String bloodType;
    private String donationType;
    private String status;
    private String email;
    private String phone;

    public DonorDTO(int id, String name, String bloodType, String donationType, String status,String email,String phone) {
        this.id = id;
        this.name = name;
        this.bloodType = bloodType;
        this.donationType = donationType;
        this.status = status;
        this.email=email;
        this.phone=phone;
    }
    public DonorDTO(){
        
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
public String getBloodType() {
    return bloodType;
}public String getDonationType() {
    return donationType;
}public int getId() {
    return id;
}public String getName() {
    return name;
}public String getStatus() {
    return status;
}
public void setBloodType(String bloodType) {
    this.bloodType = bloodType;
}public void setDonationType(String donationType) {
    this.donationType = donationType;
}public void setId(int id) {
    this.id = id;
}public void setName(String name) {
    this.name = name;
}public void setStatus(String status) {
    this.status = status;
}
}