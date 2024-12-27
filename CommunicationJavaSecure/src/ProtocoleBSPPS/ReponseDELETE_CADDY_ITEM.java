package ProtocoleBSPPS;

import ServeurTCP.Reponse;

public class ReponseDELETE_CADDY_ITEM implements Reponse {
    private boolean valid;

    public ReponseDELETE_CADDY_ITEM(boolean valid) {
        this.valid = valid;
    }
    public ReponseDELETE_CADDY_ITEM() {

    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    public boolean getValid() {
        return valid;
    }
}
