package Servidor;

import java.util.Objects;

public class Notificacao {
    private String titulo;
    private String autor;

    public Notificacao(){
        this.titulo = "n/a";
        this.autor = "n/a";
    }

    public Notificacao(String titulo, String interprete){
        this.titulo = titulo;
        this.autor = interprete;
    }

    public Notificacao(Notificacao notificacao){
        this.titulo = notificacao.getTitulo();
        this.autor = notificacao.getAutor();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------- NOTIFICACAO ---------;");
        sb.append("Título: " + this.titulo + ";");
        sb.append("Autor: " + this.autor + ";");
        return sb.toString();
    }
}
