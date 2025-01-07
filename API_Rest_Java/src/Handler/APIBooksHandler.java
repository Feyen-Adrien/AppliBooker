package Handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.*;
import model.dao.AuthorDAO;
import model.dao.BookDAO;
import model.dao.SubjectDAO;
import model.entity.Author;
import model.entity.Book;
import model.entity.Subject;
import model.viewmodel.BookSearchVM;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        String requestMethod = exchange.getRequestMethod();
        // gestion des requetes
        if(requestMethod.equals("OPTIONS"))
        {
            exchange.sendResponseHeaders(200, -1);
            System.out.println("hello");
        }
        else if(requestMethod.equalsIgnoreCase("GET"))
        {
            System.out.println("GET request received");
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            System.out.println(exchange.getRequestURI().getQuery());
            String lastName="";
            String subject="";
            String title="";
            String price="0";
            if(queryParams.containsKey("lastName"))
            {
                try {
                    if(Integer.parseInt(queryParams.get("lastName"))>0)
                    {
                        lastName = authorDAO.getAuthorById(Integer.parseInt(queryParams.get("lastName"))).getLastName();
                        System.out.println(lastName);
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(queryParams.containsKey("subject"))
            {
                try {
                    if(Integer.parseInt(queryParams.get("subject"))>0)
                    {
                        subject = subjectDAO.getSubjectById(Integer.parseInt(queryParams.get("subject"))).getSubject_name();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                };
            }
            if(queryParams.containsKey("title"))
            {
                title = queryParams.get("title");
            }
            if(queryParams.containsKey("price"))
            {
                price = queryParams.get("price");
            }
            System.out.println("lastName: "+lastName + subject + title + price);
            try {
                System.out.println("Critères = nom="+lastName+", titre ="+title+", sujet="+subject+", prix =<"+price);
                String response = convertBooksToJson(lastName,title,subject,price);
                sendResponse(exchange,200, response);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if (requestMethod.equalsIgnoreCase("POST"))
        {
            System.out.println("POST request received");
            String requestBody = readRequestBody(exchange);

            //transforme en JSON
            Gson gson = new Gson();
            JsonObject jsonObject= gson.fromJson(requestBody, JsonObject.class);
            int idAuthor = jsonObject.get("idAuthor").getAsInt();
            int idSubject = jsonObject.get("idSubject").getAsInt();
            String title = jsonObject.get("title").getAsString();
            String isbn = jsonObject.get("isbn").getAsString();
            int stockQuantity = jsonObject.get("stockQuantity").getAsInt();
            int pageCount = jsonObject.get("pageCount").getAsInt();
            float price = jsonObject.get("price").getAsFloat();
            int annee = jsonObject.get("year").getAsInt();
            System.out.println(idAuthor+";"+idSubject+";"+title+";"+isbn);
            Book book= new Book(null,idAuthor,idSubject,title,isbn,pageCount,stockQuantity,price,annee,null,null,null);
            try {
                addBook(book);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String response = "Livre ajouté avec succès !";
            sendResponse(exchange,201, response);
        }
        else if (requestMethod.equalsIgnoreCase("PUT"))
        {
            System.out.println("PUT request received");
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            if(queryParams.containsKey("id"))
            {
                int id = Integer.parseInt(queryParams.get("id"));
                System.out.println("Mise à jour du livre : " + id);
                String requestBody = readRequestBody(exchange);
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
                int idAuthor = jsonObject.get("idAuthor").getAsInt();
                int idSubject = jsonObject.get("idSubject").getAsInt();
                String title = jsonObject.get("title").getAsString();
                String isbn = jsonObject.get("isbn").getAsString();
                int stockQuantity = jsonObject.get("stockQuantity").getAsInt();
                int pageCount = jsonObject.get("pageCount").getAsInt();
                float price = jsonObject.get("price").getAsFloat();
                int annee = jsonObject.get("year").getAsInt();
                System.out.println(idAuthor+";"+idSubject+";"+title+";"+isbn);
                Book b = new Book(id,idAuthor,idSubject,title,isbn,pageCount,stockQuantity,price,annee,null,null,null);
                if(idAuthor != -1 && idSubject != -1 && title != "" && isbn != "" && pageCount >0 && price >0.0 && annee >0)
                {
                    try {
                        updateBook(b);
                        sendResponse(exchange,200,"Livre mis à jour");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    sendResponse(exchange,404,"error update des informations ont été perdues lors de la reception");
                }

            }
            else
            {
                sendResponse(exchange,404,"error update manque id");
            }
        }
        else if (requestMethod.equalsIgnoreCase("DELETE"))
        {
            System.out.println("DELETE request received");
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
            if(queryParams.containsKey("id"))
            {
                int id = Integer.parseInt(queryParams.get("id"));
                System.out.println("Suppresion du livre : " + id);

                try {
                    deleteBook(id);
                    sendResponse(exchange,200,"livre supprimé");
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

    private String convertBooksToJson(String lastname,String title, String subject, String price) throws SQLException {
        if(lastname.isEmpty() && title.isEmpty() && subject.isEmpty() && price=="0")
        {
            books = bookDAO.getAllBooks();
        }
        else
        {
            BookSearchVM b = new BookSearchVM(lastname,title,subject,Double.parseDouble(price));
            books = bookDAO.serchBooks(b);
        }
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
    private void deleteBook(int id) throws SQLException {
        bookDAO.deleteBookById(id);
    }
}






