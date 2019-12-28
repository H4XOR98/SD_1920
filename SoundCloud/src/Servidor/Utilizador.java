package Servidor;

import java.util.concurrent.locks.ReentrantLock;

public class Utilizador  {
    private int id;
    private String nome;
    private String password;
    private String pathDownload;
    private ReentrantLock lockUtilizador;

    public Utilizador(){
        this.id = -1;
        this.nome = "n/a";
        this.password = "n/a";
        this.lockUtilizador = new ReentrantLock(true);
    }

    public Utilizador(int id, String nome, String password) {
        this.id = id;
        this.nome = nome;
        this.password = password;
        this.lockUtilizador = new ReentrantLock(true);
    }

    public Utilizador(Utilizador utilizador){
        this.id = utilizador.getId();
        this.nome = utilizador.getNome();
        this.password = utilizador.getPassword();
        this.lockUtilizador = new ReentrantLock(true);
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("---- Servidor.Utilizador ----\n");
        sb.append("Id: " + this.id + ";\n");
        sb.append("Nome: " + this.nome + ";\n");
        sb.append("Password : " + this.password + ";\n");
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

    public Utilizador clone() {
        return new Utilizador(this);
    }


    public boolean  comparaPassword(String password){
        return this.password.equals(password);
    }

    public void lock(){
        this.lockUtilizador.lock();
    }

    public void unlock(){
        this.lockUtilizador.unlock();
    }
}
