import Exceptions.UtilizadorInexistenteException;

import java.util.Map;

public class Sistema {
    private Map<String,Utilizador> utilizadores;
    private Map<Integer,Musica> musicas;
    private Map<String,Integer> etiquetas;

    public void loginUtilizador(String email,String password) throws UtilizadorInexistenteException {
        if(!this.utilizadores.containsKey(email)){
            throw new UtilizadorInexistenteException("Email não existe no Sistema!");
        }
        Utilizador utilizador = this.utilizadores.get(email);
        if(!utilizador.comparaPassword(password)){

        }
    }
}
