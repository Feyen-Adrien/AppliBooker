package GUI.Jframe;

import GUI.JDialog.*;
import ProtocoleBSPP.ReponseLOGIN;
import ProtocoleBSPP.RequeteLOGIN;
import ProtocoleBSPP.RequeteLOGOUT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

public class ClientAchat extends JFrame {
    private Socket socket;
    private String nom;
    private String prenom;
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
    private final JPopupMenu popupMenu;
    private final JMenuItem itemConnexion;
    private final JMenuItem itemInscription;
    private final JMenuItem itemDeconnexion;
    private JInscription inscription;
    private ConnexionClient connexion;

    public ClientAchat() {
        // configuration du Jframe
        super("Achat Client");
        setContentPane(clienAchatPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null);
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
            inscription.setVisible(true);
        });// permet d'afficher une Jdialog pour l'inscription
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
                                if(reponse.isValide())
                                {
                                    setNom(connexion.getNom());
                                    setPrenom(connexion.getPrenom());
                                    Succes("Connexion réussi !");
                                    connexion.dispose();
                                    connected(); // active les bouttons
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

        });// permet d'afficher une JDialog pour l connexion
        itemDeconnexion.addActionListener(e1 -> {
            RequeteLOGOUT requete = new RequeteLOGOUT(getNom());
            try {
                out.writeObject(requete);
                Succes("Client bien déconnecté");
                disconnected();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // vider panier si rempli ...
        });// permet de deconnecter l'utilisateur


        // Désactiver tous les boutons
        disconnected();


        // affichage du filtre
        filtreButton.addActionListener(e -> {
            Filtre filtre = new Filtre();
            filtre.setVisible(true);
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
}
