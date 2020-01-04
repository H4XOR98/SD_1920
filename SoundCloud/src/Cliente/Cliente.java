package Cliente;

import Exceptions.*;
import Input.Input;
import View.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Cliente {


    public static void main(String[] args) {
        SistemaRemoto sistemaRemoto;
        ViewException viewException = new ViewException();
        try{
            String nome, password;
            sistemaRemoto = new SistemaRemoto();
            AtomicBoolean execucao = new AtomicBoolean(true);
            Thread threadLeituraNotificacao = new Thread(new ThreadLeituraNotificacao(sistemaRemoto,execucao));
            threadLeituraNotificacao.start();
            int op;
            Titulo titulo = new Titulo();
            MenuInicial menuInicial = new MenuInicial(titulo);
            do{
                menuInicial.showMenuInical();
                op = Input.lerInt();
                switch (op){
                    case 1:
                        titulo.showtitulo();
                        System.out.println("Introduza o seu nome:");
                        nome = Input.lerString();
                        System.out.println("Introduza a sua password:");
                        password = Input.lerString();
                        try {
                            nome = sistemaRemoto.loginUtilizador(nome, password);
                            System.out.println("Bem vindo, " + nome);
                            logado(sistemaRemoto,titulo,viewException);
                        } catch (UtilizadorInexistenteException e) {
                            viewException.showViewException(e.getMessage());
                        } catch (PasswordIncorretaException e) {
                            viewException.showViewException(e.getMessage());
                        }
                        break;
                    case 2:
                        titulo.showtitulo();
                        System.out.println("Introduza o seu nome:");
                        nome = Input.lerString();
                        System.out.println("Introduza uma password:");
                        password = Input.lerString();
                        try {
                            int id = sistemaRemoto.criarConta(nome, password);
                            System.out.println("O id da sua conta é o " + id + ".");
                        } catch (UtilizadorJaExisteException e) {
                            viewException.showViewException(e.getMessage());
                        }
                        op = 0;
                        break;
                    case 0:
                        try {
                            execucao.set(false);
                            threadLeituraNotificacao.join();
                            sistemaRemoto.sair();
                        } catch (IOException e) {
                            viewException.showViewException("Erro ao terminar sessão");
                        } catch (InterruptedException e) {
                            viewException.showViewException("Erro ao terminar sessão");
                        }
                        System.out.println("Até Breve!");
                        System.exit(0);
                        break;
                    default:
                        viewException.showViewException("       Opção inválida!       ");
                        break;
                }

            }while(true);
        } catch (IOException e) {
            viewException.showViewException("Algo de errado aconteceu com a ligação ao servidor");
        }
    }


    private static void logado(SistemaRemoto sistemaRemoto, Titulo title, ViewException viewException){
        int op = -1;
        int op1 = -1, opcao;
        String titulo, interprete, formato, etiqueta,autor;
        int ano;
        List<String> etiquetas;
        byte[] bytesFicheiro;
        String aux;
        MenuLogin menuLogin = new MenuLogin(title);
        do{
            menuLogin.showMenuLogado();
            op = Input.lerInt();
            switch (op){
                case 0:
                    sistemaRemoto.logoutUtilizador();
                    break;
                case 1:
                    title.showtitulo();
                    System.out.println("Introduza o titulo");
                    titulo = Input.lerString();
                    System.out.println("Introduza o interprete");
                    interprete = Input.lerString();
                    System.out.println("Introduza o autor");
                    autor = Input.lerString();
                    System.out.println("Introduza o ano");
                    ano = Input.lerInt();
                    etiquetas = new ArrayList<>();
                    MenuEtiquetas menuEtiquetas = new MenuEtiquetas();
                    do{
                        menuEtiquetas.showmenuEtiquetas();
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
                        sistemaRemoto.uploadMusica(titulo, interprete, autor, ano, etiquetas, bytesFicheiro, formato);
                        System.out.println("Upload realizado com sucesso");
                    } catch (FormatoInvalidoException e) {
                        viewException.showViewException(e.getMessage());
                    } catch (IOException e) {
                        viewException.showViewException("Impossível carregar ficheiro");
                    }
                    break;
                case 2:
                    title.showtitulo();
                    System.out.println("Introduza uma etiqueta");
                    etiqueta = Input.lerString();
                    try {
                        etiquetas = sistemaRemoto.procurarMusica(etiqueta);
                        opcao = 0;
                        int pagina = 0;
                        int paginaOP = 0;
                        ListagemLista listagem = new ListagemLista("Lista de Músicas",etiquetas);
                        do {
                            listagem.show(opcao);
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
                                        viewException.showViewException("Opção Inválida!");
                                    }else{
                                        pagina = paginaOP-1;
                                    }
                                    listagem.show(pagina);
                                    break;
                                case 0:
                                    listagem.show();
                                    break;
                                default:
                                    viewException.showViewException("Opção Inválida!");
                                    break;
                            }
                        }while(opcao !=0);
                    } catch (EtiquetaInexistenteException e) {
                        viewException.showViewException(e.getMessage());
                    } catch (IOException e) {
                        viewException.showViewException(e.getMessage());
                    }
                    break;
                case 3:
                    title.showtitulo();
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
                    viewException.showViewException("       Opção inválida!       ");
                    break;
            }
        }while(op != 0);
    }
}

