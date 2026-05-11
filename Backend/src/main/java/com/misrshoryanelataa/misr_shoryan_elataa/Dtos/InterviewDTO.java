package com.misrshoryanelataa.misr_shoryan_elataa.Dtos;

import java.util.List;

public class InterviewDTO {

    private int id;
    private String name;
    private List<SlotDTO> slots;

    public InterviewDTO() {}

    public InterviewDTO(int id, String name, List<SlotDTO> slots) {
        this.id = id;
        this.name = name;
        this.slots = slots;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SlotDTO> getSlots() {
        return slots;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlots(List<SlotDTO> slots) {
        this.slots = slots;
    }
}