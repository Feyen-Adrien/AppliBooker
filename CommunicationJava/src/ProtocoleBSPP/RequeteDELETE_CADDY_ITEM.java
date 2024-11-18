package ProtocoleBSPP;

import ServeurTCP.Requete;

public class RequeteDELETE_CADDY_ITEM implements Requete {
    int idLivre;

    public RequeteDELETE_CADDY_ITEM(int idLivre) {
        this.idLivre = idLivre;
    }

    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }
}
