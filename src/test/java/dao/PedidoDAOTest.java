package dao;

import com.truckbites.dao.PedidoDAO;
import com.truckbites.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoDAOTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private PedidoDAO pedidoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        pedidoDAO = new PedidoDAO();
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void testGetPedidoById() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("usuario_id")).thenReturn(1);
        when(mockResultSet.getInt("food_truck_id")).thenReturn(1);
        when(mockResultSet.getTimestamp("fecha_pedido")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockResultSet.getString("estado")).thenReturn("pendiente");
        when(mockResultSet.getString("datos")).thenReturn("Test Data");
        when(mockResultSet.getDouble("total")).thenReturn(10.0);

        Pedido result = pedidoDAO.getPedidoById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("pendiente", result.getEstado());
    }

    @Test
    void testCreatePedido() throws SQLException {
        Pedido pedido = new Pedido(1, 1, LocalDateTime.now(), "pendiente", "Test Data", 10.0);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        int result = pedidoDAO.createPedido(pedido);

        assertEquals(1, result);
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setInt(2, 1);
        verify(mockPreparedStatement).setString(4, "pendiente");
    }
}
