package model.tests;

import model.dao.BookDAO;
import model.entity.Book;
import model.viewmodel.BookSearchVM;

import java.util.ArrayList;

public class TestBooks {
    public static void main(String[] args) {
        try {
            //init du DAO
            BookDAO bookDAO = BookDAO.getInstance();

            //CREATE
            Book newBook = new Book(null, 1, 2, "Agenda", "1234567891", 1200, 5, 99.99f, 2024,null,null,null);
            bookDAO.addBook(newBook);

            int bookId = newBook.getId();
            System.out.println("Ajout Sucess Id Livre = " + bookId);

            //Search for One book spécific
            BookSearchVM searchVMFilter = new BookSearchVM("Werber", "Les Fourmis", "Science-Fiction", 2f);
            ArrayList<Book> booksFound = bookDAO.serchBooks(searchVMFilter);

            if (!booksFound.isEmpty()) {
                System.out.println("\n\nResult of the Search : \n");
                for (Book book : booksFound) {
                    System.out.println(
                            "\nID : " + book.getId() +
                            "\nAuthor ID : " + book.getAuthor_id() +
                            "\nSubject ID : " + book.getSubject_id() +
                            "\nTitre : " + book.getTitle() +
                            "\nISBN : " + book.getIsbn() +
                            "\nPage Count : " + book.getPage_count() +
                            "\nStock Quantity : " + book.getStock_quantity() +
                            "\nPrice : " + book.getPrice() +
                            "\nPublish Year : " + book.getPublish_year()+
                            "\nNomAuteur : " + book.getNomAuteur()+ " " +book.getPrenomAuteur()+
                            "\nSujet : " + book.getNomSujet());
                }
            } else {
                System.out.println("No books found");
            }


            //Search for A Lot of Book Different
            searchVMFilter = new BookSearchVM("Werber", "Default", "Default", 2f);
            booksFound = bookDAO.serchBooks(searchVMFilter);

            if (!booksFound.isEmpty()) {
                System.out.println("\n\nResult of the Search : \n");
                for (Book book : booksFound) {
                    System.out.println(
                            "\nID : " + book.getId() +
                                    "\nAuthor ID : " + book.getAuthor_id() +
                                    "\nSubject ID : " + book.getSubject_id() +
                                    "\nTitre : " + book.getTitle() +
                                    "\nISBN : " + book.getIsbn() +
                                    "\nPage Count : " + book.getPage_count() +
                                    "\nStock Quantity : " + book.getStock_quantity() +
                                    "\nPrice : " + book.getPrice() +
                                    "\nPublish Year : " + book.getPublish_year());
                }
            } else {
                System.out.println("No books found");
            }

            //GET ONEBOOK
            Book booksget = bookDAO.getBookById(bookId);

            if (booksget != null) {
                System.out.println("\n\nResult of the GETONE ID = " + bookId + " : \n");
                System.out.println(
                        "\nID : " + booksget.getId() +
                                "\nAuthor ID : " + booksget.getAuthor_id() +
                                "\nSubject ID : " + booksget.getSubject_id() +
                                "\nTitre : " + booksget.getTitle() +
                                "\nISBN : " + booksget.getIsbn() +
                                "\nPage Count : " + booksget.getPage_count() +
                                "\nStock Quantity : " + booksget.getStock_quantity() +
                                "\nPrice : " + booksget.getPrice() +
                                "\nPublish Year : " + booksget.getPublish_year());
            } else {
                System.out.println("No books found");
            }

            //GET ALLBOOKS
            ArrayList<Book> booksgets = bookDAO.getAllBooks();

            if (!booksgets.isEmpty()) {
                System.out.println("\n\nResult of the GETALL : \n");
                for (Book book : booksgets) {
                    System.out.println(
                            "\nID : " + book.getId() +
                            "\nAuthor ID : " + book.getAuthor_id() +
                            "\nSubject ID : " + book.getSubject_id() +
                            "\nTitre : " + book.getTitle() +
                            "\nISBN : " + book.getIsbn() +
                            "\nPage Count : " + book.getPage_count() +
                            "\nStock Quantity : " + book.getStock_quantity() +
                            "\nPrice : " + book.getPrice() +
                            "\nPublish Year : " + book.getPublish_year());
                }
            } else {
                System.out.println("No books found");
            }

            //UPDATE
            Book updateBook = new Book(bookId, 1, 2, "Alinea", "4567891235", 2500, 3, 99.99f, 2024,null,null,null);
            bookDAO.updateBook(updateBook);

            //GET ONEBOOK
            booksget = bookDAO.getBookById(bookId);

            if (booksget != null) {
                System.out.println("\n\nResult of the GETONE ID = " + bookId + " : \n");
                System.out.println(
                        "\nID : " + booksget.getId() +
                                "\nAuthor ID : " + booksget.getAuthor_id() +
                                "\nSubject ID : " + booksget.getSubject_id() +
                                "\nTitre : " + booksget.getTitle() +
                                "\nISBN : " + booksget.getIsbn() +
                                "\nPage Count : " + booksget.getPage_count() +
                                "\nStock Quantity : " + booksget.getStock_quantity() +
                                "\nPrice : " + booksget.getPrice() +
                                "\nPublish Year : " + booksget.getPublish_year());
            } else {
                System.out.println("No books found");
            }

            // Suppression du livre de test
            bookDAO.deleteBookById(bookId);
            System.out.println("\n\nDelete Success");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
