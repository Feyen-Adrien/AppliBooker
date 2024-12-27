package model.tests;

import model.dao.CaddiesDAO;
import model.entity.Caddies;

import java.util.ArrayList;

public class TestCaddies {
    public static void main(String[] args) {
        try {
            //init du DAO
            CaddiesDAO caddiesDAO = CaddiesDAO.getInstance();

            //CREATE
            Caddies newCaddies = new Caddies(null, 7, null, 25, null);
            caddiesDAO.addCaddies(newCaddies);

            int caddyId = newCaddies.getId();
            System.out.println("Ajout Sucess Id caddy = " + caddyId);

            //READ avec Client ID
            ArrayList<Caddies> caddiesList = caddiesDAO.getCaddiesByClientId(1);

            if (!caddiesList.isEmpty()) {
                System.out.println("\n\nResult of the GET BY ClientId : \n");
                for (Caddies caddy : caddiesList) {
                    System.out.println(
                            "\nID : " + caddy.getId() +
                            "\nClient ID : " + caddy.getCaddyId() +
                            "\nDate : " + caddy.getDate() +
                            "\nAmount : " + caddy.getAmount() +
                            "\nPayed : " + caddy.isPayed());
                }
            } else {
                System.out.println("\n\nNo caddy found");
            }

            //READ Only ID
            Caddies caddies = caddiesDAO.getCaddiesById(caddyId);

            if (caddies != null) {
                System.out.println("\n\nResult of the GET BY Id : \n");
                System.out.println(
                        "\nID : " + caddies.getId() +
                                "\nClient ID : " + caddies.getCaddyId() +
                                "\nDate : " + caddies.getDate() +
                                "\nAmount : " + caddies.getAmount() +
                                "\nPayed : " + caddies.isPayed());
            } else {
                System.out.println("\n\nNo caddy found");
            }

            //UPDATE Amount
            int addAmount = 50;
            caddiesDAO.updateCaddiesAmount(caddyId, addAmount);

            //READ Only ID
            caddies = caddiesDAO.getCaddiesById(caddyId);

            if (caddies != null) {
                System.out.println("\n\nResult of the UPDATE Amount : \n");
                System.out.println(
                        "\nID : " + caddies.getId() +
                                "\nClient ID : " + caddies.getCaddyId() +
                                "\nDate : " + caddies.getDate() +
                                "\nAmount : " + caddies.getAmount() +
                                "\nPayed : " + caddies.isPayed());
            } else {
                System.out.println("\n\nNo caddy found");
            }

            //UPDATE Payed
            caddiesDAO.updateCaddiesPayed(caddyId);

            //READ Only ID
            caddies = caddiesDAO.getCaddiesById(caddyId);

            if (caddies != null) {
                System.out.println("\n\nResult of the UPDATE Payed : \n");
                System.out.println(
                        "\nID : " + caddies.getId() +
                                "\nClient ID : " + caddies.getCaddyId() +
                                "\nDate : " + caddies.getDate() +
                                "\nAmount : " + caddies.getAmount() +
                                "\nPayed : " + caddies.isPayed());
            } else {
                System.out.println("\n\nNo caddy found");
            }


            //DELETE
            caddiesDAO.deleteCaddies(caddyId);
            System.out.println("\n\nDelete Sucess");




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
