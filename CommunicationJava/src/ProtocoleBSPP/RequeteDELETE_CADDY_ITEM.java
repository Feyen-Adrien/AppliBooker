package ProtocoleBSPP;

import ServeurTCP.Requete;

public class RequeteDELETE_CADDY_ITEM implements Requete {
    int idItemCaddy;


    public RequeteDELETE_CADDY_ITEM(int idLivre) {
        this.idItemCaddy = idLivre;
    }

    public int getIdItemCaddy() {
        return idItemCaddy;
    }

    public void setIdItemCaddy(int idLivre) {
        this.idItemCaddy = idLivre;
    }

}
