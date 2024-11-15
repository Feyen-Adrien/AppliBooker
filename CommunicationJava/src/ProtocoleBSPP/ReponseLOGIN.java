package ProtocoleBSPP;

import ServeurTCP.Reponse;

public class ReponseLOGIN implements Reponse {
    private boolean valide;

    ReponseLOGIN(boolean valide) {
        setValide(valide);
    }
    public boolean isValide() {
        return valide;
    }
    public void setValide(boolean valide) {
        this.valide = valide;
    }
}
