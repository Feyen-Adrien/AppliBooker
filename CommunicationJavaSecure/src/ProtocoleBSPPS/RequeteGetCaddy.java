package ProtocoleBSPPS;

import ServeurTCP.Requete;

public class RequeteGetCaddy implements Requete {
    private int nrCaddy;

    public RequeteGetCaddy(int nrCaddy) {
        this.nrCaddy = nrCaddy;
    }
    public int getNrCaddy() {
        return nrCaddy;
    }
    public void setNrCaddy(int nrCaddy) {
        this.nrCaddy = nrCaddy;
    }
}
