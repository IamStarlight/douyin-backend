package com.bjtu.movie.constants;

public enum Role {
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN", "超级管理员"),

    ROLE_ADMIN("ROLE_ADMIN", "管理员"),

    ROLE_USER("ROLE_USER", "普通用户");


    private final String per;
    private final String desc;

    Role(String per, String desc){
        this.per = per;
        this.desc = desc;
    }

    @Override
    public String toString(){
        return desc;
    }

    public String getValue() {
        return per;
    }
}
