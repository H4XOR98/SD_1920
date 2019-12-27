import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TaskRunner implements Runnable{
    private Socket socket;
    private Sistema sistema;
    private PrintWriter out;
    private BufferedReader in;

    public TaskRunner(Socket socket, Sistema sistema) throws IOException {
        this.socket = socket;
        this.sistema = sistema;
        this.out = new PrintWriter(this.socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String[] op ;
            String line;
            while ((line = in.readLine()) != null && !line.equals("quit")) {
                // dividir por espaços
                op = line.split(" ");

                this.runCommand(op);
                this.out.flush();
            }

            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();
            this.in.close();
            this.out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void runCommand(String[] op) {
        switch (op[0]) {
            case "criar_conta":
                this.criarConta(op[1],op[2],op[3]);
                break;
            case "login":
                this.login(op[1], op[2]);
                break;
        }
    }

    private void criarConta(String nome, String password, String pathDownload){
        out.println(sistema.criarConta(nome, password, pathDownload));
    }

    private void login(String nome, String password){
        try {
            sistema.loginUtilizador(nome, password);
            out.println("Bem vindo, " + nome + "!\n");
        }catch (Exception e){
            out.println(e.getMessage());
        }
    }

}
