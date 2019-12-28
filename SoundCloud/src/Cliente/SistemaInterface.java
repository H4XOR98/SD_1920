package Cliente;

import Exceptions.*;

import java.io.IOException;
import java.util.List;

public interface SistemaInterface {
    public int criarConta(String nome, String password) throws IOException, UtilizadorJaExisteException;

    public String loginUtilizador(String nome, String password) throws UtilizadorInexistenteException, PasswordIncorretaException, IOException;

    public void uploadMusica(String titulo, String interprete, int ano, List<String> etiquetas, byte[] bytesFicheiro, String formato) throws FormatoInvalidoException, IOException;

    public List<String> procurarMusica(String etiqueta) throws IOException, EtiquetaInexistenteException;

    public void downloadMusica(int idMusica, String pathDestino) throws MusicaInexistenteException, IOException;
}
