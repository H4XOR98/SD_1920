public class View {

    public void titulo(){
        System.out.println("*-----------------------------*");
        System.out.println("|            SOUND            |");
        System.out.println("|              &              |");
        System.out.println("|            CLOUD            |");
        System.out.println("*-----------------------------*");
    }


    public void menuInical(){
        this.titulo();
        System.out.println("1 - Iniciar Sessão");
        System.out.println("2 - Criar conta");
        System.out.println("0 - Sair");
        System.out.println("\nIntroduza a sua opção.");
        System.out.println("Opção:");
    }

    public void menuLogado(){
        this.titulo();
        System.out.println("1 - Upload de Conteudo");
        System.out.println("2 - Download de Conteudo");
        System.out.println("0 - Voltar");
        System.out.println("\nIntroduza a sua opção.");
        System.out.println("Opção:");
    }
}
