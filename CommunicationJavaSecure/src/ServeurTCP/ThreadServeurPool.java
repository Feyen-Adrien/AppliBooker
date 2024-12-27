package ServeurTCP;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadServeurPool extends ThreadServeur {
    public ThreadServeurPool(int port, Protocole protocole, Logger logger) throws IOException {
        super(port, protocole, logger);
    }

    @Override
    public void run() {
        logger.Trace("Le serveur démarre...");
        logger.Trace(Thread.currentThread().getName() + ": adresse ip = " + serverSocket.getInetAddress().getHostAddress());

        while (!this.isInterrupted()) {
            Socket csocket;
            try
            {
                serverSocket.setSoTimeout(2000);
                csocket = serverSocket.accept();
                logger.Trace("Connexion acceptée, création du thread client");
                Thread th = new ThreadClientPool(protocole,csocket,logger);
                th.start();
            }
            catch (SocketTimeoutException e)
            {
                logger.Trace("Thread client interrompu");
            }
            catch (IOException e)
            {
                logger.Trace("Erreu I/O");
            }
        }
        logger.Trace("Thread serveur interrompu");
        try{serverSocket.close();}
        catch (IOException e){
            logger.Trace("Erreu I/O");
        }
    }
}
