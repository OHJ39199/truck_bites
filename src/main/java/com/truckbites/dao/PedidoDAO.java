package com.truckbites.dao;

import com.truckbites.database.DatabaseConnection;
import com.truckbites.model.Pedido;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    // Operaciones CRUD completas

    public Pedido getPedidoById(int pedidoId) throws SQLException {
        String sql = "SELECT * FROM Pedido WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedidoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPedido(rs);
            }
            return null;
        }
    }

    public List<Pedido> getAllPedidos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM Pedido";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pedidos.add(mapResultSetToPedido(rs));
            }
        }
        return pedidos;
    }

    public int createPedido(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO Pedido (usuario_id, food_truck_id, fecha_pedido, estado, datos, total) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pedido.getUsuarioId());
            stmt.setInt(2, pedido.getFoodTruckId());
            stmt.setTimestamp(3, Timestamp.valueOf(pedido.getFechaPedido()));
            stmt.setString(4, pedido.getEstado());
            stmt.setString(5, pedido.getDatos());
            stmt.setDouble(6, pedido.getTotal());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al crear pedido, ninguna fila afectada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    conn.commit();
                    return generatedKeys.getInt(1);
                } else {
                    conn.rollback();
                    throw new SQLException("Error al obtener ID generado.");
                }
            }
        }
    }

    public boolean updatePedidoEstado(int pedidoId, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Pedido SET estado = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, pedidoId);

            int affectedRows = stmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }

    public boolean deletePedido(int pedidoId) throws SQLException {
        String sql = "DELETE FROM Pedido WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedidoId);
            int affectedRows = stmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }

    public List<Pedido> getPedidosByUsuario(int usuarioId) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM Pedido WHERE usuario_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                pedidos.add(mapResultSetToPedido(rs));
            }
        }
        return pedidos;
    }

    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
        return new Pedido(
                rs.getInt("id"),
                rs.getInt("usuario_id"),
                rs.getInt("food_truck_id"),
                rs.getTimestamp("fecha_pedido").toLocalDateTime(),
                rs.getString("estado"),
                rs.getString("datos"),
                rs.getDouble("total")
        );
    }

    public List<Object[]> getVentasPorAnio(int anio) throws SQLException {
        List<Object[]> ventas = new ArrayList<>();
        String sql = "SELECT f.nombre AS food_truck_nombre, SUM(p.total) AS total_ventas " +
                "FROM Pedido p " +
                "JOIN FoodTruck f ON p.food_truck_id = f.id " +
                "WHERE YEAR(p.fecha_pedido) = ? " +
                "GROUP BY f.id " +
                "ORDER BY total_ventas DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, anio);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nombreFoodTruck = rs.getString("food_truck_nombre");
                    double totalVentas = rs.getDouble("total_ventas");
                    ventas.add(new Object[]{nombreFoodTruck, totalVentas});
                }
            }
        }
        return ventas;
    }

    public List<Pedido> getPedidosByFoodTruck(int foodTruckId, String estado) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM Pedido WHERE food_truck_id = ? AND estado = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, foodTruckId);
            stmt.setString(2, estado);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapResultSetToPedido(rs));
                }
            }
        }
        return pedidos;
    }
}