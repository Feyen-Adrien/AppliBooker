package Handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import model.dao.SubjectDAO;
import model.entity.Subject;
import model.viewmodel.SubjectSearchVM;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APISubjectsHandler implements HttpHandler
{
    public SubjectDAO subjectDAO = SubjectDAO.getInstance();
    List<Subject> subjects = new ArrayList<>();
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        // pour travaille sur 2 ports différents
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        String requestMethod = exchange.getRequestMethod();
        if(requestMethod.equals("OPTIONS"))
        {
            exchange.sendResponseHeaders(200, -1);
            System.out.println("hello");
        }
        // gestion des requetes
        else if(requestMethod.equalsIgnoreCase("GET"))
        {
            System.out.println("GET request received");
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            if(queryParams.containsKey("name"))
            {
                String name = queryParams.get("name");
                System.out.println("Sujet rechercher : " + name);
                try {
                    String response = convertSubjectsToJson(name);
                    sendResponse(exchange,200,response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                try {
                    String response = convertSubjectsToJson("");
                    sendResponse(exchange,200, response);
                    System.out.println("Reponse : " + response);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        else if (requestMethod.equalsIgnoreCase("POST"))
        {
            System.out.println("POST request received");
            String requestBody = readRequestBody(exchange);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
            String subjectName = jsonObject.get("name").getAsString();
            System.out.println("Request Body: " + subjectName);
            Subject subject = new Subject(null, subjectName);

            try {
                addSubject(subject);
                System.out.println("ddd");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            sendResponse(exchange,201, "Sujet ajouté");

        } else if (requestMethod.equalsIgnoreCase("PUT")) {
            System.out.println("PUT request received");
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            if(queryParams.containsKey("id"))
            {
                int id = Integer.parseInt(queryParams.get("id"));
                System.out.println("Mise à jour du sujet : " + id);
                String requestBody = readRequestBody(exchange);
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
                String subjectName = jsonObject.get("name").getAsString();
                System.out.println("Request Body: " + subjectName);
                Subject subject = new Subject(id, subjectName);
                try {
                    updateBook(subject);
                    sendResponse(exchange,200,"Sujet mis à jour");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                sendResponse(exchange,404,"error update");
            }

        } else if (requestMethod.equalsIgnoreCase("DELETE")) {
            System.out.println("DELETE request received");
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            if(queryParams.containsKey("id"))
            {
                int id = Integer.parseInt(queryParams.get("id"));
                System.out.println("Suppresion du sujet : " + id);

                try {
                    deleteBook(id);
                    sendResponse(exchange,200,"Sujet supprimé");
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

    private String convertSubjectsToJson(String name) throws SQLException {

        if(name.isEmpty())
        {
            subjects = subjectDAO.getAllSubjects();
        }
        else
        {
            SubjectSearchVM s = new SubjectSearchVM(name);
            subjects = subjectDAO.searchSubject(s);
        }
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(subjects).getAsJsonArray();
        return gson.toJson(jsonArray);
    }
    private void addSubject(Subject subject) throws SQLException {
        subjectDAO.addSubject(subject);
    }
    private void updateBook(Subject subject) throws SQLException {
        subjectDAO.updateSubject(subject);
    }
    private void deleteBook(int id) throws SQLException {
        subjectDAO.deleteSubject(id);
    }
}






