package GUI.Jframe;

import GUI.JDialog.*;
import ProtocoleBSPP.*;
import model.entity.Book;
import model.entity.CaddyItems;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class ClientAchat extends JFrame {
    private Socket socket;
    private String nom;
    private String prenom;
    private int NrClient=-1;
    private int idCaddy=-1;
    private String Auteur = " ";
    private String Titre = " ";
    private String Sujet =" ";
    private int Prix = 0;
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
    private final JPopupMenu popupMenu;
    private final JMenuItem itemConnexion;
    private final JMenuItem itemInscription;
    private final JMenuItem itemDeconnexion;
    private JInscription inscription;
    private ConnexionClient connexion;
    private DefaultTableModel modele;
    private DefaultTableModel modeleCaddy;
    private ArrayList<Book> ListeLivres;
    private ArrayList<CaddyItems> Caddyitems;

    public ClientAchat() {
        // configuration du Jframe
        super("Achat Client");
        setContentPane(clienAchatPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);

        // Ajout du nom des colonnes à la liste et au pnier
        /*
        modele = new DefaultTableModel(null,new String[]{"Titre", "Auteur", "Quantité","Prix"});
        PanierTable.setModel(modele);*/


        // configuration du JPopupMenu
        popupMenu = new JPopupMenu();

        itemConnexion = new JMenuItem("Connexion");
        itemInscription = new JMenuItem("Inscription");
        itemDeconnexion = new JMenuItem("Deconnexion");
        popupMenu.add(itemConnexion);
        popupMenu.add(itemInscription);
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
        itemInscription.addMouseListener(mouseAdapter);
        itemConnexion.addMouseListener(mouseAdapter);


        // ajout d'action aux buttons du Jpopupmenu
        itemInscription.addActionListener(e1 -> {
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
                                MajListeLivres();
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
        });
        // permet d'afficher une Jdialog pour l'inscription
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
                            try
                            {
                                connexionServeur();
                                RequeteLOGIN requete = new RequeteLOGIN(connexion.getNom(),connexion.getPrenom());
                                out.writeObject(requete);
                                ReponseLOGIN reponse = (ReponseLOGIN) in.readObject();
                                if(reponse.getNrClient() != -1)
                                {
                                    setNom(connexion.getNom());
                                    setPrenom(connexion.getPrenom());
                                    NrClient = reponse.getNrClient();
                                    Succes("Connexion réussi ! Numéro de client = " + NrClient);
                                    connexion.dispose();
                                    connected(); // active les bouttons
                                    MajListeLivres();
                                }
                                else
                                {
                                    Error("Nom ou prénom incorrecte !");
                                    deconnexionServeur();
                                }
                            } catch (ClassNotFoundException | IOException e) {
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
        // permet d'afficher une JDialog pour l connexion
        itemDeconnexion.addActionListener(e1 -> {
            RequeteLOGOUT requete = new RequeteLOGOUT(getNom());
            try {
                out.writeObject(requete);
                Succes("Client bien déconnecté");
                NrClient = -1;
                disconnected();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // vider panier si rempli ...
        });// permet de deconnecter l'utilisateur


        // Désactiver tous les boutons
        disconnected();

        // Button Ajouter Element Caddy
        ajouterButton.addActionListener(e->{
            AjouterCaddy();
        });

        // affichage du filtre
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
                Prix = (int)filtre.getSpinner1().getValue();

                filtre.dispose();
            });
            filtre.setVisible(true);
        });
        rechercherButton.addActionListener(e -> {
            MajListeLivres();
        });

        // affichage menu déroulant
        menuButton.addActionListener(e -> popupMenu.show(this,33,56));
    }
    // METHODE SPECIFIQUES
    public void connexionServeur()
    {
        // récupération du port et de l'adress ip du serveur
        String filePath = "app.properties";
        Properties prop = new Properties();
        String ipServer;
        String PORT_PAYEMENT;
        try(FileInputStream inputStream = new FileInputStream(filePath))
        {
            prop.load(inputStream);
            PORT_PAYEMENT = prop.getProperty("PORT_PAYEMENT");
            ipServer = prop.getProperty("ipServer");

            System.out.println("AdresseIP : "+ ipServer + ",port : " + PORT_PAYEMENT);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try
        {
            socket = new Socket(ipServer,Integer.parseInt(PORT_PAYEMENT));
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
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void connected()
    {
        filtreButton.setEnabled(true);
        rechercherButton.setEnabled(true);
        ajouterButton.setEnabled(true);
        quantiteSpinner.setEnabled(true);
        viderLePanierButton.setEnabled(true);
        supprimerButton.setEnabled(true);
        annulerButton.setEnabled(true);
        payerButton.setEnabled(true);
        itemConnexion.setEnabled(false);
        itemInscription.setEnabled(false);
        itemDeconnexion.setEnabled(true);
    }
    public void disconnected()
    {
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
        itemInscription.setEnabled(true);

    }

    public void MajListeLivres()
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
            RequeteRECHERCHER requeteRECHERCHER = new RequeteRECHERCHER(nomAuteur,getTitre(),getSujet(),((Integer)Prix).floatValue());
            out.writeObject(requeteRECHERCHER);
            ReponseRECHERCHER reponseRECHERCHER;
            reponseRECHERCHER = (ReponseRECHERCHER) in.readObject();
            String[] colonnes = {"Auteur", "Sujet", "Titre","ISBN","Page","Stock","Prix","Année de publication"};
            modele = new DefaultTableModel();
            modele.setColumnIdentifiers(colonnes);
            ListeLivres = new ArrayList<>(reponseRECHERCHER.getBooks());
            for(int i =0;i<reponseRECHERCHER.getBooks().size();i++)
            {
                modele.addRow(new Object[]{
                        reponseRECHERCHER.getBooks().get(i).getNomAuteur()+ " " + reponseRECHERCHER.getBooks().get(i).getPrenomAuteur(),
                        reponseRECHERCHER.getBooks().get(i).getNomSujet(),
                        reponseRECHERCHER.getBooks().get(i).getTitle(),
                        reponseRECHERCHER.getBooks().get(i).getIsbn(),
                        reponseRECHERCHER.getBooks().get(i).getPage_count(),
                        reponseRECHERCHER.getBooks().get(i).getStock_quantity(),
                        reponseRECHERCHER.getBooks().get(i).getPrice(),
                        reponseRECHERCHER.getBooks().get(i).getPublish_year()
                });
            }
            TableLivre.setModel(modele);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public void MajCaddy()
    {
        try {
            RequeteGetCaddy requeteGetCaddy = new RequeteGetCaddy(idCaddy);
            out.writeObject(requeteGetCaddy);
            ReponseGetCaddy reponseGetCaddy;
            reponseGetCaddy = (ReponseGetCaddy) in.readObject();
            String[] colonnes = {"Auteur","Titre","Prix","Quantité"};
            modeleCaddy = new DefaultTableModel();
            modeleCaddy.setColumnIdentifiers(colonnes);
            Caddyitems = new ArrayList<>(reponseGetCaddy.getCaddyItems());
            for(int i =0;i<reponseGetCaddy.getCaddyItems().size();i++)
            {
                Book b = null;
                for(Book book : ListeLivres)
                {
                    if(book.getId().equals(reponseGetCaddy.getCaddyItems().get(i).getId()))
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
                PanierTable.setModel(modeleCaddy);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    public void AjouterCaddy()
    {
        try {
            int qte = (int)quantiteSpinner.getValue();
            if(qte <= 0)
            {
                Error("Veuillez choisir une quatité");
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

                    RequeteADD_CADDY_ITEM requeteADDCaddyItem = new RequeteADD_CADDY_ITEM(book.getId(),qte,idCaddy,NrClient-1000);
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
                        //MajCaddy();
                        MajListeLivres();
                    }
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void Error(String m)
    {
        ErrorDialog dialog = new ErrorDialog(m);
        dialog.pack();
        dialog.setVisible(true);
    }
    void Succes(String m)
    {
        ValidDialog dialog = new ValidDialog(m);
        dialog.pack();
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

    public int getPrix() {
        return Prix;
    }

    public void setPrix(int prix) {
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
