package model.dao;

import model.entity.Caddies;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static model.dao.ConnectDB.close;

/*
Pour le read par id ou clientId
Pour l'Update si on veut update le montant et ici on va juste passer l'id et le montant à mettre en plus par exemple (1, 50) et dans le caddies 1 on a un amount de 25 il deviendra 75
Pour l'Update si on veut update le payed on passe juste en parametre l'id il va se mettre en true
*/

public class CaddiesDAO {
    private static CaddiesDAO instance;
    private ConnectDB connexion;

    private CaddiesDAO() {}

    public static CaddiesDAO getInstance() {
        if (instance == null) {
            instance = new CaddiesDAO();
        }
        return instance;
    }

    //CREATE
    public void addCaddies(Caddies caddies) throws SQLException {
        try {
            String sql = "INSERT INTO caddies (clientId, date, amount, payed) VALUES (?,?,?,?)";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, caddies.getCaddyId());
            if (caddies.getDate() == null) {
                caddies.setDate(LocalDate.now().toString());
            }
            ps.setString(2, caddies.getDate());
            ps.setDouble(3, caddies.getAmount());
            if (caddies.isPayed() == null) {
                caddies.setPayed(false);
            }
            ps.setBoolean(4, caddies.isPayed());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                caddies.setId(rs.getInt(1));
            }
            close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //READ ONE by Id
    public Caddies getCaddiesById(int id) throws SQLException {
        try {
            String sql = "SELECT * FROM caddies WHERE id=?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Caddies(
                        rs.getInt("id"),
                        rs.getInt("clientId"),
                        rs.getString("date"),
                        rs.getDouble("amount"),
                        rs.getBoolean("payed")
                );
            }
            close();
            return null;
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //READ ONE by ClientID
    public ArrayList<Caddies> getCaddiesByClientId(int clientId) throws SQLException {
        try {
            ArrayList<Caddies> caddiesList = new ArrayList<>();
            String sql = "SELECT * FROM caddies WHERE clientId=?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caddiesList.add(new Caddies(
                        rs.getInt("id"),
                        rs.getInt("clientId"),
                        rs.getString("date"),
                        rs.getInt("amount"),
                        rs.getBoolean("payed")
                ));
            }
            close();
            return caddiesList;
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //UPDATE Amount
    public void updateCaddiesAmount(int id, float amountToAdd) throws SQLException {
        try {
            String getAmountsql = "SELECT amount FROM caddies WHERE id=?";
            connexion = new ConnectDB();
            PreparedStatement getAmountps = connexion.getConnection().prepareStatement(getAmountsql);
            getAmountps.setInt(1, id);
            ResultSet rs = getAmountps.executeQuery();

            float currentAmount = 0.0f;
            if (rs.next()) {
                currentAmount = rs.getFloat("amount");
            }

            currentAmount += amountToAdd;

            String sql = "UPDATE caddies SET amount=? WHERE id=?";
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setDouble(1, currentAmount);
            ps.setInt(2, id);
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //UPDATE Payed
    public void updateCaddiesPayed(int id) throws SQLException {
        try {
            String getPayedsql = "UPDATE caddies SET payed=? WHERE id=?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(getPayedsql);
            ps.setBoolean(1, true);
            ps.setInt(2, id);
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //DELETE
    public void deleteCaddies(int id) throws SQLException {
        try {
            String sql = "DELETE FROM caddies WHERE id=?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    // DELETE CaddyItems by Caddy ID
    public void deleteCaddyItemsByCaddyId(int caddyId) throws SQLException {
        try {
            String sql = "DELETE FROM caddy_items WHERE caddy_id=?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, caddyId);
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la suppression des CaddyItems", e);
        }
    }

}
