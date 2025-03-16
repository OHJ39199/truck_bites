package dao;

import com.truckbites.dao.NotificacionDAO;
import com.truckbites.model.Notificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacionDAOTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private NotificacionDAO notificacionDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        notificacionDAO = new NotificacionDAO();
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void testGetNotificacionesByUsuario() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getInt("usuario_id")).thenReturn(1, 1);
        when(mockResultSet.getString("mensaje")).thenReturn("Test Message 1", "Test Message 2");
        when(mockResultSet.getTimestamp("fecha_creacion")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockResultSet.getBoolean("leida")).thenReturn(false, false);

        List<Notificacion> result = notificacionDAO.getNotificacionesByUsuario(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Message 1", result.get(0).getMensaje());
        assertEquals("Test Message 2", result.get(1).getMensaje());
    }

    @Test
    void testCreateNotificacion() throws SQLException {
        Notificacion notificacion = new Notificacion(1, "Test Message");
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        int result = notificacionDAO.createNotificacion(notificacion);

        assertEquals(1, result);
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setString(2, "Test Message");
        verify(mockPreparedStatement).setBoolean(4, false);
    }
}
