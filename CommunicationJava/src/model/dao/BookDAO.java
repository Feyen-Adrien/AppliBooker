package model.dao;

import model.entity.Book;
import model.viewmodel.BookSearchVM;

import java.sql.*;
import java.util.ArrayList;

import static model.dao.ConnectDB.close;

public class BookDAO {
    private static BookDAO instance;
    private ConnectDB connexion;
    private BookDAO() {}

    public static synchronized BookDAO getInstance() {
        if (instance == null) {
            instance = new BookDAO();
        }
        return instance;
    }

    //CREATE
    public void addBook(Book book) throws SQLException {
        try {
            String sql = "INSERT INTO books (author_id, subject_id, title, isbn, page_count, stock_quantity, price, publish_year) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, book.getAuthor_id());
            ps.setInt(2, book.getSubject_id());
            ps.setString(3, book.getTitle());
            ps.setString(4, book.getIsbn());
            ps.setInt(5, book.getPage_count());
            ps.setInt(6, book.getStock_quantity());
            ps.setDouble(7, book.getPrice());
            ps.setInt(8, book.getPublish_year());
            ps.executeUpdate();

            //Va mettre l'id dans le book insérer
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                book.setId(rs.getInt(1));
            }
            ps.close();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //READ ALL
    public ArrayList<Book> getAllBooks() throws SQLException {
        try {
            ArrayList<Book> books = new ArrayList<>();
            String sql = "SELECT * FROM books";

            connexion = new ConnectDB();
            Statement stmt = connexion.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getInt("subject_id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("page_count"),
                        rs.getInt("stock_quantity"),
                        rs.getFloat("price"),
                        rs.getInt("publish_year"),null,null,null
                ));
            }
            stmt.close();
            close();
            return books;

        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }

    //READ ONE
    public Book getBookById(int id) throws SQLException {
        try {
            String sql = "SELECT * FROM books WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getInt("subject_id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("page_count"),
                        rs.getInt("stock_quantity"),
                        rs.getFloat("price"),
                        rs.getInt("publish_year"),null,null,null
                );
            }
            close();
            return null;
        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }

    //UPDATE
    public void updateBook(Book book) throws SQLException {
        try {
            String sql = "UPDATE books SET author_id = ?, subject_id = ?, title = ?, isbn = ?, page_count = ?, stock_quantity = ?, price = ?, publish_year = ? WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, book.getAuthor_id());
            ps.setInt(2, book.getSubject_id());
            ps.setString(3, book.getTitle());
            ps.setString(4, book.getIsbn());
            ps.setInt(5, book.getPage_count());
            ps.setInt(6, book.getStock_quantity());
            ps.setFloat(7, book.getPrice());
            ps.setInt(8, book.getPublish_year());
            ps.setInt(9, book.getId());
            ps.executeUpdate();
            close();
        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }

    //DELETE
    public void deleteBookById(int id) throws SQLException {
        try {
            String sql = "DELETE FROM books WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            close();
        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }

    //Filter
    public ArrayList<Book> serchBooks(BookSearchVM filter) throws SQLException {
        try {
            ArrayList<Book> books = new ArrayList<>();

            //Preparation de la requete
            StringBuilder sql = new StringBuilder("SELECT * FROM books b ");
            sql.append("JOIN authors a ON b.author_id = a.id ");
            sql.append("JOIN subjects s ON b.subject_id = s.id ");
            sql.append("WHERE 1=1 ");

            if (filter.getAuthorName() != null && !filter.getAuthorName().isEmpty() && !filter.getAuthorName().equals(" ")) {
                sql.append("AND a.last_name LIKE ? ");
            }
            if (filter.getTitle() != null && !filter.getTitle().isEmpty() && !filter.getTitle().equals(" ")) {
                sql.append("AND b.title LIKE ? ");
            }
            if (filter.getSubjectName() != null && !filter.getSubjectName().isEmpty() && !filter.getSubjectName().equals(" ")) {
                sql.append("AND s.name LIKE ? ");
            }
            if (filter.getMaxPrice() != null && !filter.getMaxPrice().equals(0f)) {
                sql.append("AND b.price <= ? ");
            }


            connexion =new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql.toString());
            int i = 1;

            if (filter.getAuthorName() != null && !filter.getAuthorName().isEmpty() && !filter.getAuthorName().equals(" ")) {
                ps.setString(i++, "%" + filter.getAuthorName() + "%");
            }
            if (filter.getTitle() != null && !filter.getTitle().isEmpty() && !filter.getTitle().equals(" ")) {
                ps.setString(i++, "%" + filter.getTitle() + "%");
            }
            if (filter.getSubjectName() != null && !filter.getSubjectName().isEmpty() && !filter.getSubjectName().equals(" ")) {
                ps.setString(i++, "%" + filter.getSubjectName() + "%");
            }
            if (filter.getMaxPrice() != null && !filter.getMaxPrice().equals(0f)) {
                ps.setFloat(i++, filter.getMaxPrice());
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getInt("subject_id"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getInt("page_count"),
                        rs.getInt("stock_quantity"),
                        rs.getFloat("price"),
                        rs.getInt("publish_year"),
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getString("name")
                ));
            }
            close();
            return books;

        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }


}
