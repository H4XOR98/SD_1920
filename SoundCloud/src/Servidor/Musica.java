package Servidor;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Musica {
    private int id;
    private String titulo;
    private String interprete;
    private String autor;
    private int ano;
    private List<String> etiquetas;
    private String path;
    private String formato;
    private int numDownloads;
    private ReentrantLock lockMusica;

    private final String pathDest = FileSystems.getDefault().getPath("").toAbsolutePath().toString() + "/Ficheiros/";


    public Musica(){
        this.id = -1;
        this.titulo = "n/a";
        this.interprete = "n/a";;
        this.autor = "n/a";
        this.ano = 0;
        this.etiquetas = new ArrayList<>();
        this.path = "n/a";
        this.formato = "n/a";
        this.numDownloads = 0;
        this.lockMusica = new ReentrantLock(true);
    }

    public Musica(int id, String titulo, String interprete, String autor, int ano, byte[] bytesFicheiro, List<String> etiquetas, String formato) {
        this.id = id;
        this.titulo = titulo;
        this.interprete = interprete;
        this.autor = autor;
        this.ano = ano;
        this.setEtiquetas(etiquetas);
        this.path = this.pathDest + titulo + "_" + interprete + "." + formato;
        this.formato = formato;
        this.uploadFicheiro(bytesFicheiro);
        this.numDownloads = 0;
        this.lockMusica = new ReentrantLock(true);
    }

    public Musica(Musica musica){
        this.id = musica.getId();
        this.titulo = musica.getTitulo();
        this.interprete = musica.getInterprete();
        this.autor = musica.getAutor();
        this.ano = musica.getAno();
        this.etiquetas = musica.getEtiquetas();
        this.path = musica.getPath();
        this.formato = musica.getFormato();
        this.numDownloads = musica.getNumDownloads();
        this.lockMusica = new ReentrantLock(true);
    }

    public int getId() {
        return this.id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getInterprete() {
        return this.interprete;
    }

    public String getAutor(){
        return this.autor;
    }

    public int getAno() {
        return this.ano;
    }

    public List<String> getEtiquetas() {
        return this.etiquetas;
    }

    public String getPath() {
        return this.path;
    }

    public String getFormato(){
        return this.formato;
    }

    private int getNumDownloads() {
        return this.numDownloads;
    }


    private void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = new ArrayList<>();
        for(String etiqueta : etiquetas){
            this.etiquetas.add(etiqueta);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Musica musica = (Musica) o;

        return id == musica.getId();
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) this.id;
        hash = 31 * hash + (this.titulo == null ? 0 : this.titulo.hashCode());
        hash = 31 * hash + (this.interprete == null ? 0 : this.interprete.hashCode());
        hash = 31 * hash + (this.autor == null ? 0 : this.autor.hashCode());
        hash = 31 * hash + (int) this.ano;
        hash = 31 * hash + (this.etiquetas == null ? 0 : this.etiquetas.hashCode());
        hash = 31 * hash + (this.path == null ? 0 : this.path.hashCode());
        hash = 31 * hash + (this.formato == null ? 0 : this.formato.hashCode());
        hash = 31 * hash + (int) this.numDownloads;
        hash = 31 * hash + (this.lockMusica == null ? 0 : this.lockMusica.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---- Música ----;");
        sb.append("Id : " + this.id + ";");
        sb.append("Titulo : " + this.titulo + ";");
        sb.append("Intérprete : " + this.interprete + ";");
        sb.append("Ano : " + this.ano + ";");
        sb.append("Etiquetas: ");
        for(String etiqueta : this.etiquetas){
            sb.append("\t-" + etiqueta + "");
        }
        sb.append(";Nº de Downloads: " + this.numDownloads);
        return sb.toString();
    }

   public Musica clone(){
        return new Musica(this);
   }

   private void uploadFicheiro(byte[] bytesFicheiro){
        try (OutputStream fos = new FileOutputStream(this.path)) {
            if(bytesFicheiro != null) {
                fos.write(bytesFicheiro);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

   public void efetuarDownload(){
        this.numDownloads++;
   }

   public void lock(){
        this.lockMusica.lock();
   }

   public void unlock(){
        this.lockMusica.unlock();
   }
}
