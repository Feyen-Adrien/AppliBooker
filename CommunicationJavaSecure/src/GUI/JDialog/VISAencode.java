package GUI.JDialog;

import javax.swing.*;
import java.awt.event.*;

public class VISAencode extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nomVISA;
    private JTextField numVISA;
    private JPanel jPanel;

    public VISAencode() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Payer avec VISA");
        setLocationRelativeTo(null);
        pack();

        numVISA.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Autoriser uniquement les chiffres (0-9)
                if (!Character.isDigit(c)) {
                    e.consume();  // Ignore la touche si ce n'est pas un chiffre
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
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

    //  getters
    public String getNomVISA() {
        return nomVISA.getText();
    }
    public String getNumVISA() {
        return numVISA.getText();
    }
    public JButton getButtonOK() {
        return buttonOK;
    }
    public static void main(String[] args) {
        // vide
    }
}
