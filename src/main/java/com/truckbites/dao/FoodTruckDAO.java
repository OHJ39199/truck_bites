package com.truckbites.dao;

import com.truckbites.database.DatabaseConnection;
import com.truckbites.model.FoodTruck;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodTruckDAO {

    public FoodTruck getFoodTruckById(int foodTruckId) throws SQLException {
        String sql = "SELECT * FROM FoodTruck WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, foodTruckId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFoodTruck(rs);
            }
            return null;
        }
    }

    public List<FoodTruck> getAllFoodTrucks() throws SQLException {
        List<FoodTruck> foodTrucks = new ArrayList<>();
        String sql = "SELECT DISTINCT ft.id, ft.nombre " +
                "FROM FoodTruck ft " +
                "LEFT JOIN Ubicacion u ON ft.id = u.food_truck_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                foodTrucks.add(new FoodTruck(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        null,  // We're not fetching description here
                        null   // We're not fetching propietario_id here
                ));
            }
        }
        return foodTrucks;
    }

    public int createFoodTruck(FoodTruck foodTruck) throws SQLException {
        String sql = "INSERT INTO FoodTruck (nombre, descripcion, propietario_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, foodTruck.getNombre());
            stmt.setString(2, foodTruck.getDescripcion());
            stmt.setInt(3, foodTruck.getPropietarioId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al crear el Food Truck: no se insertaron filas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retorna el ID generado
                } else {
                    throw new SQLException("Error al crear el Food Truck: no se generó ningún ID.");
                }
            }
        }
    }

    public boolean updateFoodTruck(FoodTruck foodTruck) throws SQLException {
        String sql = "UPDATE FoodTruck SET nombre = ?, descripcion = ?, propietario_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, foodTruck.getNombre());
            stmt.setString(2, foodTruck.getDescripcion());
            stmt.setInt(3, foodTruck.getPropietarioId());
            stmt.setInt(4, foodTruck.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Retorna true si se actualizó al menos una fila
        }
    }


    public boolean deleteFoodTruck(int foodTruckId) throws SQLException {
        String sql = "DELETE FROM FoodTruck WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, foodTruckId);
            int affectedRows = stmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }

    public List<FoodTruck> getFoodTrucksByLocalidad(String localidad) throws SQLException {
        List<FoodTruck> foodTrucks = new ArrayList<>();
        String sql = "SELECT DISTINCT ft.* FROM FoodTruck ft " +
                "JOIN Ubicacion u ON ft.id = u.food_truck_id " +
                "WHERE u.localidad = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, localidad);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                foodTrucks.add(mapResultSetToFoodTruck(rs));
            }
        }
        return foodTrucks;
    }

    private static FoodTruck mapResultSetToFoodTruck(ResultSet rs) throws SQLException {
        return new FoodTruck(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getInt("propietario_id")
        );
    }
}