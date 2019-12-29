package Servidor;

import Exceptions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.List;

public class TaskRunner implements Runnable{
    private Socket socket;
    private Sistema sistema;
    private PrintWriter out;
    private BufferedReader in;

    public TaskRunner(Socket socket, Sistema sistema) throws IOException {
        this.socket = socket;
        this.sistema = sistema;
        this.out = new PrintWriter(this.socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String[] op ;
            String line;
            while ((line = in.readLine()) != null && !line.equals("quit")) {
                // dividir por espaços
                op = line.split(" ");

                this.runCommand(op);
                this.out.flush();
            }

            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();
            this.in.close();
            this.out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void runCommand(String[] op) {
        switch (op[0]) {
            case "criar_conta":
                this.criarConta(op[1],op[2]);
                break;
            case "login":
                this.login(op[1], op[2]);
                break;
            case "upload":
                this.upload(op[1], op[2], op[3], op[4], op[5], op[6]);
                break;
            case "procura":
                this.procura(op[1]);
                break;
            case "download":
                this.download(op[1],op[2]);
            default:
                break;
        }
    }

    private void criarConta(String nome, String password){
        try {
            int id = sistema.criarConta(nome, password);
            out.println(id);
            System.out.println("O " + nome + " acabou de criar uma conta com o id " + id);
        } catch (UtilizadorJaExisteException e) {
            out.println(e.getMessage());
        }
    }

    private void login(String nome, String password){
        try {
            out.println(sistema.loginUtilizador(nome, password));
            System.out.println("O " + nome + " acabou de iniciar sessão");
        }catch (UtilizadorInexistenteException e){
            System.out.println("O " +  nome + " não existe no sistema");
            out.println(e.getMessage());
        }catch(PasswordIncorretaException e){
            System.out.println("O " +  nome + " enganou-se na password");
            out.println(e.getMessage());
        }
    }

    private void upload(String titulo, String interprete, String conteudoAno, String conteudoEtiquetas, String conteudoFicheiro, String formato){
        try {
            int ano = Integer.parseInt(conteudoAno);
            String[] etiquetas = conteudoEtiquetas.split("_");
            byte[] bytesFicheiro = Base64.getDecoder().decode(conteudoFicheiro);
            sistema.uploadMusica(titulo,interprete,ano,etiquetas,bytesFicheiro,formato);
            out.println("sucesso");
            System.out.println("Foi adicionada a musica " + titulo + "_" + interprete + "." + formato);
        } catch (FormatoInvalidoException e) {
            System.out.println("O formato da música selecionado não é permitido");
            out.println(e.getMessage());
        }
    }

    private void procura(String etiqueta){
        try {
            List<String> lista = sistema.procurarMusica(etiqueta);
            String resultado = "";
            for (String m : lista) {
                resultado += m + ",";
            }
            out.println(resultado);
            System.out.println("Foram procuradas todas as músicas com a etiqueta " + etiqueta);
        } catch (EtiquetaInexistenteException e) {
            System.out.println("Não existem músicas com a etiqueta " + etiqueta);
            out.println(e.getMessage());
        }
    }

    private void download(String id, String pathDestino) {
        try{
            int idMusica = Integer.parseInt(id);
            out.println(this.sistema.downloadMusica(idMusica,pathDestino));
        } catch (MusicaInexistenteException e) {
            out.println(e.getMessage());
        } catch (IOException e) {
            out.println("IOException");
        }
    }

}
