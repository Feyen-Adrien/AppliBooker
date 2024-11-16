package ProtocoleBSPP;

import ServeurTCP.*;
import model.dao.ConnectDB;

import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class BSPP implements Protocole {
    private Logger logger;
    private HashMap<String, Socket> clientsConnected;
    private HashMap<String, String> NomPrenom;
    ConnectDB connexion;

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

        if(requete instanceof RequeteINSCRIPTION) return TraiteRequeteINSCRIPTION((RequeteINSCRIPTION)requete, socket);

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

    private synchronized ReponseINSCRIPTION TraiteRequeteINSCRIPTION(RequeteINSCRIPTION requete, Socket socket)
    {
        connexion = new ConnectDB();// connexion BD
        PreparedStatement stmt;
        ResultSet res;
        int ide = 1000;
        String sql = "SELECT name,fisrtname  FROM clients WHERE name = " + requete.getUserName() +"AND firstname = " + requete.getFirstName();

        try
        {
            stmt = connexion.getConnection().prepareStatement(sql);
            res = stmt.executeQuery();
            stmt.close();
            if(!res.next())
            {
                sql = "INSERT INTO clients (name,fisrtname ) VALUES (?,?)";
                stmt = connexion.getConnection().prepareStatement(sql);
                stmt.setString(1, requete.getUserName());
                stmt.setString(2, requete.getFirstName());
                stmt.executeUpdate();
                res = stmt.getGeneratedKeys();
                stmt.close();
                res.next();
                ide += res.getInt(1);
                sql = "UPDATE clients SET clientNr = ? WHERE id = ?";
                stmt = connexion.getConnection().prepareStatement(sql);
                stmt.setInt(1, res.getInt(1));
                stmt.setInt(2, ide);
                stmt.executeUpdate();
                stmt.close();
                logger.Trace("Client crée ! ");
                return new ReponseINSCRIPTION(ide);
            }
            else {
                logger.Trace("Client existe déjà !");
                return new ReponseINSCRIPTION(-1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
