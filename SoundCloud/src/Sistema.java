import Exceptions.FormatoInvalidoException;
import Exceptions.PasswordIncorretaException;
import Exceptions.UtilizadorInexistenteException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sistema {
    private Map<String,Utilizador> utilizadores;
    private Map<Integer,Musica> musicas;
    private Map<String,List<Integer>> etiquetas;

    public void loginUtilizador(String email,String password) throws UtilizadorInexistenteException, PasswordIncorretaException {
        if(!this.utilizadores.containsKey(email)){
            throw new UtilizadorInexistenteException("Email não existe no Sistema!");
        }
        Utilizador utilizador = this.utilizadores.get(email);
        if(!utilizador.comparaPassword(password)){
            throw new PasswordIncorretaException("A password inserida está incorreta");
        }
    }

    public void publicarMusica(String titulo, String interprete, int ano, List<String> etiquetas, String conteudoFicheiro,String formato){
        if(!FormatosMusicaEnum.validaFormato(formato)){
            throw new FormatoInvalidoException("O formato do ficheiro selecionado não é válido!");
        }
        byte[] bytesFicheiro = conteudoFicheiro.getBytes();
        Musica musica = new Musica(titulo,interprete,ano,etiquetas,bytesFicheiro,formato);
        int id = musica.getId();
        musicas.put(id ,musica);
        for(String etiqueta : etiquetas){
            if(!this.etiquetas.containsKey(etiqueta)){
                this.etiquetas.put(etiqueta,new ArrayList<>());
            }
            this.etiquetas.get(etiqueta).add(id);
        }
    }
}
