import Exceptions.FormatoInvalidoException;
import Exceptions.PasswordIncorretaException;
import Exceptions.UtilizadorInexistenteException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Servidor {
    public static void main(String[] args) throws PasswordIncorretaException, UtilizadorInexistenteException, FormatoInvalidoException {
        try {

            ServerSocket serverSocket = new ServerSocket(12345);

            Sistema sistema = new Sistema();

            while (true) {

                Socket clientSocket = serverSocket.accept();

                //O que é recebido do Cliente
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // O que é enviado ao cliente
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream());


                String[] op ;
                String line;
                while ((line = in.readLine()) != null && !line.equals("quit")) {
                    op = line.split(" ");

                    switch (op[0]){
                        case "criar_conta":
                            out.println(sistema.criarConta(op[1],op[2],op[3]));
                            out.flush();
                            break;
                        case "login":
                            try {
                                sistema.loginUtilizador(op[1], op[2]);
                            }catch (Exception e){
                                out.println(e.getMessage());
                                out.flush();
                            }
                            break;
                        case "upload":
                            byte[] bytesFicheiro = Base64.getDecoder().decode(op[5]);
                            sistema.uploadMusica(op[1],op[2],Integer.parseInt(op[3]),op[4].split("|"),bytesFicheiro,op[6]);
                            break;
                    }

                }

                clientSocket.shutdownOutput();
                clientSocket.shutdownInput();
                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
