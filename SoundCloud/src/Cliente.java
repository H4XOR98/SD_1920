import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    public static void main(String[] args) {
        SistemaRemoto sistemaRemoto;
        try{
            sistemaRemoto = new SistemaRemoto();
            List<String> etiquetas = new ArrayList<>();
            etiquetas.add("Trance");
            etiquetas.add("EDM");
            String p = "/Users/lazaropinheiro/Downloads/kygo.mp3";
            Path path = Paths.get(p);
            byte[] bytes = Files.readAllBytes(path);
            sistemaRemoto.uploadMusica("Firestone","Kygo",2016,etiquetas,bytes,"mp3");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FormatoInvalidoException e) {
            e.printStackTrace();
        }
        /*try{
            sistemaRemoto = new SistemaRemoto();
            Scanner sc = new Scanner(System.in);
            System.out.println("------ CRIAR CONTA -----");
            System.out.println("Introduza o seu nome! ");
            String nome = sc.nextLine();
            System.out.println("Introduza a password! ");
            String password  = sc.nextLine();
            System.out.println("O seu id é o " + sistemaRemoto.criarConta(nome,password,"/src/root/"));
            System.out.println("Introduza o seu nome! ");
            nome = sc.nextLine();
            System.out.println("Introduza a password! ");
            password  = sc.nextLine();
            sistemaRemoto.loginUtilizador(nome,password);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UtilizadorInexistenteException e) {
            e.printStackTrace();
        } catch (PasswordIncorretaException e) {
            e.printStackTrace();
        }*/
    }
}
