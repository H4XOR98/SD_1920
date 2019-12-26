import Exceptions.*;

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
        this.idUtilizador++;
        lockSistema.unlock();
        return utilizador.getId();
    }

    public void loginUtilizador(String nome, String password) throws UtilizadorInexistenteException, PasswordIncorretaException{
        lockSistema.lock();
        if(!this.utilizadores.containsKey(nome)){
            throw new UtilizadorInexistenteException("Nome não existe no Sistema!");
        }
        Utilizador utilizador = this.utilizadores.get(nome).clone();
        lockSistema.unlock();
        if(!utilizador.comparaPassword(password)){
            throw new PasswordIncorretaException("A password inserida está incorreta");
        }
    }


    public void uploadMusica(String titulo, String interprete, int ano, String[] etiquetas, byte[] bytesFicheiro, String formato) throws FormatoInvalidoException {
        if(!FormatosMusicaEnum.validaFormato(formato)){
            throw new FormatoInvalidoException("O formato do ficheiro selecionado não é válido!");
        }
        Musica musica = new Musica(this.idUtilizador++,titulo,interprete,ano, Arrays.asList(etiquetas),bytesFicheiro,formato);
        int id = musica.getId();
        musicas.put(id ,musica);
        for(String etiqueta : etiquetas){
            if(!this.etiquetas.containsKey(etiqueta)){
                this.etiquetas.put(etiqueta,new ArrayList<>());
            }
            this.etiquetas.get(etiqueta).add(id);
        }
    }


    /*public List<Musica> procurarMusica(String etiqueta){
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