package Servidor;

import java.io.PrintWriter;

public class UtilizadorOnline {
    private String nome;
    private PrintWriter out;

    public UtilizadorOnline(String nome, PrintWriter out){
        this.nome = nome;
        this.out = out;
    }

    public String getNome(){
        return this.nome;
    }

    public PrintWriter getPrintWriter(){
        return this.out;
    }
}