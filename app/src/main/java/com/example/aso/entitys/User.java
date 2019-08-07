package com.example.aso.entitys;

public class User {
    private Integer id;
    private String user, password, ced;

    public User(String user, String password, String ced) {
        this.user = user;
        this.password = password;
        this.ced = ced;
    }
    public User(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCed(){
        return ced;
    }

    public void setCed(String ced){
        this.ced = ced;
    }
}
