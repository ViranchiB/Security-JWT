package com.StudySecurity.Entity.enums;

public enum Roles {
    USER, ADMIN;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
