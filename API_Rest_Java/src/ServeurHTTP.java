import Handler.*;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServeurHTTP {
    public static void main(String[] args) {

        HttpServer serveur = null;
        HttpServer serveur2 = null;
        try
        {
            serveur = HttpServer.create(new InetSocketAddress(8080),0);
            serveur2 = HttpServer.create(new InetSocketAddress(8081),0);
            serveur.createContext("/",new RootHandler());
            serveur.createContext("/books",new BooksHandler());
            serveur.createContext("/authors", new AuthorsHandler());
            serveur.createContext("/subjects", new SubjectsHandler());
            serveur2.createContext("/books", new APIBooksHandler());
            serveur2.createContext("/authors", new APIAuthorsHandler());
            serveur2.createContext("/subjects", new APISubjectsHandler());
            System.out.println("Demarrage du serveur HTTP...");
            serveur.start();
            serveur2.start();
        }
        catch (IOException e)
        {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}