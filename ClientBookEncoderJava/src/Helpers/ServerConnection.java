package Helpers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public ServerConnection(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected to " + host + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(String request) throws IOException {
        byte[] requestBytes = request.getBytes("UTF-8");
        int taille = requestBytes.length;
        out.writeInt(taille);
        out.write(requestBytes);
        out.flush();
    }

    public String receiveRequest() throws IOException {
        byte[] tailleBytes = new byte[4];
        in.readFully(tailleBytes);

        // Convertir les 4 octets en un entier en "little-endian"
        int taille = ((tailleBytes[3] & 0xFF) << 24) |
                ((tailleBytes[2] & 0xFF) << 16) |
                ((tailleBytes[1] & 0xFF) << 8) |
                (tailleBytes[0] & 0xFF);

        System.out.println("Taille lue (corrigée pour little-endian) : " + taille);

        // Vérifier que la taille est valide
        if (taille <= 0 || taille > 1000000) {  // Limite arbitraire pour éviter les tailles excessives
            System.out.println("Erreur : Taille de la requête invalide.");
            return null;
        }

        byte[] requestBytes = new byte[taille];
        in.readFully(requestBytes);
        System.out.println("Données reçues (String) : " + new String(requestBytes, "UTF-8"));

        return new String(requestBytes, "UTF-8");
    }

    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (socket != null) {
            socket.close();
        }
        System.out.println("Connection closed.");
    }
}
