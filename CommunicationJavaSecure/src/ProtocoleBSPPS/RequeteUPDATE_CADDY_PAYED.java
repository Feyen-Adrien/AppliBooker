package ProtocoleBSPPS;

import ServeurTCP.Requete;

public class RequeteUPDATE_CADDY_PAYED implements Requete {
    private int idCaddy;

    public RequeteUPDATE_CADDY_PAYED(int idCaddy) {
        this.idCaddy = idCaddy;
    }

    public int getIdCaddy() {
        return idCaddy;
    }
}
