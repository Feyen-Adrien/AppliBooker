package ProtocoleBSPPS;

import ServeurTCP.Reponse;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ReponseLOGIN implements Reponse {
    private boolean valide = false ;
    private long date;
    private double alea;
    private PublicKey publicKey;
    private int emplSecretKey;

    ReponseLOGIN(boolean valide)
    {
        this.valide = valide;
    }

    ReponseLOGIN(boolean valide, PublicKey publicKey, int emplSecretKey) {
        this.valide = valide;
        this.publicKey = publicKey;
        this.emplSecretKey = emplSecretKey;
    }
    ReponseLOGIN(long date, double alea, boolean valide) {
        this.date = date;
        this.alea = alea;
        this.valide = valide;
    }
    public boolean isValide() {
        return valide;
    }
    public void setValide(boolean valide) {
        this.valide = valide;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public double getAlea() {
        return alea;
    }
    public void setAlea(double alea) {
        this.alea = alea;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    public int getEmplSecretKey() {
        return emplSecretKey;
    }
    public void setEmplSecretKey(int emplSecretKey) {
        this.emplSecretKey = emplSecretKey;
    }
}
