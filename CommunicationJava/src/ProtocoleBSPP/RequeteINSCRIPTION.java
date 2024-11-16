package ProtocoleBSPP;

import ServeurTCP.Requete;

public class RequeteINSCRIPTION implements Requete {
    private String userName;
    private String firstName;

    public RequeteINSCRIPTION(String userName, String firstName) {
        setUserName(userName);
        setFirstName(firstName);
    }
    public String getUserName() {
        return userName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
