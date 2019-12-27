import Exceptions.FormatoInvalidoException;
import Exceptions.PasswordIncorretaException;
import Exceptions.UtilizadorInexistenteException;

import java.io.IOException;
import java.util.List;

public interface SistemaInterface {
    public int criarConta(String nome, String password, String pathDownload) throws IOException;

    public void loginUtilizador(String nome, String password) throws UtilizadorInexistenteException, PasswordIncorretaException, IOException;

    public void uploadMusica(String titulo, String interprete, int ano, List<String> etiquetas, byte[] bytesFicheiro, String formato) throws FormatoInvalidoException, IOException;

    public List<String> procurarMusica(String etiqueta) throws IOException;
}
