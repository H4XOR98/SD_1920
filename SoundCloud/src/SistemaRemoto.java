
import Exceptions.FormatoInvalidoException;
import Exceptions.MusicaInexistenteException;
import Exceptions.PasswordIncorretaException;
import Exceptions.UtilizadorInexistenteException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SistemaRemoto implements SistemaInterface{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public SistemaRemoto() throws IOException {
        this.socket = new Socket("127.0.0.1", 12345);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
    }


    @Override
    public int criarConta(String nome, String password, String pathDownload) throws IOException {
        out.println("criar_conta " + nome + " " + password + " " + pathDownload);
        out.flush();
        return Integer.parseInt(in.readLine());
    }

    @Override
    public void loginUtilizador(String nome, String password) throws UtilizadorInexistenteException, PasswordIncorretaException, IOException {
        out.println("login " + nome + " " + password);
        out.flush();
        System.out.println(in.readLine());
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
        System.out.println(in.readLine());
    }

    @Override
    public List<String> procurarMusica(String etiqueta) throws IOException {
        out.println("procura " + etiqueta);
        out.flush();
        String s = in.readLine();
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
    public void downloadMusica(int idMusica) throws MusicaInexistenteException, IOException {
        out.println("download " + idMusica + " lazaro");
        out.flush();
        String s = in.readLine();
        String[] resultado = s.split(";");
        if(resultado.length == 2){
            String path = resultado[0];
            byte[] bytesFicheiro = Base64.getDecoder().decode(resultado[1]);
            try (OutputStream fos = new FileOutputStream(path)) {
                fos.write(bytesFicheiro);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }else{
            System.out.println(resultado[0]);
        }
    }


}
