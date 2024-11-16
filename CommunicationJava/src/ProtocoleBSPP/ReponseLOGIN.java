package ProtocoleBSPP;

import ServeurTCP.Reponse;

public class ReponseLOGIN implements Reponse {
    private int nrClient;


    ReponseLOGIN(int nrClient) {
        setNrClient(nrClient);
    }
    public int getNrClient() {
        return nrClient;
    }
    public void setNrClient(int nrClient) {
        this.nrClient = nrClient;
    }
}
