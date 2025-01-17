package ServeurTCP;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class ThreadServeur extends Thread{
    protected int port;
    protected Protocole protocole;
    protected Logger logger;

    protected ServerSocket serverSocket;

    public ThreadServeur(int port, Protocole protocole, Logger logger) throws IOException {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom()+ ")");
        this.port = port;
        this.protocole = protocole;
        this.logger = logger;


        serverSocket = new ServerSocket(port);
    }
}
