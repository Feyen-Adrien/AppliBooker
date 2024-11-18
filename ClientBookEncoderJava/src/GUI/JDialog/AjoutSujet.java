package GUI.JDialog;

import Helpers.Entity.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AjoutSujet extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldSubject;

    private Subject subject;

    public AjoutSujet() {
        setContentPane(contentPane);
        setModal(true);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(300, 150));
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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

    }

    private void onOK() {
        subject = new Subject(textFieldSubject.getText());
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public Subject getSubject() {
        return subject;
    }

    public static void main(String[] args) {
        AjoutSujet dialog = new AjoutSujet();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
