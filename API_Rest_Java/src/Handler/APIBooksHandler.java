package Handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import model.dao.AuthorDAO;
import model.dao.BookDAO;
import model.dao.SubjectDAO;
import model.entity.Book;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class APIBooksHandler implements HttpHandler
{
    public BookDAO bookDAO = BookDAO.getInstance();
    public AuthorDAO authorDAO = AuthorDAO.getInstance();
    public SubjectDAO subjectDAO = SubjectDAO.getInstance();
    List<Book> books = new ArrayList<Book>();
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
                String response = convertBooksToJson();
                sendResponse(exchange,200, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (requestMethod.equalsIgnoreCase("POST")) {
            System.out.println("POST request received");
            String requestBody = readRequestBody(exchange);

            //transforme en JSON
            Gson gson = new Gson();
            Book book = gson.fromJson(requestBody, Book.class);
            try {
                addBook(book);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String response = "Livre ajouté avec succès !";
            sendResponse(exchange,201, response);

        }
    }

    // fonctions utiles

    private void sendResponse(HttpExchange exchange, int status, String response) throws IOException {
        System.out.println("Envoie de la réponse (" + status + "): " + response);
        exchange.sendResponseHeaders(status, response.length());
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

    private String convertBooksToJson() throws SQLException {
        books = bookDAO.getAllBooks();
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(books).getAsJsonArray();
        int i=0;
        for(JsonElement jsonElement : jsonArray){
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String NomAuteur = authorDAO.getAuthorById(books.get(i).getAuthor_id()).getLastName()+" "+authorDAO.getAuthorById(books.get(i).getAuthor_id()).getFirstSurname();
            String Sujet = subjectDAO.getSubjectById(books.get(i).getSubject_id()).getSubject_name();

            jsonObject.addProperty("author_id", NomAuteur);
            jsonObject.addProperty("subject_id", Sujet);
            i++;
        }
        return gson.toJson(jsonArray);
    }
    private void addBook(Book book) throws SQLException {
        bookDAO.addBook(book);
    }
    private void updateBook(Book book) throws SQLException {
        bookDAO.updateBook(book);
    }
    private void deleteBook(Book book) throws SQLException {
        bookDAO.deleteBookById(book.getId());
    }
}






