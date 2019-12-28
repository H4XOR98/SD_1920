package View;

import java.util.ArrayList;
import java.util.List;

public class ListagemLista
{
    //Variáveis de instancia

    /**
     * Título da listagem
     */
    private String titulo;
    /**
     * Lista para listar
     */
    private List<String> lista;
    /**
     * Página atual
     */
    private int paginaAtual;
    /**
     * Número de páginas
     */
    private int numPaginas;

    /**
     * Número de elementos por página
     */
    private static final int elementosPorPagina = 3;

    //Construtores

    /**
     * Construtores da classe ListagemLista
     * Declaração dos construtores por omissao (vazio) e parametrizado
     */

    /**
     * Construtor por omissão da ListagemLista
     */
    public ListagemLista(){
        this.titulo = "n/a";
        this.lista = new ArrayList<String>();
        this.paginaAtual = 0;
        this.numPaginas = 0;
    }


    /**
     * Construtor parametrizado da ListagemLista
     * @param titulo, lista
     */
    public ListagemLista(String titulo,List<String> lista) {
        this.titulo = titulo;
        this.lista = lista;
        this.paginaAtual = 0;
        this.numPaginas = (int) Math.ceil(((double)this.lista.size()) / ((double)this.elementosPorPagina));
    }

    //métodos de instância

    //Sets

    /**
     * Declara uma nova lista
     * @param lista
     */
    public void setLista(List<String> lista) {
        this.lista = lista;
    }

    //Gets

    /**
     * Devolve a página atual
     * @return paginaAtual
     */
    public int getPaginaAtual(){
        return this.paginaAtual;
    }

    /**
     * Devolve o número de páginas
     * @return numPaginas
     */
    public int getNumPaginas(){
        return this.numPaginas;
    }

    //Imprime uma lista

    /**
     * Método que pede uma opção para apresentar um menu
     */
    public void show() {
        System.out.println("\nOpção:");
    }

    /**
     * Método para apresentar um menu e ler uma opção
     * @param op
     */
    public void show(int op) {
        System.out.println("\f" + titulo + "\n");
        if(this.lista.isEmpty()){
            System.out.println("Não existem elementos a listar.");
            System.out.println("\nPagina : " + (this.paginaAtual) + " de " + this.numPaginas + ".");
        }
        else{
            this.paginaAtual = op;
            int num = this.elementosPorPagina * this.paginaAtual;
            for(int i = num ; i < this.elementosPorPagina + num && i < this.lista.size() && this.paginaAtual < this.numPaginas ; i++){
                System.out.println(this.lista.get(i));
            }
            System.out.println("\nPagina : " + (this.paginaAtual + 1) + " de " + this.numPaginas + ".");
        }
        if(this.numPaginas == 0 || this.numPaginas == 1){
            System.out.println("************Navegar************");
            System.out.println("*         0 - Sair            *");
            System.out.println("*******************************");
        }else if(this.paginaAtual == 0){
            System.out.println("\nJá se encontra na primeira página!\n");
            System.out.println("************Navegar************");
            System.out.println("*         2 - Próximo         *");
            System.out.println("*         3 - Escolher página *");
            System.out.println("*         0 - Sair            *");
            System.out.println("*******************************");
        }else if(this.paginaAtual+1 == this.numPaginas){
            System.out.println("Já se encontra na última página!\n");
            System.out.println("************Navegar************");
            System.out.println("*         1 - Anterior        *");
            System.out.println("*         3 - Escolher página *");
            System.out.println("*         0 - Sair            *");
            System.out.println("*******************************");
        }else{
            System.out.println("************Navegar************");
            System.out.println("*         1 - Anterior        *");
            System.out.println("*         2 - Próximo         *");
            System.out.println("*         3 - Escolher página *");
            System.out.println("*         0 - Sair            *");
            System.out.println("*******************************");
        }
        show();
    }
}