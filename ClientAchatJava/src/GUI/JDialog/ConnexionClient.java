package GUI.JDialog;

import javax.swing.*;
import java.awt.event.*;

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

    public ConnexionClient() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonConnexion);
        setTitle("Connexion");
        setLocationRelativeTo(null);
        pack();// permet de prendre la taille minimale ou préféré

        buttonConnexion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonAnnuler.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ConnexionClient dialog = new ConnexionClient();
        dialog.setVisible(true);
        System.exit(0);
    }
}
