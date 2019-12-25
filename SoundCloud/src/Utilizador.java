import java.util.Objects;

public class Utilizador {
    private int id;
    private String nome;
    private String password;

    private static int identificador = 0;

    public Utilizador(){
        this.id = identificador++;
        this.nome = "n/a";
        this.password = "n/a";
    }

    public Utilizador(String nome, String password) {
        this.id = identificador++;
        this.nome = nome;
        this.password = password;
    }

    public Utilizador(Utilizador utilizador){
        this.id = utilizador.getId();
        this.nome = utilizador.getNome();
        this.password = utilizador.getPassword();
    }

    public int getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean comparaPassword(String password){
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---- Utilizador ----\n");
        sb.append("Id : " + this.id + "; \n");
        sb.append("Nome : " + this.nome + "; \n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Utilizador that = (Utilizador) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int) id;
        hash = 31 * hash + (this.nome == null ? 0 : this.nome.hashCode());
        hash = 31 * hash + (this.password == null ? 0 : this.password.hashCode());
        return hash;
    }


    public Utilizador clone(Utilizador utilizador) {
        return new Utilizador(utilizador);
    }
}
