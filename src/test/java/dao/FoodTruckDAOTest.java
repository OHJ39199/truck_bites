package dao;

import com.truckbites.dao.FoodTruckDAO;
import com.truckbites.model.FoodTruck;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class FoodTruckDAOTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private FoodTruckDAO foodTruckDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        foodTruckDAO = new FoodTruckDAO();
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    }

    @Test
    public void testGetFoodTruckById() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("nombre")).thenReturn("Test FoodTruck");
        when(mockResultSet.getString("descripcion")).thenReturn("Test Description");
        when(mockResultSet.getInt("propietario_id")).thenReturn(1);

        FoodTruck result = foodTruckDAO.getFoodTruckById(1);

        assertNotNull(result);
        assertEquals(Optional.of(1), result.getId());
        assertEquals("Test FoodTruck", result.getNombre());
    }

    @Test
    public void testCreateFoodTruck() throws SQLException {
        FoodTruck foodTruck = new FoodTruck("New FoodTruck", "Description", 1);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        int result = foodTruckDAO.createFoodTruck(foodTruck);

        assertEquals(1, result);
        verify(mockPreparedStatement).setString(1, "New FoodTruck");
        verify(mockPreparedStatement).setString(2, "Description");
        verify(mockPreparedStatement).setInt(3, 1);
    }
}
