package ProtocoleBSPP;

import ServeurTCP.Reponse;

public class ReponseUPDATE_CADDY_PAYED implements Reponse {
    private boolean valide;

    public ReponseUPDATE_CADDY_PAYED(boolean valide) {
        this.valide = valide;
    }
    public ReponseUPDATE_CADDY_PAYED() {
        this.valide = false;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public boolean isValide() {
        return valide;
    }
}
