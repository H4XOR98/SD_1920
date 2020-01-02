package View;

public class MenuInicial {
    private Titulo titulo;

    public MenuInicial(Titulo  titulo) {
        this.titulo = titulo;
    }

    public void showMenuInical(){
        titulo.showtitulo();
        System.out.println("1 - Iniciar Sessão");
        System.out.println("2 - Criar conta");
        System.out.println("0 - Sair");
        System.out.println("\nIntroduza a sua opção.");
        System.out.println("Opção:");
    }
}

