package Servidor;

import Exceptions.*;

import java.io.IOException;
import java.io.PrintWriter;
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

    private Map<String, PrintWriter> utilizadoresOnline;
    private Map<String, Utilizador> utilizadores;//nome,Servidor.Utilizador
    private Map<Integer, Musica> musicas;//idMusica, musica
    private Map<String,List<Integer>> etiquetas;//etiqueta, lista de ids Servidor.Musica
    private GestorNotificacoes gestorNotificacoes;

    private ReentrantLock lockMusicas;
    private ReentrantLock lockUtilizadores;
    private Condition esperaDownload;

    private static final int MAXDOWN = 3;

    public Sistema(GestorNotificacoes gestorNotificacoes){
        this.idUtilizador = 0;
        this.idMusica = 0;
        this.numDownloads = 0;
        this.utilizadoresOnline = new HashMap<>();
        this.utilizadores = new HashMap<>();
        this.musicas = new HashMap<>();
        this.etiquetas = new HashMap<>();

        this.gestorNotificacoes = gestorNotificacoes;

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

    public String loginUtilizador(String nome, String password, PrintWriter out) throws UtilizadorInexistenteException, PasswordIncorretaException{
        this.lockUtilizadores.lock();
        if(!this.utilizadores.containsKey(nome)){
            this.lockUtilizadores.unlock();
            throw new UtilizadorInexistenteException("UtilizadorInexistenteException");
        }

        Utilizador utilizador = this.utilizadores.get(nome);
        if(!utilizador.comparaPassword(password)){
            this.lockUtilizadores.unlock();
            throw new PasswordIncorretaException("PasswordIncorretaException");
        }
        this.utilizadoresOnline.put(nome,out);
        this.lockUtilizadores.unlock();
        return nome;
    }


    public void uploadMusica(String nome, String titulo, String interprete,String autor, int ano, List<String> etiquetas, byte[] bytesFicheiro, String formato) throws FormatoInvalidoException {
        this.lockMusicas.lock();
        if (!FormatosMusicaEnum.validaFormato(formato)) {
            this.lockMusicas.unlock();
            throw new FormatoInvalidoException("FormatoInvalidoException");
        }
        Musica musica = new Musica(this.idMusica++, titulo, interprete, autor, ano, bytesFicheiro, etiquetas, formato);
        int id = musica.getId();
        musicas.put(id, musica);
        for (String etiqueta : etiquetas) {
            if (!this.etiquetas.containsKey(etiqueta)) {
                this.etiquetas.put(etiqueta, new ArrayList<>());
            }
            this.etiquetas.get(etiqueta).add(id);
        }
        Notificacao notificacao = new Notificacao(titulo,autor);
        this.gestorNotificacoes.adicionaNotificacao(nome,notificacao);
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
            resultado.add(musica.toString());
        }
        this.lockMusicas.unlock();
        return resultado;
    }


    public String downloadMusica(int idMusica, String pathDestino) throws MusicaInexistenteException, IOException, InterruptedException {
        this.lockMusicas.lock();
        while(this.numDownloads == MAXDOWN){
            this.esperaDownload.await();
        }
        this.numDownloads++;

        if(!this.musicas.containsKey(idMusica)){
            this.lockMusicas.unlock();
            throw new MusicaInexistenteException("MusicaInexistenteException");
        }
        Musica musica = this.musicas.get(idMusica);
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
        resultado += ";" + conteudo;;
        this.lockMusicas.lock();
        if(this.numDownloads > 0) {
            this.numDownloads--;
        }
        this.esperaDownload.signal();
        this.lockMusicas.unlock();
        return resultado;
    }

    public void logoutUtilizador(String nome){
        this.lockUtilizadores.lock();
        if(this.utilizadoresOnline.containsKey(nome)){
            this.utilizadoresOnline.remove(nome);
        }
        this.lockUtilizadores.unlock();
    }


    public List<UtilizadorOnline> getUtilizadoresOnline(){
        UtilizadorOnline utilizadorOnline;
        List<UtilizadorOnline> utilizadores = new ArrayList<>();
        this.lockUtilizadores.lock();
        for(String nome : this.utilizadoresOnline.keySet()){
            utilizadorOnline = new UtilizadorOnline(nome,this.utilizadoresOnline.get(nome));
            utilizadores.add(utilizadorOnline);
        }
        this.lockUtilizadores.unlock();
        return utilizadores;
    }
}