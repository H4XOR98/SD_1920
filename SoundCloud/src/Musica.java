import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

public class Musica {
    private int id;
    private String titulo;
    private String interprete;
    private int ano;
    private List<String> etiquetas;
    private String path;
    private int numDownloads;

    private final String pathDest = FileSystems.getDefault().getPath("").toAbsolutePath().toString() + "/Ficheiros/";


    public Musica(){
        this.id = -1;
        this.titulo = "n/a";
        this.interprete = "n/a";;
        this.ano = 0;
        this.etiquetas = new ArrayList<>();
        this.path = "n/a";
        this.numDownloads = 0;
    }

    public Musica(int id, String titulo, String interprete, int ano, List<String> etiquetas, byte[] bytesFicheiro, String formato) {
        this.id = id;
        this.titulo = titulo;
        this.interprete = interprete;
        this.ano = ano;
        this.setEtiquetas(etiquetas);
        this.path = this.pathDest + titulo + "_" + interprete + "." + formato;
        this.uploadFicheiro(bytesFicheiro);
        this.numDownloads = 0;
    }

    public Musica(Musica musica){
        this.id = musica.getId();
        this.titulo = musica.getTitulo();
        this.interprete = musica.getInterprete();
        this.ano = musica.getAno();
        this.etiquetas = musica.getEtiquetas();
        this.path = musica.getPath();
        this.numDownloads = musica.getNumDownloads();
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getInterprete() {
        return interprete;
    }

    public int getAno() {
        return ano;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public String getPath() {
        return this.getPath();
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
        hash = 31 * hash + (int) id;
        hash = 31 * hash + (this.titulo == null ? 0 : this.titulo.hashCode());
        hash = 31 * hash + (this.interprete == null ? 0 : this.interprete.hashCode());
        hash = 31 * hash + (int) ano;
        hash = this.etiquetas.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---- Música ----\n");
        sb.append("Id : " + this.id + "; \n");
        sb.append("Titulo : " + this.titulo + "; \n");
        sb.append("Intérprete : " + this.interprete + "; \n");
        sb.append("Ano : " + this.ano + "; \n");
        sb.append("Etiquetas: \n");
        for(String etiqueta : this.etiquetas){
            sb.append("\t-" + etiqueta);
        }
        sb.append(this.path);
        return sb.toString();
    }

   public Musica clone(){
        return new Musica(this);
   }

   private void uploadFicheiro(byte[] bytesFicheiro){
        try (OutputStream fos = new FileOutputStream(this.path)) {
            fos.write(bytesFicheiro);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

   public void efetuarDownload(){
        this.numDownloads++;
   }
}
