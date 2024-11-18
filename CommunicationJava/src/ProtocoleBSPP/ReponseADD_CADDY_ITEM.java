package ProtocoleBSPP;

import ServeurTCP.Reponse;

public class ReponseADD_CADDY_ITEM implements Reponse {
    private int nrCaddy;

    public ReponseADD_CADDY_ITEM(int nrCaddy) {
        this.nrCaddy = nrCaddy;
    }
    public int getNrCaddy() {
        return nrCaddy;
    }
    public void setValid(int nrCaddy) {
        this.nrCaddy = nrCaddy;
    }
}
