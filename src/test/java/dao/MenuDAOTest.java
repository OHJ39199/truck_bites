package dao;

import com.truckbites.dao.MenuDAO;
import com.truckbites.model.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuDAOTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private MenuDAO menuDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        menuDAO = new MenuDAO();
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    void testGetMenuById() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("food_truck_id")).thenReturn(1);
        when(mockResultSet.getString("nombre")).thenReturn("Test Menu");
        when(mockResultSet.getString("descripcion")).thenReturn("Test Description");
        when(mockResultSet.getBigDecimal("precio")).thenReturn(new BigDecimal("10.99"));

        Menu result = menuDAO.getMenuById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Menu", result.getNombre());
        assertEquals(new BigDecimal("10.99"), result.getPrecio());
    }

    @Test
    void testCreateMenu() throws SQLException {
        Menu menu = new Menu(1, "New Menu", "Description", new BigDecimal("15.99"));
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        int result = menuDAO.createMenu(menu);

        assertEquals(1, result);
        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setString(2, "New Menu");
        verify(mockPreparedStatement).setBigDecimal(4, new BigDecimal("15.99"));
    }

    @Test
    void testGetMenusByFoodTruck() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getInt("food_truck_id")).thenReturn(1, 1);
        when(mockResultSet.getString("nombre")).thenReturn("Menu 1", "Menu 2");
        when(mockResultSet.getString("descripcion")).thenReturn("Desc 1", "Desc 2");
        when(mockResultSet.getBigDecimal("precio")).thenReturn(new BigDecimal("10.99"), new BigDecimal("12.99"));

        List<Menu> result = menuDAO.getMenusByFoodTruck(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Menu 1", result.get(0).getNombre());
        assertEquals("Menu 2", result.get(1).getNombre());
    }
}
