package com.misrshoryanelataa.misr_shoryan_elataa.Dtos;

import com.misrshoryanelataa.misr_shoryan_elataa.Enums.InterviewStatus;

public class SlotDTO {
    private int id;

    private String date;
    private String time;
    private InterviewStatus status;

    public SlotDTO() {}

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
    public String getTime() {
        return time;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTime(String time) {
        this.time = time;   
    }
    public InterviewStatus getStatus() {
        return status;
    }
    public void setStatus(InterviewStatus status) {
        this.status = status;
    }
}