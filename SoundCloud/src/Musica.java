import java.util.ArrayList;
import java.util.List;

public class Musica {
    private int id;
    private String titulo;
    private String interprete;
    private int ano;
    private List<String> etiquetas;
    private int numDownloads;

    private static int identificador = 0;

    public Musica(){
        this.id = identificador++;
        this.titulo = "n/a";
        this.interprete = "n/a";;
        this.ano = 0;
        this.etiquetas = new ArrayList<>();
        this.numDownloads = 0;
    }

    public Musica(String titulo, String interprete, int ano,  List<String> etiquetas){
        this.id = identificador++;
        this.titulo = titulo;
        this.interprete = interprete;
        this.ano = ano;
        this.etiquetas = new ArrayList<>();
        for(String etiqueta : etiquetas){
            this.etiquetas.add(etiqueta);
        }
        this.numDownloads = 0;
    }

    public Musica(Musica musica){
        this.id = musica.getId();
        this.titulo = musica.getTitulo();
        this.interprete = musica.getInterprete();
        this.ano = musica.getAno();
        this.etiquetas = musica.getEtiquetas();
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

    private int getNumDownloads() {
        return this.numDownloads;
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
        return sb.toString();
    }

   public Musica clone(Musica musica){
        return new Musica(musica);
   }


   public void efetuarDownload(){
        this.numDownloads++;
   }
}
