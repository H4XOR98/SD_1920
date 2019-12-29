package Servidor;

import Exceptions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Sistema {
    private int idUtilizador;
    private int idMusica;
    private int numDownloads;

    private Map<String, Utilizador> utilizadores;//nome,Servidor.Utilizador
    private Map<Integer, Musica> musicas;//idMusica, musica
    private Map<String,List<Integer>> etiquetas;//etiqueta, lista de ids Servidor.Musica

    private ReentrantLock lockMusicas;
    private ReentrantLock lockUtilizadores;
    private Condition esperaDownload;

    private static final int MAXDOWN = 3;

    public Sistema(){
        this.idUtilizador = 0;
        this.idMusica = 0;
        this.numDownloads = 0;
        this.utilizadores = new HashMap<>();
        this.musicas = new HashMap<>();
        this.etiquetas = new HashMap<>();
        this.lockMusicas = new ReentrantLock(true);
        this.lockUtilizadores = new ReentrantLock(true);
        this.esperaDownload = this.lockMusicas.newCondition();
    }



    public int criarConta(String nome, String password) throws UtilizadorJaExisteException {
        lockUtilizadores.lock();
        if(this.utilizadores.containsKey(nome)){
            throw new UtilizadorJaExisteException("UtilizadorJaExisteException");
        }
        Utilizador utilizador = new Utilizador(idUtilizador,nome, password);
        this.utilizadores.put(nome,utilizador);
        int id = this.idUtilizador++;
        lockUtilizadores.unlock();
        return id;
    }

    public String loginUtilizador(String nome, String password) throws UtilizadorInexistenteException, PasswordIncorretaException{
        this.lockUtilizadores.lock();
        if(!this.utilizadores.containsKey(nome)){
            this.lockUtilizadores.unlock();
            throw new UtilizadorInexistenteException("UtilizadorInexistenteException");
        }
        Utilizador utilizador = this.utilizadores.get(nome);
        utilizador.lock();
        this.lockUtilizadores.unlock();
        if(!utilizador.comparaPassword(password)){
            utilizador.unlock();
            throw new PasswordIncorretaException("PasswordIncorretaException");
        }
        utilizador.unlock();
        return nome;
    }


    public void uploadMusica(String titulo, String interprete, int ano, String[] etiquetas, byte[] bytesFicheiro, String formato) throws FormatoInvalidoException {
        this.lockMusicas.lock();
        if (!FormatosMusicaEnum.validaFormato(formato)) {
            this.lockMusicas.unlock();
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
        this.lockMusicas.unlock();
    }


    public List<String> procurarMusica(String etiqueta) throws EtiquetaInexistenteException{
        List<String> resultado = new ArrayList<>();
        Musica musica;
        this.lockMusicas.lock();
        if(!this.etiquetas.containsKey(etiqueta)){
            this.lockMusicas.unlock();
            throw new EtiquetaInexistenteException("EtiquetaInexistenteException");
        }
        List<Integer> listaIds = this.etiquetas.get(etiqueta);
        for(int id : listaIds){
            musica = this.musicas.get(id);
            musica.lock();
            resultado.add(musica.toString());
            musica.unlock();
        }
        this.lockMusicas.unlock();
        return resultado;
    }


    public String downloadMusica(int idMusica, String pathDestino) throws MusicaInexistenteException, IOException, InterruptedException {
        this.lockMusicas.lock();
        while(this.numDownloads == MAXDOWN){
            System.out.println(Thread.currentThread().getId());
            this.esperaDownload.await();
        }
        if(!this.musicas.containsKey(idMusica)){
            this.lockMusicas.unlock();
            throw new MusicaInexistenteException("MusicaInexistenteException");
        }
        Musica musica = this.musicas.get(idMusica);
        musica.lock();
        musica.efetuarDownload();
        this.lockMusicas.unlock();

        String titulo = musica.getTitulo();
        String interprete = musica.getInterprete();
        String formato = musica.getFormato();

        String resultado = pathDestino + titulo + "_" +  interprete + "." + formato;

        String p = musica.getPath();
        Path path = Paths.get(p);
        byte[] bytes = Files.readAllBytes(path);
        String conteudo = Base64.getEncoder().encodeToString(bytes);
        resultado += ";" + conteudo;
        musica.unlock();
        return resultado;
    }
}