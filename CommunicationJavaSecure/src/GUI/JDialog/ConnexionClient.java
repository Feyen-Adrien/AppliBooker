package GUI.JDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;



public class ConnexionClient extends JDialog {
    private JPanel contentPane;
    private JButton buttonConnexion;
    private JButton buttonAnnuler;
    private JTextField textFieldNom;
    private JPanel nomPanel;
    private JPanel nomPanelInfo;
    private JLabel nomLabel;
    private JPanel nomInputPanel;
    private JPanel prenomPanelInfo;
    private JPanel prenomPanel;
    private JLabel prenomLabel;
    private JPanel prenomInputLabel;
    private JTextField textFieldPrenom;
    private JPanel controlPanel;
    private JPanel bouttonPanel;
    private JPanel numClientPanel;
    private JPanel numClientPanelInfo;
    private JPanel numClientInput;
    private JLabel nrClientLabel;
    private JTextField textFieldNrClient;

    public ConnexionClient() throws IOException {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonConnexion);
        setTitle("Connexion");
        setLocationRelativeTo(null);
        pack();// permet de prendre la taille minimale ou préféré


        buttonAnnuler.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onCancel() {
        dispose();
    }


    // SETTERS / GETTERS
    public String getNom()
    {
        return textFieldNom.getText();
    }
    public String getPrenom()
    {
        return textFieldPrenom.getText();
    }
    public int getNrClient() { return Integer.parseInt(textFieldNrClient.getText());}

    public JButton getButtonConnexion() {
        return buttonConnexion;
    }

    public static void main(String[] args) {
        // vide
    }
}
