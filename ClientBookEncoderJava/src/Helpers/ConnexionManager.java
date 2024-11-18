package src.Helpers;

import GUI.JFrame.ClientEncode;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class ConnexionManager {
    private ServerConnection serverConnection;

    public ConnexionManager(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public String[] Login(String username, String password) {
        String[] elements = null;
        try {
            String request = RequestBuilder.buildLoginRequest(username, password);
            serverConnection.sendRequest(request);
            String response;
            response = serverConnection.receiveRequest();
            elements = response.split("#");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur Get Authors");
        }
        return elements;
    }

    public void Logout() {
        try {
            String request = RequestBuilder.buildLogoutRequest();
            serverConnection.sendRequest(request);
            String response;
            response = serverConnection.receiveRequest();
            if (response.equals("LOGOUT#ok")) {
                JOptionPane.showMessageDialog(null, "Logout Ok");
            } else {
                JOptionPane.showMessageDialog(null, "Logout Ko");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
