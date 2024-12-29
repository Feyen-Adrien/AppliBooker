package ProtocoleBSPPS;

import ServeurTCP.Reponse;

public class ReponseLOGIN implements Reponse {
    private boolean valide = false ;
    private long date;
    private double alea;


    ReponseLOGIN(boolean valide) {
        this.valide = valide;
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
}
