package Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RootHandler implements HttpHandler {

    private static final String HTML_FILE_PATH = "D:\\HEPL_3_2024-2025\\Reseaux_Technologie_Internet\\Labo\\AppliBooker\\API_Rest_Java\\src\\view\\index.html";
    private static final String CSS_FILE_PATH = "D:\\HEPL_3_2024-2025\\Reseaux_Technologie_Internet\\Labo\\AppliBooker\\API_Rest_Java\\src\\style\\style.css";



    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();

        if ("/".equals(requestPath)) {
            // Gestion de la requête pour la page HTML
            sendFileResponse(exchange, HTML_FILE_PATH, "text/html");
        } else if ("/style.css".equals(requestPath)) {
            // Gestion de la requête pour le fichier CSS
            sendFileResponse(exchange, CSS_FILE_PATH, "text/css");
        } else {
            // Si la ressource n'est pas trouvée, envoyer une erreur 404
            sendError404(exchange);
        }
    }

    private void sendFileResponse(HttpExchange exchange, String filePath, String contentType) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            byte[] fileBytes = Files.readAllBytes(path);
            exchange.sendResponseHeaders(200, fileBytes.length);
            exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }
        } else {
            sendError404(exchange);
        }
    }

    private void sendError404(HttpExchange exchange) throws IOException {
        String response = "Fichier introuvable !";
        exchange.sendResponseHeaders(404, response.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
