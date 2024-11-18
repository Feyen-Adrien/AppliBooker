package src.Helpers;

import javax.swing.*;
import java.io.IOException;

public class BookManager {
    private ServerConnection serverConnection;

    public BookManager(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public String getBooks() {
        try {
            String request = RequestBuilder.buildGetBooksRequest();
            serverConnection.sendRequest(request);
            String response = serverConnection.receiveRequest();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur Get Book");
            return null;
        }
    }

    public int addBook(String title, String ISBN, int pages, double price, int publishyear, int stock, String author, String subject) {
        try {
            String request = RequestBuilder.buildAddBookRequest(title, ISBN, pages, price, publishyear, stock, author, subject);
            serverConnection.sendRequest(request);
            String response = serverConnection.receiveRequest();
            JOptionPane.showMessageDialog(null, response);
            String[] elements = response.split("#");
            return Integer.parseInt(elements[2]);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur Add Book");
        }
        return 0;
    }
}
