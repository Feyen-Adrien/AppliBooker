package ProtocoleBSPP;

import ServeurTCP.*;

import java.net.Socket;
import java.util.HashMap;

public class BSPP implements Protocole {
    private Logger logger;
    private HashMap<String, Socket> clientsConnected;
    private HashMap<String, String> NomPrenom;

    public BSPP(Logger log) {
        logger = log;
        NomPrenom = new HashMap<>();
        clientsConnected = new HashMap<>();
        NomPrenom.put("Adrien","Feyen");
    }

    @Override
    public String getNom()
    {
        return "BSPP";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete,Socket socket)
    {
        if(requete instanceof RequeteLOGIN) return TraiteRequeteLOGIN((RequeteLOGIN)requete,socket);

        if(requete instanceof RequeteLOGOUT) TraiteRequeteLOGOUT((RequeteLOGOUT)requete, socket);

        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket)
    {
       logger.Trace("Requete LOGIN reçue de " + requete.getUserName());
       // accès BD ici normalement
        String Nom = NomPrenom.get(requete.getUserName());
        if(Nom!=null)
        {
            if(Nom.equals(requete.getFirstName()))
            {
                String ipPortClient = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                logger.Trace(requete.getUserName() + "est correctement loggé au serveur(ip:port) : " + ipPortClient);
                clientsConnected.put(requete.getUserName(), socket);
                return new ReponseLOGIN(true);
            }
        }

        logger.Trace(requete.getUserName() + " : mauvais mdp");
        return new ReponseLOGIN(false);
    }

    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete, Socket socket)
    {
        logger.Trace("Requete LOGOUT reçue de " + requete.getUserName());
        if(clientsConnected.remove(requete.getUserName(), socket))
        {
            logger.Trace(requete.getUserName() + " correctement déconnecté du serveur");
        }
        else
        {
            logger.Trace("Erreur deconnexion coté serveur");
        }
    }
}
