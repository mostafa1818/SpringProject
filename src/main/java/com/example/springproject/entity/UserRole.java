package com.example.springproject.entity;

import lombok.ToString;


public enum  UserRole {

    ROLE_USER("USER"),ROLE_ADMIN("ADMIN"),ROLE_RESPONSIBLE("RESPONSIBLE");

    String role=null;

    UserRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
