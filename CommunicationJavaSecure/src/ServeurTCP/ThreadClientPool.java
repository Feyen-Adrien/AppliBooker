package ServeurTCP;

import java.io.IOException;

public class ThreadClientPool extends ThreadClient {

    private  FileAttente connexionEnAttente;

    public ThreadClientPool(Protocole protocole, FileAttente file,ThreadGroup groupe, Logger logger) throws IOException {
        super(protocole, groupe, logger);
        connexionEnAttente = file;
    }
    @Override
    public void run() {
        logger.Trace("TH client(pool) démarre");
        logger.Trace(Thread.currentThread().getName());
        boolean interrompu = false;
        while (!interrompu) {
            try {
                logger.Trace("Attente d'une connexion...");
                csocket = connexionEnAttente.getConnexion();
                super.run();
            }
            catch (InterruptedException e)
            {
                logger.Trace("Demande d'interruption...");
                interrompu = true;
            }
        }

        logger.Trace("TH client(pool) se termine");
    }
}
