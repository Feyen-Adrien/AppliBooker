package ProtocoleBSPP;

import ServeurTCP.*;
import model.dao.*;
import model.entity.Book;
import model.entity.Caddies;
import model.entity.CaddyItems;

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

        if(requete instanceof RequeteRECHERCHER) return TraiteRequeteRECHERCHER((RequeteRECHERCHER)requete);

        if(requete instanceof RequeteGetAuteur) return TraiteRequeteGetAuteur();

        if(requete instanceof RequeteGetBooks) return TraiteRequeteGetBooks();

        if(requete instanceof RequeteGetSubjects) return TraiteRequeteGetSubjects();

        if(requete instanceof RequeteADD_CADDY_ITEM) return TraiteRequeteADD_CADDY_ITEM((RequeteADD_CADDY_ITEM) requete);

        if(requete instanceof  RequeteGetCaddy) return  TraiteRequeteGetCaddy((RequeteGetCaddy) requete);

        if(requete instanceof RequeteDELETE_CADDY_ITEM) return TraiteRequeteDeleteCaddyItem((RequeteDELETE_CADDY_ITEM) requete);

        if(requete instanceof RequeteUPDATE_CADDY_PAYED) return TraiteRequeteUpdateCaddyItemPayed((RequeteUPDATE_CADDY_PAYED) requete);

        if(requete instanceof RequeteDELETE_CADDY) return TraiteRequeteDeleteCaddy((RequeteDELETE_CADDY) requete);

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
        logger.Trace("Requete Inscritpion reçue de " + requete.getUserName());
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

    private synchronized ReponseRECHERCHER TraiteRequeteRECHERCHER(RequeteRECHERCHER requete)
    {
        // filtre ici
        BookDAO bookDAO;
        ReponseRECHERCHER reponseRECHERCHER = new ReponseRECHERCHER();
        bookDAO = BookDAO.getInstance();

        logger.Trace("Requête rechercher reçue");

        try {
            reponseRECHERCHER.setBooks(bookDAO.serchBooks(requete.getBookSearchVM()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return reponseRECHERCHER;
    }

    private synchronized ReponseGetAuteur TraiteRequeteGetAuteur()
    {
        AuthorDAO authorDAO;

        ReponseGetAuteur reponseGetAuteur = new ReponseGetAuteur();

        authorDAO = AuthorDAO.getInstance();

        logger.Trace("Requete GetAuthors Reçue");

        try {
            reponseGetAuteur.setListeAuteur(authorDAO.getAllAuthors());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reponseGetAuteur;
    }
    private synchronized ReponseGetBooks TraiteRequeteGetBooks()
    {
        BookDAO bookDAO;

        ReponseGetBooks reponseGetBooks = new ReponseGetBooks();

        bookDAO = BookDAO.getInstance();

        logger.Trace("Requete GetBooks reçue");

        try {
            reponseGetBooks.setListeLivres(bookDAO.getAllBooks());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reponseGetBooks;
    }
    private synchronized ReponseGetSubjects TraiteRequeteGetSubjects()
    {
        SubjectDAO subjectDAO;

        ReponseGetSubjects reponseGetSubjects = new ReponseGetSubjects();

        subjectDAO = SubjectDAO.getInstance();

        logger.Trace("Requête GetSubjects reçue");

        try {
            reponseGetSubjects.setSubjects(subjectDAO.getAllSubjects());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reponseGetSubjects;
    }

    private synchronized ReponseADD_CADDY_ITEM TraiteRequeteADD_CADDY_ITEM(RequeteADD_CADDY_ITEM requete)
    {
        BookDAO bookDAO;

        bookDAO = BookDAO.getInstance();

        logger.Trace("Requete ADD_CADDY_ITEM reçue");


        try {
            Book book = bookDAO.getBookById(requete.getIdLivre());
            if(book.getStock_quantity()>0)
            {
                CaddiesDAO caddiesDAO = CaddiesDAO.getInstance();
                CaddyItemsDAO caddyItemsDAO = CaddyItemsDAO.getInstance();
                Caddies caddies;
                CaddyItems caddyItems;
                // MAJ livre
                book.setStock_quantity(book.getStock_quantity()-requete.getQuantite());
                bookDAO.updateBook(book);
                // Ajout dans caddy + creation caddy si inexistant
                if(requete.isCaddyCreated()==-1)
                {
                    caddies = new Caddies(null,requete.getIdClient(),null,0,null);
                    caddiesDAO.addCaddies(caddies);
                    logger.Trace("Id caddy créee : " + caddies.getId());
                    caddyItems = new CaddyItems(null,caddies.getId(),requete.getIdLivre(),requete.getQuantite());
                    caddyItemsDAO.addCaddyItems(caddyItems);
                    caddiesDAO.updateCaddiesAmount(caddies.getId(),book.getPrice()*requete.getQuantite());

                    return new ReponseADD_CADDY_ITEM(caddies.getId());
                }
                else
                {
                    caddyItems = new CaddyItems(null,requete.isCaddyCreated(),requete.getIdLivre(),requete.getQuantite());
                    caddyItemsDAO.addCaddyItems(caddyItems);
                    caddiesDAO.updateCaddiesAmount(requete.isCaddyCreated(), book.getPrice()*requete.getQuantite());
                    return new ReponseADD_CADDY_ITEM(requete.isCaddyCreated());
                }

            }
            else
            {
                return new ReponseADD_CADDY_ITEM(-2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized ReponseGetCaddy TraiteRequeteGetCaddy(RequeteGetCaddy requete)
    {
        CaddyItemsDAO caddyItemsDAO;
        CaddiesDAO caddiesDAO;

        ReponseGetCaddy reponseGetCaddy = new ReponseGetCaddy();


        caddyItemsDAO = CaddyItemsDAO.getInstance();
        caddiesDAO = CaddiesDAO.getInstance();

        logger.Trace("Requête GetCaddy reçue");

        try {
            reponseGetCaddy.setCaddyItems(caddyItemsDAO.getCaddyItems(requete.getNrCaddy()));
            reponseGetCaddy.setMaxAmount(caddiesDAO.getCaddiesById(requete.getNrCaddy()).getAmount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reponseGetCaddy;
    }

    private synchronized ReponseDELETE_CADDY_ITEM TraiteRequeteDeleteCaddyItem(RequeteDELETE_CADDY_ITEM requete)
    {
        CaddyItemsDAO caddyItemsDAO = CaddyItemsDAO.getInstance();
        CaddiesDAO caddiesDAO = CaddiesDAO.getInstance();
        BookDAO bookDAO = BookDAO.getInstance();
        ReponseDELETE_CADDY_ITEM reponseDELETECaddyItem = new ReponseDELETE_CADDY_ITEM();

        logger.Trace("Requête DeleteCaddyItem reçue");
        try {
            // misa à jour du livre
            CaddyItems caddyItems = caddyItemsDAO.getCaddyItemsbyId(requete.getIdItemCaddy());
            Book book = bookDAO.getBookById(caddyItems.getBookId());
            book.setStock_quantity(book.getStock_quantity()+caddyItems.getQuantity());
            bookDAO.updateBook(book);
            //Mise à jour caddy items
            caddyItemsDAO.deleteCaddyItems(caddyItems.getId());
            // Mise à jour caddy
            float montantARetirer = (caddyItems.getQuantity()*book.getPrice())*-1;
            caddiesDAO.updateCaddiesAmount(caddyItems.getCaddyId(), montantARetirer);
            reponseDELETECaddyItem.setValid(true);
        } catch (SQLException e) {
            logger.Trace("Error lors du delete caddyItem : " + e.getMessage());
            reponseDELETECaddyItem.setValid(false);
        }
        return reponseDELETECaddyItem;
    }

    private synchronized ReponseUPDATE_CADDY_PAYED TraiteRequeteUpdateCaddyItemPayed(RequeteUPDATE_CADDY_PAYED requete)
    {
        CaddiesDAO caddiesDAO = CaddiesDAO.getInstance();
        ReponseUPDATE_CADDY_PAYED reponseUPDATECaddyPayed = new ReponseUPDATE_CADDY_PAYED();

        logger.Trace("Requête UpdateCaddyItemPayed reçue");
        try
        {
            caddiesDAO.updateCaddiesPayed(requete.getIdCaddy());
            reponseUPDATECaddyPayed.setValide(true);
        } catch (SQLException e) {
            logger.Trace("Error lors du UpdateCaddyItemPayed : " + e.getMessage());
            reponseUPDATECaddyPayed.setValide(false);
        }
        return reponseUPDATECaddyPayed;
    }

    private synchronized ReponseDELETE_CADDY TraiteRequeteDeleteCaddy(RequeteDELETE_CADDY requete)
    {
        CaddiesDAO caddiesDAO = CaddiesDAO.getInstance();
        ReponseDELETE_CADDY reponseDELETECaddy = new ReponseDELETE_CADDY();

        logger.Trace("Requête DeleteCaddy reçue");
        try
        {
            caddiesDAO.deleteCaddies(requete.getIdCaddy());
            reponseDELETECaddy.setValid(true);
        } catch (SQLException e) {
            logger.Trace("Error lors du DeleteCaddy : " + e.getMessage());
            reponseDELETECaddy.setValid(false);
        }
        return reponseDELETECaddy;
    }

}
