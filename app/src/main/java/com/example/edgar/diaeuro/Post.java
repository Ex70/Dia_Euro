package com.example.edgar.diaeuro;

public class Post {
    private String title,content,nombre,correo;

    public Post() {
    }

    public Post(String title, String content, String nombre, String correo) {
        this.title = title;
        this.content = content;
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
