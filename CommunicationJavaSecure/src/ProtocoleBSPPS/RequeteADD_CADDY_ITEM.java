package ProtocoleBSPPS;

import ServeurTCP.Requete;

public class RequeteADD_CADDY_ITEM implements Requete {
    int idLivre;
    int quantite;
    int caddyCreated;
    int idClient;

    public RequeteADD_CADDY_ITEM(int idLivre, int quantite, int caddyCreated, int idClient) {
        this.idLivre = idLivre;
        this.quantite = quantite;
        this.caddyCreated = caddyCreated;
        this.idClient = idClient;
    }
    public int getIdLivre() {
        return idLivre;
    }
    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }
    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    public int isCaddyCreated() {
        return caddyCreated;
    }
    public void setCaddyCreated(int caddyCreated) {
        this.caddyCreated = caddyCreated;
    }
    public int getIdClient() {
        return idClient;
    }
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }
}
