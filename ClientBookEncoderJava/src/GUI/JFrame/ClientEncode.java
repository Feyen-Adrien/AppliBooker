package src.GUI.JFrame;

import GUI.JDialog.ConnexionClientEncoder;
import src.GUI.JDialog.*;
import src.Helpers.*;
import src.Helpers.Entity.Author;
import src.Helpers.Entity.Subject;
import src.ProtocoleBSPP.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class ClientEncode extends JFrame {

    private JPanel panel1;
    private JButton ajouterUnNouveauLivreButton;
    private JTextField txtTitle;
    private JTextField txtISBN;
    private JSpinner spPages;
    private JSpinner spPrix;
    private JSpinner spAnnepubli;
    private JSpinner spStock;
    private JButton btnViderChamps;
    private JButton btnAuteur;
    private JComboBox cbAuteur;
    private JComboBox cbSujet;
    private JButton btnSujet;
    private JTable table1;
    private JToolBar clientToolBar;
    private JButton menuButton;
    private DefaultTableModel tableModel;

    private JPopupMenu popupMenu;
    private JMenuItem itemConnexion;
    private JMenuItem itemDeconnexion;
    private ConnexionClientEncoder connexion;


    private ServerConnection serverconnection;
    private BookManager bookmanager;
    private AuthorManager authormanager;
    private SubjectManager subjectmanager;
    private ConnexionManager connexionManager;

    public ClientEncode() {
        super("Book Encoder");

        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 300);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(400, 150));

        serverconnection = new ServerConnection("192.168.254.128", 50000);
        bookmanager = new BookManager(serverconnection);
        authormanager = new AuthorManager(serverconnection);
        subjectmanager = new SubjectManager(serverconnection);
        connexionManager = new ConnexionManager(serverconnection);

        // configuration du JPopupMenu
        popupMenu = new JPopupMenu();

        itemConnexion = new JMenuItem("Connexion");
        itemDeconnexion = new JMenuItem("Deconnexion");
        popupMenu.add(itemConnexion);
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
        itemConnexion.addMouseListener(mouseAdapter);


        itemConnexion.addActionListener(e1 -> {
            try {
                connexion = new ConnexionClientEncoder();
                connexion.getButtonConnexion().addActionListener(e2 ->{

                    if (connexion.getLogin().isEmpty()) {
                        JOptionPane.showMessageDialog(ClientEncode.this, "Veuillez encoder un Login.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else if (connexion.getPassword().isEmpty()) {
                        JOptionPane.showMessageDialog(ClientEncode.this, "Veuillez encoder un Password", "Erreur", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String[] element;
                        element = connexionManager.Login(connexion.getLogin(), connexion.getPassword());

                        if (element[1].equals("ok"))
                        {
                            connexion.dispose();
                            connect();
                            try {
                                LoadData();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (element[1].equals("ko")) {
                            JOptionPane.showMessageDialog(ClientEncode.this, element[2], "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                connexion.setVisible(true);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        itemDeconnexion.addActionListener(e1 -> {
            connexionManager.Logout();
            logout();
        });


        // Désactiver tous les boutons
        disconnected();

        // affichage menu déroulant
        menuButton.addActionListener(e -> popupMenu.show(this,33,56));

        tableModel = new DefaultTableModel(new String[]{"id", "Titre", "Auteur", "Sujet", "ISBN", "Pages", "Annepubli","Prix", "Stock"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table1.setModel(tableModel);

        spPrix.setModel(new SpinnerNumberModel(0.0, 0.0, 10000.0, 0.1));


        //Vide tout les champs
        btnViderChamps.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Utils.ViderLesChamps(txtTitle, txtISBN, spPages, spPrix, spAnnepubli, spStock);
            }
        });

        //ajouter un nouveau livre
        ajouterUnNouveauLivreButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               if (!txtTitle.getText().isEmpty() && !txtISBN.getText().isEmpty()) {
                   int id = bookmanager.addBook(txtTitle.getText(), txtISBN.getText(), (int) spPages.getValue(), (double) spPrix.getValue(), (int) spAnnepubli.getValue(), (int) spStock.getValue(), (String) cbAuteur.getSelectedItem(), (String) cbSujet.getSelectedItem());
                   Object[] row = {id, txtTitle.getText(), cbAuteur.getSelectedItem(), cbSujet.getSelectedItem(), txtISBN.getText(), spPages.getValue(), spAnnepubli.getValue(), spPrix.getValue(), spStock.getValue()};
                   tableModel.insertRow(0, row);
               } else {
                   JOptionPane.showMessageDialog(ClientEncode.this, "Le titre et l'ISBN sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
               }
           }
        });

        //ajouter un nouveau auteur
        btnAuteur.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AjoutAuteur dialog = new AjoutAuteur();
                dialog.setVisible(true);
                Author newAuthor = dialog.getAuthor();
                if (newAuthor!=null) {
                    authormanager.addAuthor(newAuthor.getLastname(), newAuthor.getFirstname(), newAuthor.getDateofbirth());
                    cbAuteur.addItem(newAuthor.getLastname() + " " + newAuthor.getFirstname());
                }
            }
        });

        //ajouter un nouveau sujet
        btnSujet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AjoutSujet dialog = new AjoutSujet();
                dialog.setVisible(true);
                Subject newSubject = dialog.getSubject();
                if (newSubject!=null) {
                    subjectmanager.addSubject(newSubject.getSubject());
                    cbSujet.addItem(newSubject.getSubject());
                }
            }
        });


        //Click sur quitter l'app
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeConnection();  // Fermer proprement la connexion avant de quitter
                System.exit(0);
            }
        });

    }

    private void LoadData() throws IOException {
        subjectmanager.LoadSubjects(cbSujet);
        authormanager.LoadAuthors(cbAuteur);
    }

    private void closeConnection() {
        try {
            connexionManager.Logout();
            serverconnection.close();
            System.out.println("Connexion fermée proprement.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnected(){
        ajouterUnNouveauLivreButton.setEnabled(false);
        spPages.setEnabled(false);
        spPrix.setEnabled(false);
        spAnnepubli.setEnabled(false);
        spStock.setEnabled(false);
        btnViderChamps.setEnabled(false);
        btnAuteur.setEnabled(false);
        cbAuteur.setEnabled(false);
        cbSujet.setEnabled(false);
        btnSujet.setEnabled(false);
        itemConnexion.setEnabled(true);
        itemDeconnexion.setEnabled(false);
    }

    private void connect() {
        ajouterUnNouveauLivreButton.setEnabled(true);
        spPages.setEnabled(true);
        spPrix.setEnabled(true);
        spAnnepubli.setEnabled(true);
        spStock.setEnabled(true);
        btnViderChamps.setEnabled(true);
        btnAuteur.setEnabled(true);
        cbAuteur.setEnabled(true);
        cbSujet.setEnabled(true);
        btnSujet.setEnabled(true);
        itemConnexion.setEnabled(false);
        itemDeconnexion.setEnabled(true);
    }

    private void logout() {
        Utils.ViderLesChamps(txtTitle, txtISBN, spPages, spPrix, spAnnepubli, spStock);
        disconnected();

        for (int i=0; tableModel.getRowCount()>0; i++) {
            tableModel.removeRow(i);
        }
    }

    public static void main(String[] args) {
        ClientEncode frame = new ClientEncode();
        frame.setVisible(true);

    }
}



