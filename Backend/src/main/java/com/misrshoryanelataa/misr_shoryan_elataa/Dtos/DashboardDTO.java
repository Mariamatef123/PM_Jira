package com.misrshoryanelataa.misr_shoryan_elataa.Dtos;

public class DashboardDTO {

    private int totalVolunteers;
    private int approved;
    private int pending;
    private int rejected;
    private int interview;

    private int staffCount;

    // getters and setters

    public int getTotalVolunteers() {
        return totalVolunteers;
    }

    public void setTotalVolunteers(int totalVolunteers) {
        this.totalVolunteers = totalVolunteers;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getRejected() {
        return rejected;
    }

    public void setRejected(int rejected) {
        this.rejected = rejected;
    }

    public int getInterview() {
        return interview;
    }

    public void setInterview(int interview) {
        this.interview = interview;
    }

    public int getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(int staffCount) {
        this.staffCount = staffCount;
    }
}