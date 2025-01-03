package model.dao;

import model.entity.CaddyItems;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static model.dao.ConnectDB.close;
/*
UPDATE par rapport au book comme ça on augmente juste la quantité si besoin est avec CaddyItemId amountadd
*/


public class CaddyItemsDAO {
    private static CaddyItemsDAO instance;
    private ConnectDB connexion;

    private CaddyItemsDAO() {}

    public static CaddyItemsDAO getInstance() {
        if (instance == null) {
            instance = new CaddyItemsDAO();
        }
        return instance;
    }

    //CREATE
    public void addCaddyItems (CaddyItems caddyItems) throws SQLException {
        try {
            String sql = "INSERT INTO caddy_items (caddyId, bookId, quantity) VALUES (?,?,?)";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, caddyItems.getCaddyId());
            ps.setInt(2, caddyItems.getBookId());
            ps.setInt(3, caddyItems.getQuantity());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                caddyItems.setId(rs.getInt(1));
            }
            close();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //READ ID
    public CaddyItems getCaddyItemsbyId(int caddyId) throws SQLException {
        try {
            String sql = "SELECT * FROM caddy_items WHERE id = ?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, caddyId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                return new CaddyItems(
                        rs.getInt("id"),
                        rs.getInt("caddyId"),
                        rs.getInt("bookId"),
                        rs.getInt("quantity"));
            }
            close();
            return null;
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //READ a LOT It depends
    public ArrayList<CaddyItems> getCaddyItems (int NumeroCaddy) throws SQLException {

        try {
            ArrayList<CaddyItems> caddyItemsList = new ArrayList<>();

            StringBuilder sql = new StringBuilder("SELECT * FROM caddy_items ");
            ArrayList<Integer> params = new ArrayList<>();
            sql.append(" WHERE 1=1");
            if (NumeroCaddy != -1) {
                sql.append(" AND caddyId=?");
                params.add(NumeroCaddy);
            }
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql.toString());

            for (int i=0; i < params.size(); i++) {
                ps.setInt(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caddyItemsList.add(new CaddyItems(
                        rs.getInt("id"),
                        rs.getInt("caddyId"),
                        rs.getInt("bookId"),
                        rs.getInt("quantity")));
            }

            close();
            return caddyItemsList;

        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //UPDATE +1
    public void updateCaddyItemsPlusOne (int id) throws SQLException {
        try {
            String getQuantitysql = "SELECT quantity FROM caddy_items WHERE id=?";
            connexion = new ConnectDB();
            PreparedStatement getQuantityps = connexion.getConnection().prepareStatement(getQuantitysql);
            getQuantityps.setInt(1, id);
            ResultSet rs = getQuantityps.executeQuery();

            int currentQuantity = 0;
            if (rs.next()) {
                currentQuantity = rs.getInt("quantity");
            }

            currentQuantity ++;

            String sql = "UPDATE caddy_items SET quantity=? WHERE id=?";
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, currentQuantity);
            ps.setInt(2, id);
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        }
    }

    //UPDATE -1
    public void updateCaddyItemsMinusOne (int id) throws SQLException {
        try {
            String getQuantitysql = "SELECT quantity FROM caddy_items WHERE id=?";
            connexion = new ConnectDB();
            PreparedStatement getQuantityps = connexion.getConnection().prepareStatement(getQuantitysql);
            getQuantityps.setInt(1, id);
            ResultSet rs = getQuantityps.executeQuery();

            int currentQuantity = 0;
            if (rs.next()) {
                currentQuantity = rs.getInt("quantity");
            }

            currentQuantity --;

            String sql = "UPDATE caddy_items SET quantity=? WHERE id=?";
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, currentQuantity);
            ps.setInt(2, id);
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    //DELETE
    public void deleteCaddyItems (int id) throws SQLException {
        try {
            String sql = "DELETE FROM caddy_items WHERE id=?";
            connexion = new ConnectDB();
            PreparedStatement ps = connexion.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            close();
        } catch (SQLException e) {
            throw new SQLException();
        }
    }
}
