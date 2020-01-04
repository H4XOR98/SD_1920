package Cliente;

import Exceptions.*;
import View.ViewNotificacao;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class SistemaRemoto implements SistemaInterface {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean operacaoEmCurso = false;
    private String nome;
    private ReentrantLock lockSistemaRemoto = new ReentrantLock(true);
    private ViewNotificacao viewNotificacao = new ViewNotificacao();

    public SistemaRemoto() throws IOException {
        this.socket = new Socket("127.0.0.1", 12345);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
    }


    @Override
    public int criarConta(String nome, String password) throws IOException, UtilizadorJaExisteException {
        this.operacaoEmCurso = true;
        out.println("criar_conta;" + nome + ";" + password);
        out.flush();
        String resultado;
        lockSistemaRemoto.lock();
        do{
            resultado  = in.readLine();
        } while(processaNotificacao(resultado));
        lockSistemaRemoto.unlock();
        this.operacaoEmCurso = false;
        if(resultado.equals("UtilizadorJaExisteException")){
            throw new UtilizadorJaExisteException("O nome '"+ nome + "' já está associado a uma conta!");
        }
        return Integer.parseInt(resultado);
    }

    @Override
    public String loginUtilizador(String nome, String password) throws UtilizadorInexistenteException, PasswordIncorretaException, IOException {
        this.operacaoEmCurso = true;
        out.println("login;" + nome + ";" + password);
        out.flush();
        String resultado;
        lockSistemaRemoto.lock();
        do{
            resultado  = in.readLine();
        } while(processaNotificacao(resultado));
        lockSistemaRemoto.unlock();
        this.operacaoEmCurso = false;
        if(resultado.equals("UtilizadorInexistenteException")){
            throw new UtilizadorInexistenteException("Nome não existe no sistema!");
        }else if(resultado.equals("PasswordIncorretaException")){
            throw new PasswordIncorretaException("A password inserida está incorreta!");
        }
        this.nome = nome;
        return resultado;
    }

    @Override
    public void uploadMusica(String titulo, String interprete, String autor, int ano, List<String> etiquetas, byte[] bytesFicheiro, String formato) throws FormatoInvalidoException, IOException {
        this.operacaoEmCurso = true;
        String etiqueta = "";
        for (String e : etiquetas){
            etiqueta += e + "_";
        }
        String bytes = Base64.getEncoder().encodeToString(bytesFicheiro);
        out.println("upload;" + this.nome + ";" + titulo + ";" + interprete + ";" + autor + ";" + ano + ";" + etiqueta + ";" + bytes + ";" + formato);
        out.flush();
        String resultado;
        lockSistemaRemoto.lock();
        do{
            resultado  = in.readLine();
        } while(processaNotificacao(resultado));
        lockSistemaRemoto.unlock();
        this.operacaoEmCurso = false;
        if(resultado.equals("FormatoInvalidoException")){
            throw new FormatoInvalidoException("O formato do ficheiro indicado é invalido!");
        }
    }

    @Override
    public List<String> procurarMusica(String etiqueta) throws EtiquetaInexistenteException, IOException {
        this.operacaoEmCurso = true;
        out.println("procura;" + etiqueta);
        out.flush();
        String s;
        lockSistemaRemoto.lock();
        do{
            s  = in.readLine();
        } while(processaNotificacao(s));
        lockSistemaRemoto.unlock();
        this.operacaoEmCurso = false;
        if(s.equals("EtiquetaInexistenteException")){
            throw new EtiquetaInexistenteException("Não existe nenhuma música no sistema com a etiqueta " + etiqueta);
        }
        String[] componentes = s.split(",");
        List<String> resultado = new ArrayList<>();
        String musica;
        for(String comp : componentes){
            musica = "";
            for(String c : comp.split(";")){
                musica += c + "\n";
            }
            resultado.add(musica);
        }
        return resultado;
    }

    @Override
    public String downloadMusica(int idMusica, String pathDestino) throws MusicaInexistenteException, IOException {
        this.operacaoEmCurso = true;
        out.println("download;" + idMusica + ";" + pathDestino);
        out.flush();
        String s;
        lockSistemaRemoto.lock();
        do{
            s  = in.readLine();
        } while(processaNotificacao(s));
        lockSistemaRemoto.unlock();
        this.operacaoEmCurso = false;
        String[] resultado = s.split(";");
        String nomeMusica = "";
        if(resultado.length == 2){
            String path = resultado[0];
            Path p = Paths.get(path);
            nomeMusica = p.getFileName().toString();
            byte[] bytesFicheiro = Base64.getDecoder().decode(resultado[1]);
            try (OutputStream fos = new FileOutputStream(path)) {
                if(bytesFicheiro != null) {
                    fos.write(bytesFicheiro);
                }
            } catch (IOException ioe) {
                throw new IOException("Impossível realizar download!");
            }
        }else{
            if(resultado[0].equals("MusicaInexistenteException")) {
                throw new MusicaInexistenteException("Não existe nenhuma música com o id selecionado");
            }else if (resultado[0].equals("InterruptedException")){
                throw new IOException("Impossível realizar download!");
            }else{
                throw new IOException("Impossível realizar download!");
            }
        }
        return nomeMusica;
    }


    public void logoutUtilizador() {
        out.println("logout;" + this.nome);
        out.flush();
    }


    public void sair() throws IOException {
        out.println("quit");
        out.flush();
        this.socket.shutdownOutput();
        this.socket.shutdownInput();
        this.socket.close();
        this.out.close();
        this.in.close();
    }


    /*   NOTIFICACOES   */
    private boolean processaNotificacao(String s){
        String[] list = s.split(";");
        if(list[0].equals("notificacao")){
            String notificacao = "";
            String[] resultado = list[1].split(",");
            for(String n : resultado){
                notificacao += n + "\n";
            }
            viewNotificacao.showNotificacao(notificacao);
            return true;
        }
        return false;
    }

    public void leNotificacao() throws IOException {
        if(this.operacaoEmCurso){
            return;
        }
        this.lockSistemaRemoto.lock();
        if(!this.operacaoEmCurso && in.ready()) {
            String s = in.readLine();
            processaNotificacao(s);
        }
        this.lockSistemaRemoto.unlock();
    }
}