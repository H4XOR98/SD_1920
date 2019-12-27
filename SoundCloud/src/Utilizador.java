

public class Utilizador  {
    private int id;
    private String nome;
    private String password;
    private String pathDownload;

    public Utilizador(){
        this.id = -1;
        this.nome = "n/a";
        this.password = "n/a";
        this.password = "n/a";
    }

    public Utilizador(int id, String nome, String password, String pathDownload) {
        this.id = id;
        this.nome = nome;
        this.password = password;
        this.pathDownload = pathDownload;
    }

    public Utilizador(Utilizador utilizador){
        this.id = utilizador.getId();
        this.nome = utilizador.getNome();
        this.password = utilizador.getPassword();
        this.pathDownload = utilizador.getPathDownload();
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

    public  String getPathDownload() {
        return this.pathDownload;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---- Utilizador ----\n");
        sb.append("Id: " + this.id + ";\n");
        sb.append("Nome: " + this.nome + ";\n");
        sb.append("Password : " + this.password + ";\n");
        sb.append("Path Destino: " + this.pathDownload + ";\n");
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
        hash = 31 * hash + (this.pathDownload == null ? 0 : this.pathDownload.hashCode());
        return hash;
    }

    public Utilizador clone() {
        return new Utilizador(this);
    }


    public boolean  comparaPassword(String password){
        return this.password.equals(password);
    }
}
