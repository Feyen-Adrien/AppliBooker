package ProtocoleBSPPS;

import Crypto.KeysMaker;
import Crypto.MyCrypto;
import ServeurTCP.Logger;
import ServeurTCP.Protocole;
import ServeurTCP.Reponse;
import ServeurTCP.Requete;
import model.dao.*;
import model.entity.Book;
import model.entity.Caddies;
import model.entity.CaddyItems;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static model.dao.ConnectDB.close;

public class BSPPS implements Protocole {
    private Logger logger;
    private ConnectDB connexion;
    // Ajout de la clé privé et publique du serveur
    private KeysMaker k = new KeysMaker();
    private PrivateKey privateKey = k.getPrivateKey();
    private PublicKey publicKey = k.getPublicKey();
    private int emplSecretKey =0;
    private ArrayList<SecretKey> secretKeys = new ArrayList<SecretKey>();// pour contenir les différentes clé de session
    private ArrayList<PublicKey> clientsKeys = new ArrayList<PublicKey>();
    // création du sel
    long time = new Date().getTime();
    double alea = Math.random();

    public BSPPS(Logger log) throws Exception {
        logger = log;
    }

    @Override
    public String getNom()
    {
        return "BSPPS";
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

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket) {
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
                // création du digest
                Security.addProvider(new BouncyCastleProvider());
                if (requete.getPassword() == null && requete.getSecretKey() == null) {
                    return new ReponseLOGIN(time, alea, true);
                } else {
                    if (requete.getPassword() != null && requete.getSecretKey() == null) {
                        // création du digest
                        MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
                        md.update(requete.getUserName().getBytes());
                        md.update(requete.getFirstName().getBytes());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        DataOutputStream dos = new DataOutputStream(baos);
                        dos.writeLong(time);
                        dos.writeDouble(alea);
                        dos.writeInt(res.getInt("clientNr"));
                        md.update(baos.toByteArray());
                        byte[] digestServe = md.digest();
                        if (MessageDigest.isEqual(digestServe, requete.getPassword())) {
                            // Si un utilisateur correspondant est trouvé, il est connecté avec succès
                            String ipPortClient = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                            logger.Trace(requete.getUserName() + " est correctement loggé au serveur (ip:port) : " + ipPortClient);
                            emplSecretKey++;
                            return new ReponseLOGIN(true, publicKey,emplSecretKey-1);

                        } else {
                            return new ReponseLOGIN(false);
                        }
                    } else {
                        if (requete.getPassword() == null && requete.getSecretKey() != null) {
                            SecretKey s=MyCrypto.get3DESKeyFromBytes(MyCrypto.DecryptAsymRSA(privateKey, requete.getSecretKey()));
                            logger.Trace(String.valueOf(s));
                            secretKeys.add(s);
                            clientsKeys.add(requete.getPublicKey());
                            return new ReponseLOGIN(true);
                        }
                        else
                        {
                            return new ReponseLOGIN(false);
                        }
                    }

                }

            } else {
                // Aucun utilisateur correspondant trouvé
                logger.Trace(requete.getUserName() + " : mauvais identifiants.");
                return new ReponseLOGIN(false); // Répondre avec échec
            }
        } catch (SQLException e) {
            logger.Trace("Erreur SQL lors de la vérification de l'utilisateur : " + e.getMessage());
            return new ReponseLOGIN(false);
        } catch (NoSuchProviderException | IOException | NoSuchAlgorithmException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
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
            byte[] messagedecrypte = MyCrypto.DecryptSym3DES(secretKeys.get(requete.getEmplSecretKey()),requete.getData());
            ByteArrayInputStream bais = new ByteArrayInputStream(messagedecrypte);
            DataInputStream dis = new DataInputStream(bais);
            int id = dis.readInt();
            int qte = dis.readInt();
            int caddyCreated = dis.readInt();
            int nrClient = dis.readInt();
            Book book = bookDAO.getBookById(id);
            if(book.getStock_quantity()>0)
            {
                CaddiesDAO caddiesDAO = CaddiesDAO.getInstance();
                CaddyItemsDAO caddyItemsDAO = CaddyItemsDAO.getInstance();
                Caddies caddies;
                CaddyItems caddyItems;
                // MAJ livre
                book.setStock_quantity(book.getStock_quantity()-qte);
                bookDAO.updateBook(book);
                // Ajout dans caddy + creation caddy si inexistant
                if(caddyCreated==-1)
                {
                    caddies = new Caddies(null,nrClient,null,0,null);
                    caddiesDAO.addCaddies(caddies);
                    logger.Trace("Id caddy créee : " + caddies.getId());
                    caddyItems = new CaddyItems(null,caddies.getId(),id,qte);
                    caddyItemsDAO.addCaddyItems(caddyItems);
                    caddiesDAO.updateCaddiesAmount(caddies.getId(),book.getPrice()*qte);

                    return new ReponseADD_CADDY_ITEM(caddies.getId());
                }
                else
                {
                    caddyItems = new CaddyItems(null,caddyCreated,id,qte);
                    caddyItemsDAO.addCaddyItems(caddyItems);
                    caddiesDAO.updateCaddiesAmount(caddyCreated, book.getPrice()*qte);
                    return new ReponseADD_CADDY_ITEM(caddyCreated);
                }

            }
            else
            {
                return new ReponseADD_CADDY_ITEM(-2);
            }
        } catch (SQLException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | NoSuchProviderException | IOException e) {
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
            byte[] messagedecrypte = MyCrypto.DecryptSym3DES(secretKeys.get(requete.getEmplSecretKey()),requete.getData());
            ByteArrayInputStream bais = new ByteArrayInputStream(messagedecrypte);
            DataInputStream dis = new DataInputStream(bais);
            int id = dis.readInt();
            // misa à jour du livre
            CaddyItems caddyItems = caddyItemsDAO.getCaddyItemsbyId(id);
            Book book = bookDAO.getBookById(caddyItems.getBookId());
            book.setStock_quantity(book.getStock_quantity()+caddyItems.getQuantity());
            bookDAO.updateBook(book);
            //Mise à jour caddy items
            caddyItemsDAO.deleteCaddyItems(caddyItems.getId());
            // Mise à jour caddy
            float montantARetirer = (caddyItems.getQuantity()*book.getPrice())*-1;
            caddiesDAO.updateCaddiesAmount(caddyItems.getCaddyId(), montantARetirer);
            reponseDELETECaddyItem.setValid(true);
        } catch (SQLException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | NoSuchProviderException | IOException e) {
            logger.Trace("Error lors du delete caddyItem : " + e.getMessage());
            reponseDELETECaddyItem.setValid(false);
        }
        return reponseDELETECaddyItem;
    }

    private synchronized ReponseUPDATE_CADDY_PAYED TraiteRequeteUpdateCaddyItemPayed(RequeteUPDATE_CADDY_PAYED requete)
    {
        CaddiesDAO caddiesDAO = CaddiesDAO.getInstance();
        ReponseUPDATE_CADDY_PAYED reponseUPDATECaddyPayed;

        logger.Trace("Requête UpdateCaddyItemPayed reçue");
        try
        {
            // récupération données symétriquement
            byte[] messagedecrypte = MyCrypto.DecryptSym3DES(secretKeys.get(requete.getEmplKey()),requete.getData());
            ByteArrayInputStream bais = new ByteArrayInputStream(messagedecrypte);
            DataInputStream dis = new DataInputStream(bais);
            int idCaddy = dis.readInt();
            String numVisa = dis.readUTF();
            String nomVisa = dis.readUTF();

            // vérifie la signature (si elles correspondent) avec les données recupérées
            Security.addProvider(new BouncyCastleProvider());
            Signature s = Signature.getInstance("SHA1withRSA","BC");
            s.initVerify(clientsKeys.get(requete.getEmplKey()));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeInt(idCaddy);
            dos.writeUTF(numVisa);
            dos.writeUTF(nomVisa);
            s.update(bos.toByteArray());
            if(s.verify(requete.getSignature()))
            {
                caddiesDAO.updateCaddiesPayed(idCaddy);
                reponseUPDATECaddyPayed = new ReponseUPDATE_CADDY_PAYED(true, secretKeys.get(requete.getEmplKey()));
            }
            else
            {
                reponseUPDATECaddyPayed =new ReponseUPDATE_CADDY_PAYED();
                reponseUPDATECaddyPayed.setValide(false);
            }

        } catch (SQLException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IOException |
                 SignatureException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            logger.Trace("Error lors du UpdateCaddyItemPayed : " + e.getMessage());
            reponseUPDATECaddyPayed =new ReponseUPDATE_CADDY_PAYED();
            reponseUPDATECaddyPayed.setValide(false);
        }
        return reponseUPDATECaddyPayed;
    }

    private synchronized ReponseDELETE_CADDY TraiteRequeteDeleteCaddy(RequeteDELETE_CADDY requete)
    {
        CaddiesDAO caddiesDAO = CaddiesDAO.getInstance();
        ReponseDELETE_CADDY reponseDELETECaddy;

        logger.Trace("Requête DeleteCaddy reçue");
        try
        {
            // vérifie la signature (si elles correspondent)
            Security.addProvider(new BouncyCastleProvider());
            Signature s = Signature.getInstance("SHA1withRSA","BC");
            s.initVerify(clientsKeys.get(requete.getEmplKey()));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeInt(requete.getIdCaddy());
            s.update(bos.toByteArray());

            if(s.verify(requete.getSignature()))
            {
                logger.Trace("Signature OK ");
                caddiesDAO.deleteCaddies(requete.getIdCaddy());
                reponseDELETECaddy =  new ReponseDELETE_CADDY(true, secretKeys.get(requete.getEmplKey()));

            }
            else
            {
                reponseDELETECaddy = new ReponseDELETE_CADDY();
                reponseDELETECaddy.setValid(false);
            }

        } catch (SQLException | NoSuchAlgorithmException | NoSuchProviderException e) {
            logger.Trace("Error lors du DeleteCaddy : " + e.getMessage());
            reponseDELETECaddy =new ReponseDELETE_CADDY();
            reponseDELETECaddy.setValid(false);
        } catch (InvalidKeyException | IOException | SignatureException e) {
            throw new RuntimeException(e);
        }
        return reponseDELETECaddy;
    }

}
