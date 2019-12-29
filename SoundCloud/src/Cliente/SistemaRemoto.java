package Cliente;

import Cliente.SistemaInterface;
import Exceptions.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SistemaRemoto implements SistemaInterface {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public SistemaRemoto() throws IOException {
        this.socket = new Socket("127.0.0.1", 12345);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
    }


    @Override
    public int criarConta(String nome, String password) throws IOException, UtilizadorJaExisteException {
        out.println("criar_conta " + nome + " " + password);
        out.flush();
        String resultado = in.readLine();
        if(resultado.equals("UtilizadorJaExisteException")){
            throw new UtilizadorJaExisteException("O nome '"+ nome + "' já está associado a uma conta!");
        }
        System.out.println(resultado);
        return Integer.parseInt(resultado);
    }

    @Override
    public String loginUtilizador(String nome, String password) throws UtilizadorInexistenteException, PasswordIncorretaException, IOException {
        out.println("login " + nome + " " + password);
        out.flush();
        String resultado = in.readLine();
        if(resultado.equals("UtilizadorInexistenteException")){
            throw new UtilizadorInexistenteException("Nome não existe no sistema!");
        }else if(resultado.equals("PasswordIncorretaException")){
            throw new PasswordIncorretaException("A password inserida está incorreta!");
        }
        return resultado;
    }

    @Override
    public void uploadMusica(String titulo, String interprete, int ano, List<String> etiquetas, byte[] bytesFicheiro, String formato) throws FormatoInvalidoException, IOException {
        String etiqueta = "";
        for (String e : etiquetas){
            etiqueta += e + "_";
        }
        String bytes = Base64.getEncoder().encodeToString(bytesFicheiro);
        out.println("upload " + titulo + " " + interprete + " " + ano + " " + etiqueta + " " + bytes + " " + formato);
        out.flush();
        String resultado = in.readLine();
        if(resultado.equals("FormatoInvalidoException")){
            throw new FormatoInvalidoException("O formato do ficheiro indicado é invalido!");
        }
    }

    @Override
    public List<String> procurarMusica(String etiqueta) throws EtiquetaInexistenteException, IOException {
        out.println("procura " + etiqueta);
        out.flush();
        String s = in.readLine();
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
    public void downloadMusica(int idMusica, String pathDestino) throws MusicaInexistenteException, IOException {
        out.println("download " + idMusica + " " + pathDestino);
        out.flush();
        String s = in.readLine();
        String[] resultado = s.split(";");
        if(resultado.length == 2){
            String path = resultado[0];
            byte[] bytesFicheiro = Base64.getDecoder().decode(resultado[1]);
            try (OutputStream fos = new FileOutputStream(path)) {
                if(bytesFicheiro != null) {
                    fos.write(bytesFicheiro);
                }
            } catch (IOException ioe) {
                throw new IOException("ERRO! Impossível realizar download!");
            }
        }else{
            if(resultado[0].equals("MusicaInexistenteException")) {
                throw new MusicaInexistenteException("Não existe nenhuma música com o id selecionado");
            }else if (resultado[0].equals("InterruptedException")){

            }else{
                throw new IOException("ERRO! Impossível realizar download!");
            }
        }
    }


    public void logoutUtilizador() throws IOException {
        out.println("quit");
        this.socket.shutdownOutput();
        this.socket.shutdownInput();
        this.socket.close();
    }
}
