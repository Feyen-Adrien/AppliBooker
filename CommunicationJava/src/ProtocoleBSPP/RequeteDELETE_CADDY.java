package ProtocoleBSPP;

import ServeurTCP.Requete;

public class RequeteDELETE_CADDY implements Requete {
    private final int idCaddy;

    public RequeteDELETE_CADDY(int idCaddy) {
        this.idCaddy = idCaddy;
    }

    public int getIdCaddy() {
        return idCaddy;
    }
}
