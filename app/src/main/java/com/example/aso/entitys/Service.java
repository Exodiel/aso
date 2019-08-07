package com.example.aso.entitys;

public class Service {
    private Integer id, idstudent, num;
    private String typecolor, typeaction;


    public Service(Integer idstudent, Integer num, String typecolor, String typeaction) {
        this.idstudent = idstudent;
        this.num = num;
        this.typecolor = typecolor;
        this.typeaction = typeaction;
    }

    public Service() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdstudent() {
        return idstudent;
    }

    public void setIdstudent(Integer idstudent) {
        this.idstudent = idstudent;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getTypecolor() {
        return typecolor;
    }

    public void setTypecolor(String typecolor) {
        this.typecolor = typecolor;
    }

    public String getTypeaction() {
        return typeaction;
    }

    public void setTypeaction(String typeaction) {
        this.typeaction = typeaction;
    }
}
