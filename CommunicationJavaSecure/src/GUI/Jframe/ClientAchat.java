package GUI.Jframe;

import Crypto.KeysMaker;
import Crypto.MyCrypto;
import GUI.JDialog.*;
import ProtocoleBSPPS.*;
import model.entity.Book;
import model.entity.CaddyItems;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.ArrayList;
import java.util.Properties;

public class ClientAchat extends JFrame {
    private Socket socket;
    private String nom;
    private String prenom;
    private int NrClient=-1;
    private int idCaddy=-1;
    private int emplKey=-1;
    private String Auteur = " ";
    private String Titre = " ";
    private String Sujet =" ";
    private double Prix = 0.0;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private JButton rechercherButton;
    private JButton ajouterButton;
    private JSpinner quantiteSpinner;
    private JButton filtreButton;
    private JButton viderLePanierButton;
    private JButton supprimerButton;
    private JButton annulerButton;
    private JButton payerButton;
    private JPanel listeLivrePanel;
    private JPanel PanierPanel;
    private JPanel clienAchatPanel;
    private JButton menuButton;
    private JLabel prixTotalLabel;
    private JLabel listeLivresLabel;
    private JLabel quantiteLabel;
    private JLabel panierLabel;
    private JTextField prixTotalTextField;
    private JScrollPane listeLivreScrollPanel;
    private JToolBar clientToolBar;
    private JTable PanierTable;
    private JTable TableLivre;
    private JButton resetButton;
    private JPopupMenu popupMenu;
    private JMenuItem itemConnexion;
    private JMenuItem itemInscription;
    private JMenuItem itemDeconnexion;
    private JInscription inscription;
    private ConnexionClient connexion;
    private DefaultTableModel modele;
    private DefaultTableModel modeleCaddy;
    private ArrayList<Book> ListeLivres;
    private ArrayList<CaddyItems> Caddyitems;

    private PrivateKey privateKey;
    private PublicKey publicKeyServeur;
    private SecretKey secretKey;
    private PublicKey publicKey;

    public ClientAchat() {
        // configuration du Jframe
        super("Achat Client");
        setContentPane(clienAchatPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);

        // Configuration du Menu
        JPopMenuConfiguration();

        // Apparait déconnecté
        disconnected();
        //Configuration des bouttons pour le client
        // Menu affichage
        menuButton.addActionListener(e -> popupMenu.show(this,33,56));
        // Inscription
        /*itemInscription.addActionListener(e1 -> {
            inscription  = new JInscription();
            inscription.getButtonInscription().addActionListener(e3->{
                if(inscription.getNom().isEmpty())
                {
                    Error("Veuillez entrer un nom");
                }
                else
                {
                    if(inscription.getPrenom().isEmpty())
                    {
                        Error("Veuillez entrer un prenom");
                    }
                    else
                    {
                        try
                        {
                            connexionServeur();
                            RequeteINSCRIPTION requete = new RequeteINSCRIPTION(inscription.getNom(),inscription.getPrenom());
                            out.writeObject(requete);
                            ReponseINSCRIPTION reponse = (ReponseINSCRIPTION) in.readObject();
                            if(reponse.getNrClient()!= -1)
                            {
                                setNom(inscription.getNom());
                                setPrenom(inscription.getPrenom());
                                NrClient = reponse.getNrClient();
                                Succes("Inscription réussie ! Numéro client = " + NrClient);
                                inscription.dispose();
                                connected(); // active les bouttons
                                majListeLivres();
                            }
                            else
                            {
                                Error("Le client existe déjà");
                                deconnexionServeur();
                            }
                        } catch (ClassNotFoundException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            inscription.setVisible(true);
        });*/
        // Connexion
        itemConnexion.addActionListener(e1 -> {
            try {
                connexion = new ConnexionClient();
                connexion.getButtonConnexion().addActionListener(e2 ->{
                    if(connexion.getNom().isEmpty())
                    {
                        Error("Veuillez entrer un nom");
                    }
                    else
                    {
                        if(connexion.getPrenom().isEmpty())
                        {
                            Error("Veuillez entrer un prenom !");
                        }
                        else
                        {
                            if(connexion.getNrClient().isEmpty())
                            {
                                Error("Veuillez entrer un numéro de client !");
                            }
                            try
                            {
                                connexionServeur();
                                RequeteLOGIN requete = new RequeteLOGIN(connexion.getNom(),connexion.getPrenom(),null,null,null);
                                out.writeObject(requete);
                                ReponseLOGIN reponse = (ReponseLOGIN) in.readObject();
                                Security.addProvider(new BouncyCastleProvider());
                                if(reponse.isValide())
                                {
                                    // création du digest
                                    MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
                                    md.update(connexion.getNom().getBytes());
                                    md.update(connexion.getPrenom().getBytes());
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    DataOutputStream dos = new DataOutputStream(baos);
                                    dos.writeLong(reponse.getDate());
                                    dos.writeDouble(reponse.getAlea());
                                    dos.writeInt(Integer.parseInt(connexion.getNrClient()));
                                    md.update(baos.toByteArray());
                                    byte[] digest = md.digest();
                                    out.writeObject(new RequeteLOGIN(connexion.getNom(),connexion.getPrenom(),digest,null,null));
                                    ReponseLOGIN reponse2 = (ReponseLOGIN) in.readObject();
                                    if(reponse2.isValide())
                                    {
                                        setNom(connexion.getNom());
                                        setPrenom(connexion.getPrenom());
                                        NrClient = Integer.parseInt(connexion.getNrClient());
                                        emplKey = reponse2.getEmplSecretKey();// pour savoir où est la clé secrete dans l'array list serveur
                                        // création clé publiques/privées
                                        KeysMaker km = new KeysMaker();
                                        publicKey = km.getPublicKey();
                                        privateKey = km.getPrivateKey();
                                        publicKeyServeur = reponse2.getPublicKey();
                                        secretKey = MyCrypto.generate3DESKey();// création d'une clé de session
                                        out.writeObject(new RequeteLOGIN(nom,prenom,null,MyCrypto.CryptAsymRSA(publicKeyServeur,secretKey.getEncoded()),publicKey));// envoie de la clé de session
                                        ReponseLOGIN reponse3 = (ReponseLOGIN) in.readObject();
                                        if(reponse3.isValide())
                                        {
                                            System.out.println("Clé =" + secretKey);
                                            Succes("Connexion réussie");
                                            connexion.dispose();
                                            connected(); // active les bouttons
                                            majListeLivres();
                                        }
                                        else
                                        {
                                            Error("Erreur lors de la création de la clé de session");
                                        }
                                    }
                                    else {
                                        Error("Numéro de client incorrect !");
                                        deconnexionServeur();
                                    }
                                }
                                else {
                                    Error("Nom ou prénom incorrect !");
                                    deconnexionServeur();
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
                connexion.setVisible(true);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Deconnexion
        itemDeconnexion.addActionListener(e1 -> {
            if(idCaddy != -1)
            {
                viderCaddy();
                annulerCaddy();
            }
            disconnected();
        });
        // Reset
        resetButton.addActionListener(e -> {
            Auteur = " ";
            Titre = " ";
            Sujet = " ";
            Prix = 0.0;
            majListeLivres();
        });
        // Filtre
        filtreButton.addActionListener(e -> {
            Filtre filtre = new Filtre(in,out);
            filtre.getComboBoxAuteur().setSelectedItem(Auteur);
            filtre.getComboBoxTitre().setSelectedItem(Titre);
            filtre.getComboBoxSujet().setSelectedItem(Sujet);
            filtre.getSpinner1().setValue(Prix);
            filtre.getButtonOK().addActionListener(e1 -> {
                Auteur = filtre.getComboBoxAuteur().getSelectedItem().toString();
                Titre = filtre.getComboBoxTitre().getSelectedItem().toString();
                Sujet = filtre.getComboBoxSujet().getSelectedItem().toString();
                Prix = (double)filtre.getSpinner1().getValue();

                filtre.dispose();
            });
            filtre.setVisible(true);
        });
        // Rechercher
        rechercherButton.addActionListener(e -> {
            majListeLivres();
        });
        // Ajouter
        ajouterButton.addActionListener(e->{
            ajouterCaddyItem();
        });
        // Supprimer
        supprimerButton.addActionListener(e -> {
            supprimerCaddyItem();
        });
        // Vider Caddy
        viderLePanierButton.addActionListener(e -> {
            if(idCaddy != -1)
            {
                viderCaddy();
            }
        });
        // Payement caddy
        payerButton.addActionListener(e -> {
            VISAencode visa = new VISAencode();
            visa.getButtonOK().addActionListener(e2 ->{
                if(visa.getNomVISA().isEmpty())
                {
                    Error("Veuillez entrez un nom de VISA !");
                }
                else
                {
                    if(visa.getNumVISA().isEmpty())
                    {
                        Error("Veuillez entrer un numéro de VISA !");
                    }
                    else
                    {
                        payed(visa.getNomVISA(),visa.getNumVISA());
                        visa.dispose();
                        disconnected();
                    }
                }
            });
            visa.setVisible(true);

        });
        // Annulation
        annulerButton.addActionListener(e -> {
            if(idCaddy != -1)
            {
                viderCaddy();
                annulerCaddy();
            }
            disconnected();
        });
        // Annulation commande quand clique sur croix
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(NrClient != -1)
                {
                    if(idCaddy != -1)
                    {
                        viderCaddy();
                        annulerCaddy();
                    }
                    dispose();
                }
                else
                {
                    dispose();
                }

            }
        });

    }// encore à améliorer !!!!
    // METHODE SPECIFIQUES
    public void connexionServeur()
    {
        // récupération du port et de l'adress ip du serveur
        String filePath = "app.properties";
        Properties prop = new Properties();
        String ipServer;
        String PORT_PAYEMENT_SECURE;
        try(FileInputStream inputStream = new FileInputStream(filePath))
        {
            prop.load(inputStream);
            PORT_PAYEMENT_SECURE = prop.getProperty("PORT_PAYEMENT_SECURE");
            ipServer = prop.getProperty("ipServer");

            System.out.println("AdresseIP : "+ ipServer + ",port : " + PORT_PAYEMENT_SECURE);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try
        {
            socket = new Socket(ipServer,Integer.parseInt(PORT_PAYEMENT_SECURE));
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }
        catch (Exception e)
        {
            Dialog dialog = new ErrorDialog("Erreur connexion au serveur");
            dialog.setVisible(true);
        }
    }
    public void deconnexionServeur()
    {
        try {
            if(NrClient != -1)
            {
                //Logout
                RequeteLOGOUT requeteLOGOUT = new RequeteLOGOUT(getNom());
                out.writeObject(requeteLOGOUT);
                Succes("Client bien déconnecté");
                NrClient = -1;
                idCaddy = -1;
                Auteur = " ";
                Titre = " ";
                Sujet = " ";
                Prix = 0.0;
                modele = new DefaultTableModel();
                if(Caddyitems != null)
                {
                    Caddyitems.clear();
                    PanierTable.setModel(modele);
                }
                ListeLivres.clear();
                TableLivre.setModel(modele);
                prixTotalTextField.setText("0.0");
            }
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void connected()
    {
        resetButton.setEnabled(true);
        filtreButton.setEnabled(true);
        rechercherButton.setEnabled(true);
        ajouterButton.setEnabled(true);
        quantiteSpinner.setEnabled(true);
        viderLePanierButton.setEnabled(true);
        supprimerButton.setEnabled(true);
        annulerButton.setEnabled(true);
        payerButton.setEnabled(true);
        itemConnexion.setEnabled(false);
        //itemInscription.setEnabled(false);
        itemDeconnexion.setEnabled(true);
    }
    public void disconnected()
    {
        resetButton.setEnabled(false);
        filtreButton.setEnabled(false);
        rechercherButton.setEnabled(false);
        ajouterButton.setEnabled(false);
        quantiteSpinner.setEnabled(false);
        viderLePanierButton.setEnabled(false);
        supprimerButton.setEnabled(false);
        annulerButton.setEnabled(false);
        payerButton.setEnabled(false);
        itemDeconnexion.setEnabled(false);
        itemConnexion.setEnabled(true);
        //itemInscription.setEnabled(true);
        if(NrClient != -1)
        {
            deconnexionServeur();
        }
    }
    public void JPopMenuConfiguration()
    {
        // configuration du JPopupMenu
        popupMenu = new JPopupMenu();

        itemConnexion = new JMenuItem("Connexion");
        //itemInscription = new JMenuItem("Inscription");
        itemDeconnexion = new JMenuItem("Deconnexion");
        popupMenu.add(itemConnexion);
        //popupMenu.add(itemInscription);
        popupMenu.add(itemDeconnexion);

        // permet de colorer en bleu l'item du menu quand la souris est dessus
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                source.setBackground(new Color(255, 255, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                source.setBackground(UIManager.getColor("MenuItem.background"));
            }
        };
        // ajout du listener aux bouttons
        itemDeconnexion.addMouseListener(mouseAdapter);
        //itemInscription.addMouseListener(mouseAdapter);
        itemConnexion.addMouseListener(mouseAdapter);
    }
    public void majListeLivres()
    {
        try
        {
            String nomAuteur;
            if(getAuteur().equals(" "))
            {
                nomAuteur = " ";
            }
            else {
                String[] part = getAuteur().split(" ");
                nomAuteur= part[0];
            }
            RequeteRECHERCHER requeteRECHERCHER = new RequeteRECHERCHER(nomAuteur,getTitre(),getSujet(),getPrix());
            out.writeObject(requeteRECHERCHER);
            ReponseRECHERCHER reponseRECHERCHER;
            reponseRECHERCHER = (ReponseRECHERCHER) in.readObject();
            String[] colonnes = {"Auteur", "Sujet", "Titre","ISBN","Page","Stock","Prix","Année de publication"};
            modele = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            modele.setColumnIdentifiers(colonnes);
            ListeLivres = new ArrayList<>(reponseRECHERCHER.getBooks());
            for(int i =0;i<ListeLivres.size();i++)
            {
                modele.addRow(new Object[]{
                        ListeLivres.get(i).getNomAuteur()+ " " + ListeLivres.get(i).getPrenomAuteur(),
                        ListeLivres.get(i).getNomSujet(),
                        ListeLivres.get(i).getTitle(),
                        ListeLivres.get(i).getIsbn(),
                        ListeLivres.get(i).getPage_count(),
                        ListeLivres.get(i).getStock_quantity(),
                        ListeLivres.get(i).getPrice(),
                        ListeLivres.get(i).getPublish_year()
                });
            }
            TableLivre.setModel(modele);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public void majCaddy()
    {
        try {
            RequeteGetCaddy requeteGetCaddy = new RequeteGetCaddy(idCaddy);
            out.writeObject(requeteGetCaddy);
            ReponseGetCaddy reponseGetCaddy;
            reponseGetCaddy = (ReponseGetCaddy) in.readObject();
            String[] colonnes = {"Auteur","Titre","Prix","Quantité"};
            modeleCaddy = new DefaultTableModel()
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            modeleCaddy.setColumnIdentifiers(colonnes);
            Caddyitems = new ArrayList<>(reponseGetCaddy.getCaddyItems());
            for(int i =0;i<reponseGetCaddy.getCaddyItems().size();i++)
            {
                Book b = null;
                for(Book book : ListeLivres)
                {
                    if(book.getId().equals(reponseGetCaddy.getCaddyItems().get(i).getBookId()))
                    {
                        b = book;
                    }
                }
                if(b == null)
                {
                    Error("Erreur lors de la maj caddy !");
                }
                modeleCaddy.addRow(new Object[]{
                        b.getNomAuteur() + " " + b.getPrenomAuteur(),
                        b.getTitle(),
                        b.getPrice(),
                        reponseGetCaddy.getCaddyItems().get(i).getQuantity()
                });
            }
            prixTotalTextField.setText(String.valueOf(reponseGetCaddy.getMaxAmount()));
            PanierTable.setModel(modeleCaddy);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public void ajouterCaddyItem()
    {
        try {
            int qte = (int)quantiteSpinner.getValue();
            if(qte <= 0)
            {
                Error("Veuillez choisir une quantité");
            }
            else
            {
                if(TableLivre.getSelectedRow() ==-1)
                {
                    Error("Veuillez selectionner un livre !");
                }
                else
                {
                    Book book;
                    book = ListeLivres.get(TableLivre.getSelectedRow());

                    RequeteADD_CADDY_ITEM requeteADDCaddyItem = new RequeteADD_CADDY_ITEM(book.getId(),qte,idCaddy,NrClient-1000,secretKey,emplKey);
                    out.writeObject(requeteADDCaddyItem);
                    ReponseADD_CADDY_ITEM reponseADDCaddyItem;
                    reponseADDCaddyItem = (ReponseADD_CADDY_ITEM) in.readObject();
                    if(reponseADDCaddyItem.getNrCaddy()==-2)
                    {
                        Error("il n'y plus assez d'article disponible !");
                    }
                    else
                    {
                        Succes("Article bien ajouté au caddy");
                        idCaddy = reponseADDCaddyItem.getNrCaddy();
                        majCaddy();
                        majListeLivres();
                    }
                }
            }
        } catch (ClassNotFoundException | IOException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
    public void supprimerCaddyItem()
    {
        try {
            if(PanierTable.getSelectedRow() ==-1)
            {
                Error("Veuillez selectionner un livre !");
            }
            else
            {
                CaddyItems caddyItem;
                caddyItem = Caddyitems.get(PanierTable.getSelectedRow());

                RequeteDELETE_CADDY_ITEM requeteDELETE_caddy_item = new RequeteDELETE_CADDY_ITEM(caddyItem.getId(),secretKey,emplKey);
                out.writeObject(requeteDELETE_caddy_item);
                ReponseDELETE_CADDY_ITEM reponseDELETE_caddy_item;
                reponseDELETE_caddy_item = (ReponseDELETE_CADDY_ITEM) in.readObject();
                if(!reponseDELETE_caddy_item.getValid())
                {
                    Error("Erreur lors de la suppression de l'article du caddy !");
                }
                else
                {
                    Succes("Article bien supprimé du caddy");
                    majCaddy();
                    majListeLivres();
                }
            }
        } catch (ClassNotFoundException | IOException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
    public void viderCaddy()
    {
        try {
            ArrayList<CaddyItems> caddyItems = new ArrayList<>(Caddyitems);

            for (CaddyItems caddyItem : caddyItems) {

                RequeteDELETE_CADDY_ITEM requeteDELETE_caddy_item = new RequeteDELETE_CADDY_ITEM(caddyItem.getId(),secretKey,emplKey);
                out.writeObject(requeteDELETE_caddy_item);
                ReponseDELETE_CADDY_ITEM reponseDELETE_caddy_item;
                reponseDELETE_caddy_item = (ReponseDELETE_CADDY_ITEM) in.readObject();

                if (!reponseDELETE_caddy_item.getValid()) {
                    Error("Erreur lors de la suppression des articles du caddy !");
                    return ;
                }
            }
            Succes("Articles bien supprimés du caddy");
            majCaddy();
            majListeLivres();

        } catch (ClassNotFoundException | IOException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }


    }
    public void payed(String nomVisa, String numVISA)
    {
        try {
            //Flaggé comme payé
            RequeteUPDATE_CADDY_PAYED requeteUPDATECaddyPayed = new RequeteUPDATE_CADDY_PAYED(idCaddy,numVISA,nomVisa,secretKey,privateKey,emplKey);
            out.writeObject(requeteUPDATECaddyPayed);
            ReponseUPDATE_CADDY_PAYED reponseUPDATECaddyPayed;
            reponseUPDATECaddyPayed = (ReponseUPDATE_CADDY_PAYED) in.readObject();
            // Construction du HMAC local
            Mac hm = Mac.getInstance("HMAC-MD5","BC");
            hm.init(secretKey);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeBoolean(reponseUPDATECaddyPayed.isValide());
            hm.update(baos.toByteArray());
            byte[] hmacloc = hm.doFinal();
            // compare le hmac pour voir si les données sont bonnes
            if (!MessageDigest.isEqual(hmacloc,reponseUPDATECaddyPayed.getHmac())) {
                Error("Erreur lors du marquage du caddy comme payé !");
                return;
            }
            Succes("Caddy Payé avec Succès");

            disconnected();
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Erreur lors du paiement : " + e.getMessage());
        } catch (InvalidKeyException | SignatureException | NoSuchPaddingException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }
    void annulerCaddy()
    {
        try
        {
            RequeteDELETE_CADDY requeteDELETECaddy = new RequeteDELETE_CADDY(idCaddy,privateKey,emplKey);
            out.writeObject(requeteDELETECaddy);
            ReponseDELETE_CADDY reponseDELETECaddy;
            reponseDELETECaddy = (ReponseDELETE_CADDY) in.readObject();

            // Construction du HMAC local
            Mac hm = Mac.getInstance("HMAC-MD5","BC");
            hm.init(secretKey);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeBoolean(reponseDELETECaddy.isValid());
            hm.update(baos.toByteArray());
            byte[] hmacloc = hm.doFinal();
            // comparaison des 2 hmac
            if (!MessageDigest.isEqual(hmacloc,reponseDELETECaddy.getHmac())) {
                Error("Erreur lors de la suppression du caddy et des caddyItem !");
                return;
            }
            Succes("Caddy Corectement supprimé");

        } catch (ClassNotFoundException | IOException | InvalidKeyException | NoSuchAlgorithmException |
                 NoSuchProviderException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }
    void Error(String m)
    {
        ErrorDialog dialog = new ErrorDialog(m);
        dialog.pack();
        dialog.setTitle("Erreur");
        dialog.setVisible(true);
    }
    void Succes(String m)
    {
        ValidDialog dialog = new ValidDialog(m);
        dialog.pack();
        dialog.setTitle("Succes");
        dialog.setVisible(true);
    }

    //  SETTERS/GETTERS
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String name) {
        nom = name;
    }
    public String getFirstName() {
        return prenom;
    }
    public void setPrenom(String firstName) {
        this.prenom = firstName;
    }

    public String getAuteur() {
        return Auteur;
    }

    public void setAuteur(String auteur) {
        Auteur = auteur;
    }

    public String getTitre() {
        return Titre;
    }

    public void setTitre(String titre) {
        Titre = titre;
    }

    public String getSujet() {
        return Sujet;
    }

    public void setSujet(String sujet) {
        Sujet = sujet;
    }

    public double getPrix() {
        return Prix;
    }

    public void setPrix(double prix) {
        Prix = prix;
    }

    public DefaultTableModel getModele()
    {
        return modele;
    }

    public DefaultTableModel getModeleCaddy() {
        return modeleCaddy;
    }

}
