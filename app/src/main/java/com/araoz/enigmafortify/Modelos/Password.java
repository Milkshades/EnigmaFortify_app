package com.araoz.enigmafortify.Modelos;

public class Password {
    private String id;
    private String titulo;
    private String usuario;
    private String password;
    private String sitioWeb;
    private String nota;
    private String userId;

    // Constructor vacío requerido por Firestore
    public Password() {}

    // Constructor con todos los campos
    public Password(String id, String titulo, String usuario, String password, String sitioWeb, String nota, String userId) {
        this.id = id;
        this.titulo = titulo;
        this.usuario = usuario;
        this.password = password;
        this.sitioWeb = sitioWeb;
        this.nota = nota;
        this.userId = userId;

    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
