package com.misrshoryanelataa.misr_shoryan_elataa.Dtos;

public class LoginDto {
    private String officialEmail;
    private String password;
    public String getOfficialEmail() { return officialEmail; }
    public void setOfficialEmail(String officialEmail) { this.officialEmail = officialEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public LoginDto() {
   
    }
}