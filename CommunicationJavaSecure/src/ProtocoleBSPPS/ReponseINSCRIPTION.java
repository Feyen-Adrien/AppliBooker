package ProtocoleBSPPS;

import ServeurTCP.Reponse;

public class ReponseINSCRIPTION implements Reponse {
    private int nrClient;


    ReponseINSCRIPTION(int nrClient) {
        setNrClient(nrClient);
    }
    public int getNrClient() {
        return nrClient;
    }
    public void setNrClient(int nrClient) {
        this.nrClient = nrClient;
    }
}
