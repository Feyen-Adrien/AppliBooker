package GUI.JDialog;

import ProtocoleBSPPS.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Filtre extends JDialog {
    private JPanel Filtre;
    private JButton buttonOK;
    private JButton buttonAnnuler;
    private JComboBox<String> comboBoxAuteur;
    private JComboBox<String> comboBoxTitre;
    private JComboBox<String> comboBoxSujet;
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

        SpinnerNumberModel mod = new SpinnerNumberModel(0.0,0,10000,0.01);
        spinner1.setModel(mod);

        // création des comboxbox
        try
        {
            RequeteGetAuteur requeteGetAuteur = new RequeteGetAuteur();
            out.writeObject(requeteGetAuteur);
            ReponseGetAuteur reponseGetAuteur = new ReponseGetAuteur();
            reponseGetAuteur = (ReponseGetAuteur) in.readObject();



            for(int i=0;i<reponseGetAuteur.getListeAuteur().size();i++){
                if(i==0)
                {
                    comboBoxAuteur.addItem(" ");
                }
                comboBoxAuteur.addItem(reponseGetAuteur.getListeAuteur().get(i).getLastName()+ " "+ reponseGetAuteur.getListeAuteur().get(i).getFirstSurname());
            }


        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try
        {
            RequeteGetBooks requeteGetBooks = new RequeteGetBooks();
            out.writeObject(requeteGetBooks);
            ReponseGetBooks reponseGetBooks = new ReponseGetBooks();
            reponseGetBooks = (ReponseGetBooks) in.readObject();

            for (int i =0;i<reponseGetBooks.getListeLivres().size();i++) {
                if(i==0)
                {
                    comboBoxTitre.addItem(" ");
                }
                comboBoxTitre.addItem(reponseGetBooks.getListeLivres().get(i).getTitle());

            }

        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        try
        {
            RequeteGetSubjects requeteGetSubjects = new RequeteGetSubjects();
            out.writeObject(requeteGetSubjects);
            ReponseGetSubjects reponseGetSubjects = new ReponseGetSubjects();
            reponseGetSubjects = (ReponseGetSubjects) in.readObject();

            for(int i = 0;i<reponseGetSubjects.getSubjects().size();i++) {
                if(i==0)
                {
                    comboBoxSujet.addItem(" ");
                }
                comboBoxSujet.addItem(reponseGetSubjects.getSubjects().get(i).getSubject_name());
            }

        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

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

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    // setters/gettes
    public JComboBox<String> getComboBoxSujet() {
        return comboBoxSujet;
    }
    public JComboBox<String> getComboBoxAuteur() {
        return comboBoxAuteur;
    }
    public JComboBox<String> getComboBoxTitre() {
        return comboBoxTitre;
    }
    public JSpinner getSpinner1() {
        return spinner1;
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    public static void main(String[] args) {
      //
    }
}
