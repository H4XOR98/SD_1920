
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(12345);
        Sistema sistema = new Sistema();
        while(true){
            Socket socket = s.accept();
            new Thread(new TaskRunner(socket, sistema)).start();
        }
    }
}
