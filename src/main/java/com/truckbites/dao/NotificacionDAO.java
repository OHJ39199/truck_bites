package com.truckbites.dao;

import com.truckbites.database.DatabaseConnection;
import com.truckbites.model.Notificacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificacionDAO {

    // Obtener una notificación por su ID
    public Notificacion getNotificacionById(int notificacionId) throws SQLException {
        String sql = "SELECT * FROM Notificacion WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificacionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToNotificacion(rs);
            }
            return null;
        }
    }

    // Obtener todas las notificaciones de un usuario
    public List<Notificacion> getNotificacionesByUsuario(int usuarioId) throws SQLException {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion WHERE usuario_id = ? ORDER BY fecha_creacion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notificaciones.add(mapResultSetToNotificacion(rs));
            }
        }
        return notificaciones;
    }

    // Obtener las notificaciones no leídas de un usuario
    public List<Notificacion> getNotificacionesNoLeidas(int usuarioId) throws SQLException {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion WHERE usuario_id = ? AND leida = FALSE ORDER BY fecha_creacion DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notificaciones.add(mapResultSetToNotificacion(rs));
            }
        }
        return notificaciones;
    }

    // Crear una nueva notificación
    public int createNotificacion(Notificacion notificacion) throws SQLException {
        String sql = "INSERT INTO Notificacion (usuario_id, mensaje, fecha_creacion, leida) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, notificacion.getUsuarioId());
            stmt.setString(2, notificacion.getMensaje());
            stmt.setTimestamp(3, Timestamp.valueOf(notificacion.getFechaCreacion()));
            stmt.setBoolean(4, notificacion.getLeida());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating notification failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    conn.commit();
                    return generatedKeys.getInt(1);
                } else {
                    conn.rollback();
                    throw new SQLException("Creating notification failed, no ID obtained.");
                }
            }
        }
    }

    // Marcar una notificación como leída
    public boolean marcarComoLeida(int notificacionId) throws SQLException {
        String sql = "UPDATE Notificacion SET leida = TRUE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificacionId);

            int affectedRows = stmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }

    // Eliminar una notificación por su ID
    public boolean deleteNotificacion(int notificacionId) throws SQLException {
        String sql = "DELETE FROM Notificacion WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificacionId);

            int affectedRows = stmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            DatabaseConnection.rollback();
            throw e;
        }
    }

    // Mapeo de ResultSet a objeto Notificación
    private Notificacion mapResultSetToNotificacion(ResultSet rs) throws SQLException {
        return new Notificacion(
                rs.getInt("id"),
                rs.getInt("usuario_id"),
                rs.getString("mensaje"),
                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
                rs.getBoolean("leida")
        );
    }
}