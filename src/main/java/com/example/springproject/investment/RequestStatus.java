package com.example.springproject.investment;

public enum  RequestStatus {
   HAS_BEEN_ANSWERED("ANSWERED"),CLOSED("CLOSED") ,IN_PROGRESS("PROGRESS");

    String role=null;

    RequestStatus(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
