package View;

public class MenuLogin {
    private Titulo titulo;

    public MenuLogin(Titulo  titulo){
        this.titulo = titulo;
    }

    public void showMenuLogado(){
        titulo.showtitulo();
        System.out.println("1 - Upload de Conteudo");
        System.out.println("2 - Procurar Música");
        System.out.println("3 - Download de Conteudo");
        System.out.println("0 - Terminar Sessão");
        System.out.println("\nIntroduza a sua opção.");
        System.out.println("Opção:");
    }
}
