package com.truckbites.dao;

import com.truckbites.database.DatabaseConnection;
import com.truckbites.model.Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class MenuDAO {

    public Menu getMenuById(int menuId) throws SQLException {
        String sql = "SELECT * FROM Menu WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, menuId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMenu(rs);
            }
            return null;
        }
    }

    public List<Menu> getMenusByFoodTruck(int foodTruckId) throws SQLException {
        List<Menu> menus = new ArrayList<>();
        String sql = "SELECT * FROM Menu WHERE food_truck_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, foodTruckId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                menus.add(mapResultSetToMenu(rs));
            }
        }
        return menus;
    }

    public int createMenu(Menu menu) throws SQLException {
        String sql = "INSERT INTO Menu (food_truck_id, nombre, descripcion, precio) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, menu.getFoodTruckId());
            stmt.setString(2, menu.getNombre());
            stmt.setString(3, menu.getDescripcion());
            stmt.setBigDecimal(4, menu.getPrecio());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating menu failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    conn.commit();
                    return generatedKeys.getInt(1);
                } else {
                    conn.rollback();
                    throw new SQLException("Creating menu failed, no ID obtained.");
                }
            }
        }
    }

    public boolean updateMenu(Menu menu) throws SQLException {
        String sql = "UPDATE Menu SET food_truck_id = ?, nombre = ?, descripcion = ?, precio = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, menu.getFoodTruckId());
            stmt.setString(2, menu.getNombre());
            stmt.setString(3, menu.getDescripcion());
            stmt.setBigDecimal(4, menu.getPrecio());
            stmt.setInt(5, menu.getId());

            int affectedRows = stmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }

    public boolean deleteMenu(int menuId) throws SQLException {
        String sql = "DELETE FROM Menu WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, menuId);
            int affectedRows = stmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }
//    // he eliminado la opcion boolean de disponibilidad de las tablas
//    public List<Menu> getMenusDisponibles(int foodTruckId) throws SQLException {
//        List<Menu> menus = new ArrayList<>();
//        String sql = "SELECT * FROM Menu WHERE food_truck_id = ? AND disponible = TRUE";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setInt(1, foodTruckId);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                menus.add(mapResultSetToMenu(rs));
//            }
//        }
//        return menus;
//    }


    private Menu mapResultSetToMenu(ResultSet rs) throws SQLException {
        return new Menu(
                rs.getInt("id"),
                rs.getInt("food_truck_id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBigDecimal("precio")
        );
    }

    private void setMenuParameters(PreparedStatement stmt, Menu menu) throws SQLException {
        stmt.setInt(1, menu.getFoodTruckId());
        stmt.setString(2, menu.getNombre());
        stmt.setString(3, menu.getDescripcion());
        stmt.setBigDecimal(4, menu.getPrecio());
    }
}
