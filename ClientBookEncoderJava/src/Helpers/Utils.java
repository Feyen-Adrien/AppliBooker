package src.Helpers;

import javax.swing.*;

public class Utils {

    public static void ViderLesChamps(JTextField txtTitle, JTextField txtISBN, JSpinner spPages, JSpinner spPrix, JSpinner spAnnepubli, JSpinner spStock) {
        txtTitle.setText("");
        txtISBN.setText("");
        spPages.setValue(0);
        spPrix.setValue(0.0);
        spAnnepubli.setValue(1980);
        spStock.setValue(0);
    }
}
