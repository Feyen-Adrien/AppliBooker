package ServeurTCP;

import java.net.Socket;
import java.util.LinkedList;

public class FileAttente {
    private final LinkedList<Socket> fileAttente; // permet de créer un file d'attente avec les sockets

    public FileAttente() {
        fileAttente = new LinkedList<>();
    }
    public synchronized void addConnexion(Socket s) {
        fileAttente.addLast(s);
        notify();
    }
    public synchronized Socket getConnexion() throws InterruptedException {
        while (fileAttente.isEmpty()) {
            wait();
        }
        return fileAttente.remove();
    }
}
