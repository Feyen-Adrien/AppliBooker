package GUI.JDialog;

import ProtocoleBSPP.ReponseGetAuteur;
import ProtocoleBSPP.RequeteGetAuteur;
import model.entity.Author;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Filtre extends JDialog {
    private JPanel Filtre;
    private JButton buttonOK;
    private JButton buttonAnnuler;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JSpinner spinner1;
    private JPanel JValidation;
    private JPanel JChoix;
    private JLabel Auteur;
    private JLabel Livre;
    private JLabel Sujet;
    private JLabel Prix;

    public Filtre(ObjectInputStream in, ObjectOutputStream out) {
        setContentPane(Filtre);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Filtre");
        setLocationRelativeTo(null);
        setSize(500,350);

        RequeteGetAuteur requeteGetAuteur = new RequeteGetAuteur();
        try {
            out.writeObject(requeteGetAuteur);
            ReponseGetAuteur reponseGetAuteur = new ReponseGetAuteur();
            reponseGetAuteur = (ReponseGetAuteur) in.readObject();

            // parcourir le tableau recu..


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        buttonOK.addActionListener(new ActionListener() {
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
        Filtre.registerKeyboardAction(new ActionListener() {
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
      //
    }
}
