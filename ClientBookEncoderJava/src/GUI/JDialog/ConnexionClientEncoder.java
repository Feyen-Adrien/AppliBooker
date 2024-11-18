package GUI.JDialog;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;



public class ConnexionClientEncoder extends JDialog {
    private JPanel contentPane;
    private JButton buttonConnexion;
    private JButton buttonAnnuler;
    private JTextField textFieldLogin;
    private JPanel LoginPanel;
    private JPanel LoginPanelInfo;
    private JLabel LoginLabel;
    private JPanel LoginInputPanel;
    private JPanel passwordPanelInfo;
    private JPanel passwordPanel;
    private JLabel passwordLabel;
    private JPanel passwordInputLabel;
    private JPanel controlPanel;
    private JPanel bouttonPanel;
    private JPasswordField passwordField;

    public ConnexionClientEncoder() throws IOException {
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
    public String getLogin()
    {
        return textFieldLogin.getText();
    }
    public String getPassword()
    {
        return passwordField.getText();
    }

    public JButton getButtonConnexion() {
        return buttonConnexion;
    }

    public static void main(String[] args) {
        // vide
    }
}
