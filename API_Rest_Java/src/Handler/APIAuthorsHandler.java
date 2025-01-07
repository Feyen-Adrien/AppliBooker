package Handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import model.dao.AuthorDAO;
import model.entity.Author;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIAuthorsHandler implements HttpHandler
{
    public AuthorDAO authorDAO = AuthorDAO.getInstance();
    List<Author> authors = new ArrayList<Author>();
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        // pour travaille sur 2 ports différents
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content Type");

        String requestMethod = exchange.getRequestMethod();
        // gestion des requetes
        if(requestMethod.equalsIgnoreCase("GET"))
        {
            System.out.println("GET request received");
            try {
                String response = convertAuthorsToJson();
                sendResponse(exchange,200, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (requestMethod.equalsIgnoreCase("POST")) {
            System.out.println("POST request received");
            String requestBody = readRequestBody(exchange);

            //transforme en JSON
            Gson gson = new Gson();
            Author author = gson.fromJson(requestBody, Author.class);
            try {
                addAuthor(author);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String response = "Auteur ajouté avec succès !";
            sendResponse(exchange,201, response);

        }
    }

    // fonctions utiles

    private void sendResponse(HttpExchange exchange, int status, String response) throws IOException {
        System.out.println("Envoie de la réponse (" + status + "): " + response);

        exchange.sendResponseHeaders(status, response.getBytes().length); // bien mettre getBytes sinon fonctionne pas
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private String readRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
    private static Map<String, String> parseQueryParams(String query)
    {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null)
        {
            String[] params = query.split("&");
            for (String param : params)
            {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2)
                {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    private String convertAuthorsToJson() throws SQLException {
        authors = authorDAO.getAllAuthors();
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(authors).getAsJsonArray();

        return gson.toJson(jsonArray);
    }
    private void addAuthor(Author author) throws SQLException {
        authorDAO.addAuthor(author);
    }
    private void updateAuthor(Author author) throws SQLException {
        authorDAO.updateAuthor(author);
    }
    private void deleteAuthor(Author author) throws SQLException {
        authorDAO.deleteAuthor(author.getId());
    }
}






