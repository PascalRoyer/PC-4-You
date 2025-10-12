import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class SimpleHttpServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new FileHandler());
        server.setExecutor(null);
        System.out.println("🚀 Serveur lancé sur http://localhost:8000/");
        server.start();
    }

    static class FileHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath().substring(1);
            if (path.isEmpty()) path = "index.html";
            File file = new File(path);
            if (!file.exists()) {
                String response = "404 Not Found";
                t.sendResponseHeaders(404, response.length());
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
                return;
            }
            byte[] bytes = Files.readAllBytes(file.toPath());
            t.sendResponseHeaders(200, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
