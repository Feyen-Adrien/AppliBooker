package ProtocoleBSPPS;

import ServeurTCP.Requete;

public class RequeteLOGOUT implements Requete {
    private String userName;

    public RequeteLOGOUT(String userName) {
        setUserName(userName);
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
