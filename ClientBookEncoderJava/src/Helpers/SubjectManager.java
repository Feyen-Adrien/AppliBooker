package Helpers;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class SubjectManager {
    private ServerConnection serverConnection;

    public SubjectManager(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    public ArrayList<String> getSubjects() {
        ArrayList<String> subjects = new ArrayList<>();
        try {
            String request = RequestBuilder.buildGetSubjectsRequest();
            serverConnection.sendRequest(request);
            String response;
            response = serverConnection.receiveRequest();
            if (response.equals("GET_SUBJECTS#ok")) {
                while (!(response = serverConnection.receiveRequest()).equals("FINRSQL")) {
                    subjects.add(response);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur Get Subject");
        }
        return subjects;
    }

    public void addSubject(String subject) {
        try {
            String request = RequestBuilder.buildAddSubjectRequest(subject);
            serverConnection.sendRequest(request);
            String response = serverConnection.receiveRequest();
            JOptionPane.showMessageDialog(null, response);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur Add Subject");
        }
    }

    public void LoadSubjects(JComboBox cbSujet) {
        ArrayList<String> subjects = getSubjects();
        cbSujet.removeAllItems();
        String[] element = null;

        for (String subject : subjects) {
            element = subject.split("#");
            cbSujet.addItem(element[1]);
        }

    }
}
