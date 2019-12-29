import Exceptions.PasswordIncorretaException;
import Exceptions.UtilizadorInexistenteException;
import Servidor.Sistema;

import Exceptions.EtiquetaInexistenteException;
import Exceptions.MusicaInexistenteException;

import java.io.IOException;
import java.net.Socket;

public class Teste implements Runnable {
    private Sistema sistema;
    private Socket socket;

    Teste(Sistema sistema,Socket socket){
        this.sistema = sistema;
        this.socket = socket;
    }

    @Override
    public void run() {
        for(int i = 0; i <100 ; i ++){
            try {
                /*sistema.downloadMusica(1,"/Users/lazaropinheiro/Desktop/Teste/");
                sistema.procurarMusica("EDM");*/
                sistema.loginUtilizador("lazaro","123",this.socket);
                sistema.logoutUtilizador("lazaro");
            } /*catch (MusicaInexistenteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Threads Candido");
            } catch (EtiquetaInexistenteException e) {
                e.printStackTrace();
            } */catch (UtilizadorInexistenteException e) {
                e.printStackTrace();
            } catch (PasswordIncorretaException e) {
                e.printStackTrace();
            }
        }
    }
}