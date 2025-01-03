package model.dao;

import model.entity.Author;
import model.entity.Subject;
import model.viewmodel.AuthorSearchVM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static model.dao.ConnectDB.close;

public class AuthorDAO {
    private static AuthorDAO instance;
    private ConnectDB connexion;
    private AuthorDAO() {}

    public static synchronized AuthorDAO getInstance() {
        if (instance == null) {
            instance = new AuthorDAO();
        }
        return instance;
    }

    //CREATE
    public void addAuthor(Author author) throws SQLException {
        try {
            String sql = "INSERT INTO authors (last_name, first_name, birth_date) VALUES (?,?,?)";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setString(1, author.getLastName());
            ps.setString(2, author.getFirstSurname());
            ps.setString(3, author.getBirthdate());
            ps.executeUpdate();
            close();// ferme la connexion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //READ ALL
    public ArrayList<Author> getAllAuthors() throws SQLException {
        try {
            ArrayList<Author> authors = new ArrayList<>();
            String sql = "SELECT * FROM authors";

            connexion = new ConnectDB();
            Statement stmt = connexion.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                authors.add(new Author(
                        rs.getInt("id"),
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getString("birth_date")
                ));
            }
            close();
            return authors;

        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }

    //READ ONE
    public Author getAuthorById(int id) throws SQLException {
        try {
            String sql = "SELECT * FROM authors WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Author(
                        rs.getInt("id"),
                        rs.getString("last_name"),
                        rs.getString("first_name"),
                        rs.getString("birth_date")
                );
            }
            close();
            return null;
        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }

    //UPDATE
    public void updateAuthor(Author author) throws SQLException {
        try {
            String sql = "UPDATE authors SET last_name = ?, first_name = ?, birth_date = ? WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setString(1, author.getLastName());
            ps.setString(2, author.getFirstSurname());
            ps.setString(3, author.getBirthdate());
            ps.setInt(4, author.getId());
            ps.executeUpdate();
            close();
        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }

    //DELETE
    public void deleteAuthor(int id) throws SQLException {
        try {
            String sql = "DELETE FROM authors WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            close();
        } catch ( SQLException e ) {
            throw new SQLException();
        }
    }

    //FILTER
    public ArrayList<Author> searchSubject(AuthorSearchVM filter) throws SQLException {
        ArrayList<Author> Author = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM authors WHERE 1=1 ");
        if(filter.getNom() != null && !filter.getNom().isEmpty() && !filter.getNom().equals(" ")) {
            sql.append("AND last_name LIKE ? ");
        }
        if(filter.getPrenom() != null && !filter.getPrenom().isEmpty() && !filter.getPrenom().equals(" ")) {
            sql.append("AND first_name LIKE ? ");
        }

        connexion =new ConnectDB();
        PreparedStatement ps = connexion.getConnection().prepareStatement(sql.toString());
        int i = 1;

        if(filter.getNom() != null && !filter.getNom().isEmpty() && !filter.getNom().equals(" "))
        {
            ps.setString(i++, "%"+filter.getNom()+"%");
        }
        if(filter.getPrenom() != null && !filter.getPrenom().isEmpty() && !filter.getPrenom().equals(" ")) {
            ps.setString(i++, "%"+filter.getPrenom()+"%");
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Author.add(new Author(
                    rs.getInt("id"),
                    rs.getString("last_name"),
                    rs.getString("first_name"),
                    rs.getString("birth_date")
            ));
        }
        close();
        return Author;
    }
}
