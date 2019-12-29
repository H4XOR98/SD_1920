import Exceptions.EtiquetaInexistenteException;
import Exceptions.MusicaInexistenteException;
import Servidor.Sistema;

import java.io.IOException;

public class Teste implements Runnable {
    private Sistema sistema;

    Teste(Sistema sistema){
        this.sistema = sistema;
    }

    @Override
    public void run() {
        for(int i = 0; i <100 ; i ++){
            try {
                sistema.downloadMusica(1,"/Users/lazaropinheiro/Desktop/Teste/");
                sistema.procurarMusica("EDM");
            } catch (MusicaInexistenteException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("Threads Candido");
            } catch (EtiquetaInexistenteException e) {
                e.printStackTrace();
            }
        }
    }
}
