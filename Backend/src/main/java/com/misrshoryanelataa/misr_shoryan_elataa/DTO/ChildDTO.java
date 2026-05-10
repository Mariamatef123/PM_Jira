package com.misrshoryanelataa.misr_shoryan_elataa.DTO;

public class ChildDTO {
    private int id;
    private String name;
    private int age;
    private String bloodType;
    private int parentNumber;

    public ChildDTO(int id, String name, int age, String bloodType, int parentNumber) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.bloodType = bloodType;
        this.parentNumber = parentNumber;
    }
public ChildDTO(){}
   public int getAge() {
       return age;
   }
   public String getBloodType() {
       return bloodType;
   }
   public int getId() {
       return id;
   }public String getName() {
       return name;
   }public int getParentNumber() {
       return parentNumber;
   }public void setAge(int age) {
       this.age = age;
   }public void setBloodType(String bloodType) {
       this.bloodType = bloodType;
   }public void setId(int id) {
       this.id = id;
   }public void setName(String name) {
       this.name = name;
   }public void setParentNumber(int parentNumber) {
       this.parentNumber = parentNumber;
   }
}