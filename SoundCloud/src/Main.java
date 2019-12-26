import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        List<String> s = new ArrayList<>();
        String path = "/Users/lazaropinheiro/Downloads/kygo.mp3";
        Path p = Paths.get(path);
        File ficheiro = new File(path);
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(p);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try (OutputStream fos = new FileOutputStream("/Users/lazaropinheiro/Downloads/kygo.mp3")) {
            fos.write(bytes);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Musica m = new Musica("Jingle Bells", "Jow Americano",2016,s,bytes,"mp3");
        InputStream is = new ByteArrayInputStream(bytes);

    }
}
