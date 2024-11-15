package GUI.JDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ValidDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel DialogSucces;

    public ValidDialog(String succesMessage) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(null);

        DialogSucces.setText("Succès : "+ succesMessage);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public static void main(String[] args) {
        ValidDialog dialog = new ValidDialog("Test le bonheur n'est qu'une illusion humaine");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
