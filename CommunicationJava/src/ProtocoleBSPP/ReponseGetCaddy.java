package ProtocoleBSPP;

import ServeurTCP.Reponse;
import model.entity.CaddyItems;

import java.util.ArrayList;

public class ReponseGetCaddy implements Reponse {
    private ArrayList<CaddyItems> caddyItems;
    double maxAmount;

    public ReponseGetCaddy() {

    }
    public ArrayList<CaddyItems> getCaddyItems() {
        return caddyItems;
    }
    public void setCaddyItems(ArrayList<CaddyItems> caddyItems) {
        this.caddyItems = caddyItems;
    }
    public double getMaxAmount() {
        return maxAmount;
    }
    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }
}
