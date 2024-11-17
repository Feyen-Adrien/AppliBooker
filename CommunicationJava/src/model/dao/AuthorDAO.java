package model.dao;

import model.entity.Author;


import java.sql.*;
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
}
