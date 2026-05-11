package com.misrshoryanelataa.misr_shoryan_elataa.DTO;

public class ValidationResponse {
    private boolean eligible;
    private String message;

    public ValidationResponse(boolean eligible, String message) {
        this.eligible = eligible;
        this.message = message;
    }

    public boolean isEligible() { return eligible; }
    public String getMessage() { return message; }
}
