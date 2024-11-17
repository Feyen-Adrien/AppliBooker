package ProtocoleBSPP;

import ServeurTCP.*;
import model.dao.AuthorDAO;
import model.dao.BookDAO;
import model.dao.ConnectDB;
import model.dao.SubjectDAO;

import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static model.dao.ConnectDB.close;

public class BSPP implements Protocole {
    private Logger logger;
    private ConnectDB connexion;

    public BSPP(Logger log) {
        logger = log;
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

        if(requete instanceof RequeteRECHERCHER) return TraiteRequeteRECHERCHER((RequeteRECHERCHER)requete, socket);

        if(requete instanceof RequeteGetAuteur) return TraiteRequeteGetAuteur();

        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket)
    {
        logger.Trace("Requete LOGIN reçue de " + requete.getUserName());

        connexion = new ConnectDB();
        PreparedStatement stmt = null;
        ResultSet res = null;

        try {
            // Préparer la requête SQL pour vérifier les informations de l'utilisateur
            String sql = "SELECT * FROM clients WHERE name = ? AND firstname = ?";
            stmt = connexion.getConnection().prepareStatement(sql);
            stmt.setString(1, requete.getUserName());
            stmt.setString(2, requete.getFirstName());

            res = stmt.executeQuery();

            if (res.next()) {
                // Si un utilisateur correspondant est trouvé, il est connecté avec succès
                String ipPortClient = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                logger.Trace(requete.getUserName() + " est correctement loggé au serveur (ip:port) : " + ipPortClient);

                return new ReponseLOGIN(res.getInt("clientNr"));
            } else {
                // Aucun utilisateur correspondant trouvé
                logger.Trace(requete.getUserName() + " : mauvais identifiants.");
                return new ReponseLOGIN(-1); // Répondre avec échec
            }
        } catch (SQLException e) {
            logger.Trace("Erreur SQL lors de la vérification de l'utilisateur : " + e.getMessage());
            return new ReponseLOGIN(-1);
        } finally {
            // Nettoyer les ressources pour éviter les fuites
            try {
                if (res != null) res.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                logger.Trace("Erreur lors de la fermeture des ressources SQL : " + e.getMessage());
            }
        }
    }

    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete, Socket socket)
    {
        logger.Trace("Requete LOGOUT reçue de " + requete.getUserName());
        logger.Trace(requete.getUserName() + " correctement déconnecté du serveur");
    }

    private synchronized ReponseINSCRIPTION TraiteRequeteINSCRIPTION(RequeteINSCRIPTION requete, Socket socket)
    {
        connexion = new ConnectDB();// connexion BD
        PreparedStatement stmt, pstmt;
        ResultSet res,res2;
        int ide;

        try
        {
            String sql = "SELECT name,firstname  FROM clients WHERE name = ? AND firstname = ?";
            stmt = connexion.getConnection().prepareStatement(sql);
            stmt.setString(1, requete.getUserName());
            stmt.setString(2, requete.getFirstName());
            res = stmt.executeQuery();
            if(!res.next())
            {
                sql = "INSERT INTO clients (name,firstname ) VALUES (?,?)";
                pstmt = connexion.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, requete.getUserName());
                pstmt.setString(2, requete.getFirstName());
                pstmt.executeUpdate();
                res2 = pstmt.getGeneratedKeys();
                res2.next();
                System.out.println(res2.getInt(1));
                ide = res2.getInt(1);
                pstmt.close();
                sql = "UPDATE clients SET clientNr = ? WHERE id = ?";
                pstmt = connexion.getConnection().prepareStatement(sql);
                pstmt.setInt(1, ide+1000);
                pstmt.setInt(2, ide);
                pstmt.executeUpdate();
                pstmt.close();
                stmt.close();
                logger.Trace("Client crée ! ");
                close();//ferme la connection à la BD
                return new ReponseINSCRIPTION(ide+1000);
            }
            else {
                logger.Trace("Client existe déjà !");
                close();
                return new ReponseINSCRIPTION(-1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized ReponseRECHERCHER TraiteRequeteRECHERCHER(RequeteRECHERCHER requete, Socket socket)
    {
        // filtre ici
        AuthorDAO authorDAO;
        BookDAO bookDAO;
        SubjectDAO subjectDAO;
        return null;
    }

    private synchronized ReponseGetAuteur TraiteRequeteGetAuteur()
    {
        AuthorDAO authorDAO;

        ReponseGetAuteur reponseGetAuteur = new ReponseGetAuteur();

        authorDAO = AuthorDAO.getInstance();

        try {
            reponseGetAuteur.setListeAuteur(authorDAO.getAllAuthors());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reponseGetAuteur;
    }
}
