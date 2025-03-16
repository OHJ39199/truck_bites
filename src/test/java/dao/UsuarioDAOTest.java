package dao;

import com.truckbites.dao.UsuarioDAO;
import com.truckbites.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioDAOTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        usuarioDAO = new UsuarioDAO();
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void testGetUsuarioById() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("nombre")).thenReturn("Test User");
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        when(mockResultSet.getString("password")).thenReturn("password123");
        when(mockResultSet.getTimestamp("fecha_registro")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        Usuario result = usuarioDAO.getUsuarioById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test User", result.getNombre());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testCreateUsuario() throws SQLException {
        Usuario usuario = new Usuario("New User", "new@example.com", "password123");
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        int result = usuarioDAO.createUsuario(usuario);

        assertEquals(1, result);
        verify(mockPreparedStatement).setString(1, "New User");
        verify(mockPreparedStatement).setString(2, "new@example.com");
        verify(mockPreparedStatement).setString(3, "password123");
    }

    @Test
    void testGetUsuarioByEmail() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("nombre")).thenReturn("Test User");
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        when(mockResultSet.getString("password")).thenReturn("password123");
        when(mockResultSet.getTimestamp("fecha_registro")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        Usuario result = usuarioDAO.getUsuarioByEmail("test@example.com");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test User", result.getNombre());
        assertEquals("test@example.com", result.getEmail());
    }
}