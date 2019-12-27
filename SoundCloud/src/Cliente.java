import Exceptions.FormatoInvalidoException;
import Exceptions.MusicaInexistenteException;
import Exceptions.PasswordIncorretaException;
import Exceptions.UtilizadorInexistenteException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        SistemaRemoto sistemaRemoto;
        try{
            sistemaRemoto = new SistemaRemoto();

            Scanner sc = new Scanner(System.in);
            System.out.println("------ CRIAR CONTA -----");
            System.out.println("Introduza o seu nome! ");
            String nome = sc.nextLine();
            System.out.println("Introduza a password! ");
            String password  = sc.nextLine();
            System.out.println("O seu id é o " + sistemaRemoto.criarConta(nome,password,"/Users/lazaropinheiro/Desktop/"));
            /*System.out.println("Introduza o seu nome! ");
            String nome = sc.nextLine();
            System.out.println("Introduza a password! ");
            String password  = sc.nextLine();
            sistemaRemoto.loginUtilizador(nome,password);*/


            List<String> etiquetas = new ArrayList<>();
            etiquetas.add("Trance");
            etiquetas.add("EDM");
            String p = "/Users/lazaropinheiro/Downloads/kygo.mp3";
            Path path = Paths.get(p);
            byte[] bytes = Files.readAllBytes(path);
            sistemaRemoto.uploadMusica("Firestone","Kygo",2016,etiquetas,bytes,"mp3");
            //
            etiquetas = new ArrayList<>();
            etiquetas.add("EDM");
            p = "/Users/lazaropinheiro/Downloads/Bocejo_SONY_g.mp4";
            path = Paths.get(p);
            bytes = Files.readAllBytes(path);
            sistemaRemoto.uploadMusica("Bocejo","SONY",2016,etiquetas,bytes,"mp4");
            //
            etiquetas = new ArrayList<>();
            etiquetas.add("Kuduro");
            etiquetas.add("Gospel");
            etiquetas.add("EDM");
            p = "/Users/lazaropinheiro/Downloads/carlos_t.mp3";
            path = Paths.get(p);
            bytes = Files.readAllBytes(path);
            sistemaRemoto.uploadMusica("Carlos","t",2016,etiquetas,bytes,"mp3");
            //

            List<String> musicas = sistemaRemoto.procurarMusica("EDM");
            for(String m : musicas){
                System.out.println(m);
            }
            sistemaRemoto.downloadMusica(1);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (FormatoInvalidoException e) {
            e.printStackTrace();
        } /*catch (UtilizadorInexistenteException e) {
            e.printStackTrace();
        } catch (PasswordIncorretaException e) {
            e.printStackTrace();
        }  catch (UtilizadorInexistenteException e) {
            e.printStackTrace();
        } catch (PasswordIncorretaException e) {
            e.printStackTrace();
        }*/catch (MusicaInexistenteException e) {
            e.printStackTrace();
        }
    }
}
