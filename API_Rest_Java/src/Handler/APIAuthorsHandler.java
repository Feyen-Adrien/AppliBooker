package Handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import model.dao.AuthorDAO;
import model.entity.Author;
import model.entity.Subject;
import model.viewmodel.AuthorSearchVM;
import model.viewmodel.SubjectSearchVM;

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
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        String requestMethod = exchange.getRequestMethod();
        // gestion des requetes
        if(requestMethod.equalsIgnoreCase("OPTIONS"))
        {
            exchange.sendResponseHeaders(200, -1);
            System.out.println("hello");
        }
        else if(requestMethod.equalsIgnoreCase("GET"))
        {
            System.out.println("GET request received");
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            System.out.println(exchange.getRequestURI().getQuery());
            String lastName ="";
            String firstName ="";
            if(queryParams.containsKey("lastName"))
            {
                lastName = queryParams.get("lastName");
            }
            if(queryParams.containsKey("firstName"))
            {
                firstName = queryParams.get("firstName");
            }
            System.out.println("Auteur rechercher :" + lastName + " " + firstName);
            try {
                String response = convertAuthorsToJson(lastName,firstName);
                sendResponse(exchange,200, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if (requestMethod.equalsIgnoreCase("POST"))
        {
            System.out.println("POST request received");
            String requestBody = readRequestBody(exchange);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
            String lastName = jsonObject.get("lastname").getAsString();
            String firstName = jsonObject.get("firstname").getAsString();
            String birthDate = jsonObject.get("birthdate").getAsString();

            System.out.println("Request Body: " + lastName + "//" + firstName + "//" + birthDate);
            if(lastName != null && !lastName.isEmpty() && firstName != null && !firstName.isEmpty() && birthDate != null && !birthDate.isEmpty())
            {
                Author author = new Author(null, lastName, firstName, birthDate);

                try {
                    addAuthor(author);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                sendResponse(exchange,201, "Auteur ajouté");
            }
            else
            {
                sendResponse(exchange,404, "Manque de données lors de l'ajout");
            }
        }
        else if(requestMethod.equalsIgnoreCase("PUT"))
        {
            System.out.println("PUT request received");
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            if(queryParams.containsKey("id"))
            {
                int id = Integer.parseInt(queryParams.get("id"));
                System.out.println("Mise à jour de l'auteur : " + id);
                String requestBody = readRequestBody(exchange);
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
                String lastName = jsonObject.get("lastname").getAsString();
                String firstName = jsonObject.get("firstname").getAsString();
                String birthDate = jsonObject.get("birthdate").getAsString();
                System.out.println("Request Body: " + lastName + "//" + firstName + "//" + birthDate);
                if(id >0 && lastName!= null && !lastName.isEmpty() && firstName != null && !firstName.isEmpty() && birthDate != null && !birthDate.isEmpty())
                {
                    Author author = new Author(id, lastName, firstName, birthDate);
                    try {
                        updateAuthor(author);
                        sendResponse(exchange,200,"Sujet mis à jour");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    sendResponse(exchange,404,"error update des informations ont été perdues lors de la réception");
                }

            }
            else
            {
                sendResponse(exchange,404,"error update manque id");
            }
        }
        else if(requestMethod.equalsIgnoreCase("DELETE"))
        {
            System.out.println("DELETE request received");
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            if(queryParams.containsKey("id"))
            {
                int id = Integer.parseInt(queryParams.get("id"));
                System.out.println("Suppresion de l'auteur : " + id);

                try {
                    deleteAuthor(id);
                    sendResponse(exchange,200,"Auteur supprimé");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                sendResponse(exchange,404,"error update");
            }
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

    private String convertAuthorsToJson(String lastname, String firstname) throws SQLException {
        if(firstname.isEmpty() && lastname.isEmpty())
        {
            authors = authorDAO.getAllAuthors();
        }
        else
        {
            AuthorSearchVM a = new AuthorSearchVM(lastname,firstname);
            authors = authorDAO.searchSubject(a);
        }
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
    private void deleteAuthor(int id) throws SQLException {
        authorDAO.deleteAuthor(id);
    }
}






