package Handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import model.dao.BookDAO;
import model.entity.Book;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class BooksHandler implements HttpHandler
{
    public BookDAO bookDAO = BookDAO.getInstance();
    private final String HTML_FILE_PATH = ".\\API_Rest_Java\\src\\view\\books.html";
    private final String CSS_FILE_PATH = ".\\API_Rest_Java\\src\\style\\style.css";
    private final String JS_FILE_PATH = ".\\API_Rest_Java\\src\\controller\\books.js";
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String requestPath = exchange.getRequestURI().getPath();

        //chargement des différents fichier
        if ("/books".equals(requestPath))
        {
            // Gestion de la requête pour la page HTML
            sendFileResponse(exchange, HTML_FILE_PATH, "text/html");
        } else if ("/style.css".equals(requestPath)) {
            // Gestion de la requête pour le fichier CSS
            sendFileResponse(exchange, CSS_FILE_PATH, "text/css");
        } else if("/books.js".equals(requestPath)) {
            sendFileResponse(exchange, JS_FILE_PATH, "text/javascript");

        }
        else {
            // Si la ressource n'est pas trouvée, envoyer une erreur 404
            sendError404(exchange);
        }
    }

    // fonctions utiles
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





