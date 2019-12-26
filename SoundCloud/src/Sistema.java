import Exceptions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sistema {
    private Map<String,Utilizador> utilizadores;//nome,Utilizador
    private Map<Integer,Musica> musicas;//idMusica, musica
    private Map<String,List<Integer>> etiquetas;//etiqueta, lista de ids Musica


    public Sistema(){
        this.utilizadores = new HashMap<>();
        this.musicas = new HashMap<>();
        this.etiquetas = new HashMap<>();
    }

    private String[] parseString(String componentes){
        String[] resultado = componentes.split("_");
        return resultado;
    }

    public int criarConta(String componentes) throws OperacaoInvalidaException {
        String[] partes = this.parseString(componentes);
        if(partes.length != 3){
            throw new OperacaoInvalidaException("Não foram indicados todos os componentes!");
        }
        String nome = partes[0];
        String password = partes[1];
        String pathDownload = partes[2];
        Utilizador utilizador = new Utilizador(nome, password,pathDownload);
        this.utilizadores.put(nome,utilizador);
        return utilizador.getId();
    }


    public void loginUtilizador(String componentes) throws UtilizadorInexistenteException, PasswordIncorretaException, OperacaoInvalidaException {
        String[] partes = this.parseString(componentes);
        if(partes.length != 2){
            throw new OperacaoInvalidaException("Não foram indicados todos os componentes!");
        }
        String email = partes[0];
        String password = partes[1];
        if(!this.utilizadores.containsKey(email)){
            throw new UtilizadorInexistenteException("Email não existe no Sistema!");
        }
        Utilizador utilizador = this.utilizadores.get(email);
        if(!utilizador.comparaPassword(password)){
            throw new PasswordIncorretaException("A password inserida está incorreta");
        }
    }

    /*public void uploadrMusica(String titulo, String interprete, int ano, List<String> etiquetas, String conteudoFicheiro,String formato) throws FormatoInvalidoException {
        if(!FormatosMusicaEnum.validaFormato(formato)){
            throw new FormatoInvalidoException("O formato do ficheiro selecionado não é válido!");
        }
        byte[] bytesFicheiro = conteudoFicheiro.getBytes();
        Musica musica = new Musica(titulo,interprete,ano,etiquetas,bytesFicheiro,formato);
        int id = musica.getId();
        musicas.put(id ,musica);
        for(String etiqueta : etiquetas){
            if(!this.etiquetas.containsKey(etiqueta)){
                this.etiquetas.put(etiqueta,new ArrayList<>());
            }
            this.etiquetas.get(etiqueta).add(id);
        }
    }


    public List<Musica> procurarMusica(String etiqueta){
        List<Musica> resultado = new ArrayList<>();
        Musica m;
        List<Integer> listaIds = this.etiquetas.get(etiqueta);
        for(int id : listaIds){
            m = this.musicas.get(id);
            resultado.add(m);
        }
        return resultado;
    }

    private static void copyFile(String origem, String destino) throws IOException {
        File ficheiroOrigem = new File(origem);
        File ficheiroDestino = new File(destino);

        FileInputStream fileInputStream = new FileInputStream(ficheiroOrigem);
        FileOutputStream fileOutputStream = new FileOutputStream(ficheiroDestino);

        int bufferSize;
        byte[] bufffer = new byte[512];
        while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
            fileOutputStream.write(bufffer, 0, bufferSize);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

    public void downloadMusica(String componentes) throws MusicaInexistenteException, OperacaoInvalidaException {
        String[] partes = parseString(componentes);
        if(partes.length < 1){
            throw new OperacaoInvalidaException("Não foram indicados todos os componentes!");
        }
        int id = Integer.parseInt(partes[0]);
        String path = partes[1];
        if(!this.musicas.containsKey(id)){
            throw new MusicaInexistenteException("Não existe nenhuma música com o id selecionado");
        }
        this.musicas.get(id).efetuarDownload();
        Musica m = this.musicas.get(id).clone();
        String pathFile = m.getPath();
        this.copyFile();
    }*/

}