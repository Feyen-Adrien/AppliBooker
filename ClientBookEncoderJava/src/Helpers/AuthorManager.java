package src.Helpers;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorManager {
    private ServerConnection serverConnection;

    public AuthorManager(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public ArrayList<String> getAuthors() {
        ArrayList<String> authors = new ArrayList<>();
        try {
            String request = RequestBuilder.buildGetAuthorsRequest();
            serverConnection.sendRequest(request);
            String response;
            response = serverConnection.receiveRequest();
            if (response.equals("GET_AUTHORS#ok")) {
                while (!(response = serverConnection.receiveRequest()).equals("FINRSQL")) {
                    authors.add(response);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur Get Authors");
        }
        return authors;
    }

    public void addAuthor(String nom, String prenom, String date) {
        try {
            String request = RequestBuilder.buildAddAuthorRequest(nom, prenom, date);
            serverConnection.sendRequest(request);
            String response = serverConnection.receiveRequest();
            JOptionPane.showMessageDialog(null, response);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur Add Author");
        }
    }

    public void LoadAuthors(JComboBox cbAuteur) {
        ArrayList<String> authors = getAuthors();
        cbAuteur.removeAllItems();
        String[] element = null;
        String nomprenom;

        for (String subject : authors) {
            element = subject.split("#");
            nomprenom = element[1] + " " + element[2];
            cbAuteur.addItem(nomprenom);
        }
    }
}
