package ProtocoleBSPP;

import ServeurTCP.Reponse;
import model.entity.CaddyItems;

import java.util.ArrayList;

public class ReponseGetCaddy implements Reponse {
    private ArrayList<CaddyItems> caddyItems;

    public ReponseGetCaddy() {

    }
    public ArrayList<CaddyItems> getCaddyItems() {
        return caddyItems;
    }
    public void setCaddyItems(ArrayList<CaddyItems> caddyItems) {
        this.caddyItems = caddyItems;
    }
}
