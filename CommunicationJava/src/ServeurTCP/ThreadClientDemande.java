package ServeurTCP;

import java.io.IOException;
import java.net.Socket;

public class ThreadClientDemande extends ThreadClient {
    public ThreadClientDemande(Protocole protocole, Socket csocket, Logger logger) throws IOException {
        super(protocole, csocket, logger);
    }
    @Override
    public void run() {
        logger.Trace("TH client démarre");
        logger.Trace(Thread.currentThread().getName());
        super.run();
        logger.Trace("TH client se termine");
    }
}
