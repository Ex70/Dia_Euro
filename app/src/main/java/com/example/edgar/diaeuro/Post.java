package com.example.edgar.diaeuro;

public class Post {
    private String Lugar,Telefono,Nombre,Correo,Celular,Escuela,Carrera,Carrera2,Beca,Sexo;

    public Post() {
    }

    public Post(String lugar, String telefono, String celular, String nombre, String correo, String escuela, String carrera, String carrera2, String beca, String sexo) {
        this.Lugar = lugar;
        this.Telefono = telefono;
        this.Nombre = nombre;
        this.Correo = correo;
        this.Celular = celular;
        this.Escuela = escuela;
        this.Carrera = carrera;
        this.Carrera2 = carrera2;
        this.Beca = beca;
        this.Sexo = sexo;
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

    public String getEscuela() {
        return Escuela;
    }

    public void setEscuela(String escuela) {
        Escuela = escuela;
    }

    public String getCarrera() {
        return Carrera;
    }

    public void setCarrera(String carrera) {
        Carrera = carrera;
    }

    public String getCarrera2() {
        return Carrera2;
    }

    public void setCarrera2(String carrera2) {
        Carrera2 = carrera2;
    }

    public String getBeca() {
        return Beca;
    }

    public void setBeca(String beca) {
        Beca = beca;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }
}
