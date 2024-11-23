package GUI.JFrame;

import GUI.JDialog.ConnexionClientEncoder;
import GUI.JDialog.*;
import Helpers.*;
import Helpers.Entity.Author;
import Helpers.Entity.Subject;
import Helpers.Managers.AuthorManager;
import Helpers.Managers.BookManager;
import Helpers.Managers.ConnexionManager;
import Helpers.Managers.SubjectManager;

import javax.sound.sampled.UnsupportedAudioFileException;
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

        serverconnection = new ServerConnection("192.168.21.129", 50000);
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
                        Error("Veuillez encoder un Login.");
                    } else if (connexion.getPassword().isEmpty()) {
                        Error("Veuillez encoder un Password.");
                    } else {
                        String[] element;
                        element = connexionManager.Login(connexion.getLogin(), connexion.getPassword());

                        if (element[1].equals("ok"))
                        {
                            connexion.dispose();
                            connect();
                            try {
                                LoadData();
                                Utils.PlayMusic(4);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (UnsupportedAudioFileException e) {
                                throw new RuntimeException(e);
                            }
                        } else if (element[1].equals("ko")) {
                            Error(element[2]);
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

        //Dans ces modèle de spinner on peut réduire leurs tailles
        spPages.setModel(new SpinnerNumberModel(0, 0, 10000, 1));
        spPrix.setModel(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1));
        spAnnepubli.setModel(new SpinnerNumberModel(1980, 1900, 2021, 1));
        spStock.setModel(new SpinnerNumberModel(0, 0, 1000, 1));



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
                   if (!Utils.checkISBN(txtISBN.getText())) {
                       Error("L'ISBN doit contenir 10 chiffres.");
                       return;
                   }
                   int id = bookmanager.addBook(txtTitle.getText(), txtISBN.getText(), (int) spPages.getValue(), (double) spPrix.getValue(), (int) spAnnepubli.getValue(), (int) spStock.getValue(), (String) cbAuteur.getSelectedItem(), (String) cbSujet.getSelectedItem());
                   Object[] row = {id, txtTitle.getText(), cbAuteur.getSelectedItem(), cbSujet.getSelectedItem(), txtISBN.getText(), spPages.getValue(), spAnnepubli.getValue(), spPrix.getValue(), spStock.getValue()};
                   tableModel.insertRow(0, row);

                   try {
                       Utils.PlayMusic(0);
                   } catch (IOException ex) {
                       throw new RuntimeException(ex);
                   } catch (UnsupportedAudioFileException ex) {
                       throw new RuntimeException(ex);
                   }

               } else {
                   Error("Le titre et l'ISBN sont obligatoires.");
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

        panel1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Click();
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

        try {
            Utils.PlayMusic(5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }

        for (int i=0; tableModel.getRowCount()>0; i++) {
            tableModel.removeRow(i);
        }
    }

    private void Click() {
        try {
            Utils.PlayMusic(3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
    }

    private void Error(String message) {
        try {
            Utils.PlayMusic(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }
        JOptionPane.showMessageDialog(ClientEncode.this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        ClientEncode frame = new ClientEncode();
        frame.setVisible(true);

    }
}



