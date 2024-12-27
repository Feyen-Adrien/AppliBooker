package ServeurTCP;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadServeurPool extends ThreadServeur {
    private FileAttente connexionsEnAttente;
    private ThreadGroup pool;
    private int taillePool;

    public ThreadServeurPool(int port, Protocole protocole,int taillePool, Logger logger) throws IOException {
        super(port, protocole, logger);

        connexionsEnAttente = new FileAttente();
        this.taillePool = taillePool;
        pool = new ThreadGroup("POOL");
    }

    @Override
    public void run() {
        logger.Trace("Le serveur(pool) démarre...");
        logger.Trace(Thread.currentThread().getName() + ": adresse ip = " + serverSocket.getInetAddress().getHostAddress() + "Pool = " + taillePool);

        try {
            for (int i = 0; i < taillePool; i++) {
                new ThreadClientPool(protocole,connexionsEnAttente,pool,logger).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (!this.isInterrupted()) {
            Socket csocket;
            try
            {
                serverSocket.setSoTimeout(2000);
                csocket = serverSocket.accept();
                logger.Trace("Connexion acceptée, mise en file d'attente");
                connexionsEnAttente.addConnexion(csocket);
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
        pool.interrupt();
    }
}
