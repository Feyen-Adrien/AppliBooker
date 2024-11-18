package GUI.JDialog;

import Helpers.Entity.Author;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AjoutAuteur extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldFirstName;
    private JTextField textFieldLastName;
    private JTextField textFieldDate;

    private Author author;

    public AjoutAuteur() {
        setContentPane(contentPane);
        setModal(true);
        setSize(300, 200);
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
        author = new Author(textFieldFirstName.getText(), textFieldLastName.getText(), textFieldDate.getText());
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public Author getAuthor() {
        return author;
    }

    public static void main(String[] args) {
        AjoutAuteur dialog = new AjoutAuteur();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}