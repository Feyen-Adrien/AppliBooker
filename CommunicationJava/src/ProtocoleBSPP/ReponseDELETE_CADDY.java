package ProtocoleBSPP;

import ServeurTCP.Reponse;

public class ReponseDELETE_CADDY implements Reponse {
    private boolean valid;

    public ReponseDELETE_CADDY(boolean valid) {
        this.valid = valid;
    }

    public ReponseDELETE_CADDY() {
        this.valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
