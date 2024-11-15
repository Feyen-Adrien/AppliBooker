import GUI.Jframe.ClientAchat;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientBSPP extends JFrame {

    public static void main(String[] args) {
        ClientAchat client = new ClientAchat();
        client.setVisible(true);
    }
}
