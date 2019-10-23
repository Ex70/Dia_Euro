package com.example.edgar.diaeuro;

public class Post {
    private String Lugar,Telefono,Nombre,Correo,Celular;

    public Post() {
    }

    public Post(String lugar, String telefono, String celular, String nombre, String correo) {
        this.Lugar = lugar;
        this.Telefono = telefono;
        this.Nombre = nombre;
        this.Correo = correo;
        this.Celular = celular;
    }

    public String getLugar() {
        return Lugar;
    }

    public void setLugar(String lugar) {
        Lugar = lugar;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }
}
