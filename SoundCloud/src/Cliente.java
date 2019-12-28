import Exceptions.*;

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
            String nome, password;
            sistemaRemoto = new SistemaRemoto();
            int op;
            do{
                View.menuInical();
                op = Input.lerInt();
                switch (op){
                   case 1:
                       View.titulo();
                       System.out.println("Introduza o seu nome:");
                       nome = Input.lerString();
                       System.out.println("Introduza a sua password:");
                       password = Input.lerString();
                       try {
                           nome = sistemaRemoto.loginUtilizador(nome, password);
                           System.out.println("Bem vindo, " + nome);
                           logado(nome,sistemaRemoto);
                       } catch (UtilizadorInexistenteException e) {
                           View.viewException(e.getMessage());
                       } catch (PasswordIncorretaException e) {
                           View.viewException(e.getMessage());
                       }

                       break;
                   case 2:
                       View.titulo();
                       System.out.println("Introduza o seu nome:");
                       nome = Input.lerString();
                       System.out.println("Introduza uma password:");
                       password = Input.lerString();
                       try {
                           int id = sistemaRemoto.criarConta(nome, password);
                           System.out.println("O id da sua conta é o " + id + ".");
                       } catch (UtilizadorJaExisteException e) {
                           View.viewException(e.getMessage());
                       }
                       break;
                   case 0:
                       System.out.println("Até Breve!");
                       System.exit(0);
                       break;
                   default:
                       View.viewException("       Opção inválida!       ");
                       break;
               }

           }while(true);
        } catch (IOException e) {
            View.viewException("Algo de errado aconteceu com a ligação ao servidor");
        }
    }


    private static void logado(String nomeUtilizador,SistemaRemoto sistemaRemoto){
        int op = -1;
        int op1 = -1;
        String titulo, interprete, formato, etiqueta;
        int ano;
        List<String> etiquetas;
        byte[] bytesFicheiro;
        String aux;
        do{
            View.menuLogado();
            op = Input.lerInt();
            switch (op){
                case 0:
                    try {
                        sistemaRemoto.logoutUtilizador();
                    } catch (IOException e) {
                        View.viewException("Erro ao terminar sessão");
                    }
                    break;
                case 1:
                    View.titulo();
                    System.out.println("Introduza o titulo");
                    titulo = Input.lerString();
                    System.out.println("Introduza o interprete");
                    interprete = Input.lerString();
                    System.out.println("Introduza o ano");
                    ano = Input.lerInt();
                    etiquetas = new ArrayList<>();
                    do{
                        View.menuEtiquetas();
                        op1 = Input.lerInt();
                        if(op1 != 0) {
                            System.out.println("Introduza etiqueta:");
                            aux = Input.lerString();
                            etiquetas.add(aux);
                        }
                    }while(op1 != 0);
                    System.out.println("Insira a path do fiheiro que pretende dar upload");
                    aux = Input.lerString();
                    Path path = Paths.get(aux);
                    try {
                        bytesFicheiro = Files.readAllBytes(path);
                        String nomeFicheiro = path.getFileName().toString();
                        String[] partes = nomeFicheiro.split("_");
                        formato = partes[partes.length - 1].substring(partes[partes.length - 1].lastIndexOf(".") + 1);
                        sistemaRemoto.uploadMusica(titulo, interprete, ano, etiquetas, bytesFicheiro, formato);
                        System.out.println("Upload realizado com sucesso");
                    } catch (FormatoInvalidoException e) {
                        View.viewException(e.getMessage());
                    } catch (IOException e) {
                        View.viewException("Impossível carregar ficheiro");
                    }
                    break;
                case 2:
                    View.titulo();
                    System.out.println("Introduza uma etiqueta");
                    etiqueta = Input.lerString();
                    try {
                         etiquetas = sistemaRemoto.procurarMusica(etiqueta);
                         op1 = 0;
                         ListagemLista listagem = new ListagemLista("Lista de Músicas",etiquetas);
                         do {
                             listagem.show(op1);
                             op1 = Input.lerInt();
                         }while(op1!=0);
                    } catch (EtiquetaInexistenteException e) {
                        View.viewException(e.getMessage());
                    } catch (IOException e) {
                        View.viewException(e.getMessage());
                    }
                    break;
                case 3:

                    break;
                default:
                    break;
            }
        }while(op != 0);
    }
    /*public static void main(String[] args) {
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
            System.out.println("Introduza o seu nome! ");
            nome = sc.nextLine();
            System.out.println("Introduza a password! ");
            password  = sc.nextLine();
            sistemaRemoto.loginUtilizador(nome,password);


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
        } catch (UtilizadorInexistenteException e) {
            e.printStackTrace();
        } catch (PasswordIncorretaException e) {
            e.printStackTrace();
        } catch (MusicaInexistenteException e) {
            e.printStackTrace();
        }
    }
     */
}
