package Cliente;

import Exceptions.*;
import View.*;
import Input.*;

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
                           logado(sistemaRemoto);
                       } catch (UtilizadorInexistenteException e) {
                           View.viewErro(e.getMessage());
                       } catch (PasswordIncorretaException e) {
                           View.viewErro(e.getMessage());
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
                           View.viewErro(e.getMessage());
                       }
                       op = 0;
                       break;
                   case 0:
                       try {
                           sistemaRemoto.sair();
                       } catch (IOException e) {
                           View.viewErro("Erro ao terminar sessão");
                       }
                       System.out.println("Até Breve!");
                       System.exit(0);
                       break;
                   default:
                       View.viewErro("       Opção inválida!       ");
                       break;
               }

           }while(true);
        } catch (IOException e) {
            View.viewErro("Algo de errado aconteceu com a ligação ao servidor");
        }
    }


    private static void logado(SistemaRemoto sistemaRemoto){
        int op = -1;
        int op1 = -1, opcao;
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
                    sistemaRemoto.logoutUtilizador();
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
                        View.viewErro(e.getMessage());
                    } catch (IOException e) {
                        View.viewErro("Impossível carregar ficheiro");
                    }
                    break;
                case 2:
                    View.titulo();
                    System.out.println("Introduza uma etiqueta");
                    etiqueta = Input.lerString();
                    try {
                         etiquetas = sistemaRemoto.procurarMusica(etiqueta);
                         opcao = 0;
                         int pagina = 0;
                         int paginaOP = 0;
                         ListagemLista listagem = new ListagemLista("Lista de Músicas",etiquetas);
                         do {
                             listagem.show(op1);
                             opcao = Input.lerInt();
                             switch (opcao){
                                 case 1:
                                     pagina -= 1;
                                     if(pagina < 0) pagina =0;
                                     listagem.show(pagina);
                                     break;
                                 case 2:
                                     pagina += 1;
                                     if(pagina >= listagem.getNumPaginas()) pagina = listagem.getNumPaginas() - 1;
                                     listagem.show(pagina);
                                     break;
                                 case 3:
                                     int elem = listagem.getNumPaginas();
                                     System.out.println("\n\nQual a página?");
                                     listagem.show();
                                     paginaOP = Input.lerInt();
                                     if(paginaOP < 0 || paginaOP > elem){
                                         View.viewErro("Opção Inválida!");
                                     }else{
                                         pagina = paginaOP-1;
                                     }
                                     listagem.show(pagina);
                                     break;
                                 case 0:
                                     listagem.show();
                                     break;
                                 default:
                                     View.viewErro("Opção Inválida!");
                                     break;
                             }
                         }while(opcao !=0);
                    } catch (EtiquetaInexistenteException e) {
                        View.viewErro(e.getMessage());
                    } catch (IOException e) {
                        View.viewErro(e.getMessage());
                    }
                    break;
                case 3:
                        View.titulo();
                        System.out.println("Introduza o id da música que pretende fazer download.");
                        int id = Input.lerInt();
                        System.out.println("Introduza a path para onde pretende que o download seja efetuado.");
                        aux = Input.lerString();
                        try{
                            String nomeMusica = sistemaRemoto.downloadMusica(id,aux);
                            System.out.println("Download da música " + nomeMusica + " efetuado com sucesso.");
                        } catch (MusicaInexistenteException e) {
                            System.out.println(e.getMessage());
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    break;
                default:
                    View.viewErro("       Opção inválida!       ");
                    break;
            }
        }while(op != 0);
    }
}
