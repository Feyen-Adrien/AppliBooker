package GUI.Jframe;

import GUI.JDialog.ConnexionClient;
import GUI.JDialog.Filtre;
import GUI.JDialog.JInscription;

import javax.swing.*;

public class ClientAchat extends JFrame {
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

    public ClientAchat() {
        super("Achat Client");
        setContentPane(clienAchatPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(700, 550);
        setLocationRelativeTo(null);

        // affichage du filtre
        filtreButton.addActionListener(e -> {
            Filtre filtre = new Filtre();
            filtre.setVisible(true);
        });

        // affichage menu déroulant
        menuButton.addActionListener(e -> {
            JPopupMenu popupmenu = new JPopupMenu();
            JMenuItem menuItemConnexion = new JMenuItem("Connexion");
            JMenuItem menuItemInscription = new JMenuItem("S'inscrire");
            JMenuItem menuItemDeconnexion = new JMenuItem("Se deconnecter");

            // affichage jDialogConnexion
            menuItemInscription.addActionListener(e1 -> {
                JInscription inscription  = new JInscription();
                inscription.setVisible(true);
            });
            menuItemConnexion.addActionListener(e1 -> {
                ConnexionClient connexion = new ConnexionClient();
                connexion.setVisible(true);
            });

            popupmenu.add(menuItemConnexion);
            popupmenu.add(menuItemInscription);
            popupmenu.add(menuItemDeconnexion);

            popupmenu.setLocation(this.getX(), this.getY());
            popupmenu.setVisible(true);


        });
    };


}
