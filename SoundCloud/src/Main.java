import Exceptions.FormatoInvalidoException;
import Servidor.Sistema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, FormatoInvalidoException, InterruptedException {
        Sistema s = new Sistema();
        String[] etiquetas = {"Trance","EDM"};
        String p = "/Users/lazaropinheiro/Downloads/kygo.mp3";
        Path path = Paths.get(p);
        byte[] bytes = Files.readAllBytes(path);
        s.uploadMusica("Firestone","Kygo",2016,etiquetas,bytes,"mp3");
        //
        String[] etiquetas1 = {"EDM","Gospel","kuduro"};
        p = "/Users/lazaropinheiro/Downloads/carlos_t.mp3";
        path = Paths.get(p);
        bytes = Files.readAllBytes(path);
        s.uploadMusica("carlos","t",2016,etiquetas1,bytes,"mp3");

        Thread[] threads = new Thread[100];

        for(int i = 0 ; i < 5 ; i++){
            threads[i] = new Thread(new Teste(s));
        }
        for(int i = 0; i < 5 ; i++){
            threads[i].start();
        }

        for(int i = 0; i < 5 ; i++){
            threads[i].join();
        }
    }
}
