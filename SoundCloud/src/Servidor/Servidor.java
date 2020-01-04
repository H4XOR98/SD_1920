package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(12345);
        GestorNotificacoes gestorNotificacoes = new GestorNotificacoes();
        Sistema sistema = new Sistema(gestorNotificacoes);
        Thread threadEnvioNotificacoes = new Thread(new ThreadEnvioNotificacoes(sistema,gestorNotificacoes));
        threadEnvioNotificacoes.start();
        while(true){
            Socket socket = s.accept();
            new Thread(new TaskRunner(socket, sistema)).start();
        }
    }
}
