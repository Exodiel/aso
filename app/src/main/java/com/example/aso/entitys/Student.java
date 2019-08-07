package com.example.aso.entitys;

public class Student {
    private Integer id;
    private String ced, names, level, carrer;

    public Student() {
    }

    public Student(Integer id, String names, String level, String carrer) {
        this.id = id;
        this.names = names;
        this.level = level;
        this.carrer = carrer;
    }

    public Student(String ced, String names, String level, String carrer) {
        this.ced = ced;
        this.names = names;
        this.level = level;
        this.carrer = carrer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCarrer() {
        return carrer;
    }

    public void setCarrer(String carrer) {
        this.carrer = carrer;
    }

    public String getCed() {
        return ced;
    }

    public void setCed(String ced) {
        this.ced = ced;
    }
}
