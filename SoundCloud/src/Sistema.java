import Exceptions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Sistema {
    private int idUtilizador;
    private int idMusica;
    private Map<String,Utilizador> utilizadores;//nome,Utilizador
    private Map<Integer,Musica> musicas;//idMusica, musica
    private Map<String,List<Integer>> etiquetas;//etiqueta, lista de ids Musica
    private ReentrantLock lockSistema;

    public Sistema(){
        this.idUtilizador = 0;
        this.idMusica = 0;
        this.utilizadores = new HashMap<>();
        this.musicas = new HashMap<>();
        this.etiquetas = new HashMap<>();
        this.lockSistema = new ReentrantLock(true);
    }



    public int criarConta(String nome, String password, String pathDownload) {
        lockSistema.lock();
        Utilizador utilizador = new Utilizador(idUtilizador,nome, password,pathDownload);
        this.utilizadores.put(nome,utilizador);
        int id = this.idUtilizador++;
        lockSistema.unlock();
        return id;
    }

    public String loginUtilizador(String nome, String password) throws UtilizadorInexistenteException, PasswordIncorretaException{
        this.lockSistema.lock();
        if(!this.utilizadores.containsKey(nome)){
            throw new UtilizadorInexistenteException("UtilizadorInexistenteException");
        }
        Utilizador utilizador = this.utilizadores.get(nome).clone();
        if(!utilizador.comparaPassword(password)){
            throw new PasswordIncorretaException("A password inserida está incorreta!");
        }
        this.lockSistema.unlock();
        return nome;
    }


    public void uploadMusica(String titulo, String interprete, int ano, String[] etiquetas, byte[] bytesFicheiro, String formato) throws FormatoInvalidoException {
        this.lockSistema.lock();
        if (!FormatosMusicaEnum.validaFormato(formato)) {
            throw new FormatoInvalidoException("FormatoInvalidoException");
        }
        Musica musica = new Musica(this.idMusica++, titulo, interprete, ano, bytesFicheiro, Arrays.asList(etiquetas), formato);
        int id = musica.getId();
        musicas.put(id, musica);
        for (String etiqueta : etiquetas) {
            if (!this.etiquetas.containsKey(etiqueta)) {
                this.etiquetas.put(etiqueta, new ArrayList<>());
            }
            this.etiquetas.get(etiqueta).add(id);
        }
        this.lockSistema.unlock();
    }


    public List<String> procurarMusica(String etiqueta) throws EtiquetaInexistenteException{
        List<String> resultado = new ArrayList<>();
        Musica musica;
        this.lockSistema.lock();
        if(!this.etiquetas.containsKey(etiqueta)){
            throw new EtiquetaInexistenteException("Etiqueta não existe no sistema!");
        }
        List<Integer> listaIds = this.etiquetas.get(etiqueta);
        for(int id : listaIds){
            musica = this.musicas.get(id);
            resultado.add(musica.toString());
        }
        this.lockSistema.unlock();
        return resultado;
    }


    public String downloadMusica(int idMusica, String nome) throws MusicaInexistenteException, IOException {
        this.lockSistema.lock();
        if(!this.musicas.containsKey(idMusica)){
            throw new MusicaInexistenteException("Não existe nenhuma música com o id selecionado");
        }
        this.musicas.get(idMusica).efetuarDownload();
        Musica musica = this.musicas.get(idMusica);

        String pathDestino = this.utilizadores.get(nome).getPathDownload();
        String titulo = musica.getTitulo();
        String interprete = musica.getInterprete();
        String formato = musica.getFormato();

        String resultado = pathDestino + titulo + "_" +  interprete + "." + formato;

        String p = musica.getPath();
        Path path = Paths.get(p);
        byte[] bytes = Files.readAllBytes(path);
        String conteudo = Base64.getEncoder().encodeToString(bytes);
        resultado += ";" + conteudo;
        this.lockSistema.unlock();
        return resultado;
    }


}