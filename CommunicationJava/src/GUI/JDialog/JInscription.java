package GUI.JDialog;

import javax.swing.*;
import java.awt.event.*;

public class JInscription extends JDialog {
    private JPanel contentPane;
    private JButton buttonInscription;
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

    public JInscription() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonInscription);
        setTitle("Inscription");
        setLocationRelativeTo(null);
        pack();// permet de prendre la taille minimale ou préféré

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

    private void onCancel() {
        // add your code here if necessary
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

    public JButton getButtonInscription() {
        return buttonInscription;
    }

    public static void main(String[] args) {
        JInscription dialog = new JInscription();
        dialog.setVisible(true);
        System.exit(0);
    }
}
