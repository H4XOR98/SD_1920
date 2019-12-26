import Exceptions.OperacaoInvalidaException;
import Exceptions.PasswordIncorretaException;
import Exceptions.UtilizadorInexistenteException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args)  {
        /*Sistema s = new Sistema();
        try {
            s.registarUtilizador("Candido_Faisca");
            s.loginUtilizador("Candido_Faisca");
        } catch (OperacaoInvalidaException e) {
            System.out.println(e.getMessage());
        } catch (UtilizadorInexistenteException e) {
            e.printStackTrace();
        } catch (PasswordIncorretaException e) {
            e.printStackTrace();
        }
        List<String> s = new ArrayList<>();
        String path = "/Users/lazaropinheiro/Downloads/kygo.mp3";
        Path p = Paths.get(path);
        File ficheiro = new File(path);
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(p);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try (OutputStream fos = new FileOutputStream("/Users/lazaropinheiro/Downloads/kygo.mp3")) {
            fos.write(bytes);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Musica m = new Musica("Jingle Bells", "Jow Americano",2016,s,bytes,"mp4");
        InputStream is = new ByteArrayInputStream(bytes);
        System.out.println(m.toString());*/

    }
}
