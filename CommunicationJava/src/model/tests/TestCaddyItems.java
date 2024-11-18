package model.tests;

import model.dao.BookDAO;
import model.dao.CaddyItemsDAO;
import model.entity.CaddyItems;
import model.viewmodel.CaddyItemsSearchVM;

import java.util.ArrayList;

public class TestCaddyItems {
    public static void main(String[] args) {
        try {
            //init du DAO
            CaddyItemsDAO caddyItemsDAO = CaddyItemsDAO.getInstance();

            //CREATE
            CaddyItems newcaddyItems = new CaddyItems(null, 1, 1, 24);
            caddyItemsDAO.addCaddyItems(newcaddyItems);

            int caddyItemId = newcaddyItems.getId();
            System.out.println("Ajout Sucess Id CaddyItem = " + caddyItemId);


            //READ Only bookID
            CaddyItemsSearchVM caddyItemsSearchVM = new CaddyItemsSearchVM(1, null);
            ArrayList<CaddyItems> caddyItems = caddyItemsDAO.getCaddyItems(1);

            if (!caddyItems.isEmpty()) {
                System.out.println("\n\nResult of the GET BY BookId : \n");
                for (CaddyItems caddyItem : caddyItems) {
                    System.out.println(
                            "\nID : " + caddyItem.getId() +
                            "\nBook ID : " + caddyItem.getBookId() +
                            "\nCaddy ID : " + caddyItem.getCaddyId() +
                            "\nQuantity : " + caddyItem.getQuantity());
                }
            } else {
                System.out.println("\n\nNo caddyItems found");
            }

            //READ Only caddyID
            caddyItemsSearchVM = new CaddyItemsSearchVM(null, 1);
            caddyItems = caddyItemsDAO.getCaddyItems(1);

            if (!caddyItems.isEmpty()) {
                System.out.println("\n\nResult of the GET BY CaddyId : \n");
                for (CaddyItems caddyItem : caddyItems) {
                    System.out.println(
                            "\nID : " + caddyItem.getId() +
                                    "\nBook ID : " + caddyItem.getBookId() +
                                    "\nCaddy ID : " + caddyItem.getCaddyId() +
                                    "\nQuantity : " + caddyItem.getQuantity());
                }
            } else {
                System.out.println("\n\nNo caddyItems found");
            }

            //READ The TWO
            caddyItemsSearchVM = new CaddyItemsSearchVM(1, 1);
            caddyItems = caddyItemsDAO.getCaddyItems(1);

            if (!caddyItems.isEmpty()) {
                System.out.println("\n\nResult of the GET BY BookId & CaddyId : \n");
                for (CaddyItems caddyItem : caddyItems) {
                    System.out.println(
                            "\nID : " + caddyItem.getId() +
                                    "\nBook ID : " + caddyItem.getBookId() +
                                    "\nCaddy ID : " + caddyItem.getCaddyId() +
                                    "\nQuantity : " + caddyItem.getQuantity());
                }
            } else {
                System.out.println("\n\nNo caddyItems found");
            }

            //ReadId
            CaddyItems caddyItems1 = caddyItemsDAO.getCaddyItemsbyId(caddyItemId);

            if (caddyItems1 != null) {
                System.out.println("\n\nResult of the GET BY Id : \n");
                System.out.println(
                        "\nID : " + caddyItems1.getId() +
                                "\nBook ID : " + caddyItems1.getBookId() +
                                "\nCaddy ID : " + caddyItems1.getCaddyId() +
                                "\nQuantity : " + caddyItems1.getQuantity());
            }


            //UPDATE +1
            caddyItemsDAO.updateCaddyItemsPlusOne(caddyItemId);

            //ReadId
            caddyItems1 = caddyItemsDAO.getCaddyItemsbyId(caddyItemId);

            if (caddyItems1 != null) {
                System.out.println("\n\nResult of the UPDATE +1 : \n");
                System.out.println(
                        "\nID : " + caddyItems1.getId() +
                                "\nBook ID : " + caddyItems1.getBookId() +
                                "\nCaddy ID : " + caddyItems1.getCaddyId() +
                                "\nQuantity : " + caddyItems1.getQuantity());
            }

            //UPDATE -1
            caddyItemsDAO.updateCaddyItemsMinusOne(caddyItemId);

            //ReadId
            caddyItems1 = caddyItemsDAO.getCaddyItemsbyId(caddyItemId);

            if (caddyItems1 != null) {
                System.out.println("\n\nResult of the UPDATE -1 : \n");
                System.out.println(
                        "\nID : " + caddyItems1.getId() +
                                "\nBook ID : " + caddyItems1.getBookId() +
                                "\nCaddy ID : " + caddyItems1.getCaddyId() +
                                "\nQuantity : " + caddyItems1.getQuantity());
            }



            //DELETE
            caddyItemsDAO.deleteCaddyItems(caddyItemId);
            System.out.println("DELETE Sucess Id CaddyItem = " + caddyItemId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
