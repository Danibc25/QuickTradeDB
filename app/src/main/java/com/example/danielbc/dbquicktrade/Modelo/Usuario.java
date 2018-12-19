package com.example.danielbc.dbquicktrade.Modelo;

public class Usuario {
    String userName;
    String nombre;
    String apellido;
    String direccion;
    String userId;

    public Usuario() {

    }

    public Usuario(String userName, String nombre, String apellido, String direccion, String userId) {
        this.userName = userName;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.userId = userId;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {

        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {

        this.direccion = direccion;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
