package service;

import com.truckbites.dao.*;
import com.truckbites.model.*;
import com.truckbites.service.GestorTruckBites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestorTruckBitesTest {

    @Mock
    private FoodTruckDAO mockFoodTruckDAO;
    @Mock
    private MenuDAO mockMenuDAO;
    @Mock
    private PedidoDAO mockPedidoDAO;
    @Mock
    private UsuarioDAO mockUsuarioDAO;
    @Mock
    private NotificacionDAO mockNotificacionDAO;

    private GestorTruckBites gestor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gestor = new GestorTruckBites();
        // inyectar los mocks en el gestor
        // gestor.setFoodTruckDAO(mockFoodTruckDAO);
        // hay que tener setters en GestorTruckBites para esto
    }

    @Test
    void testListarFoodTrucks() throws SQLException {
        List<FoodTruck> mockFoodTrucks = Arrays.asList(
                new FoodTruck(1, "FoodTruck 1", "Desc 1", 1),
                new FoodTruck(2, "FoodTruck 2", "Desc 2", 2)
        );
        when(mockFoodTruckDAO.getAllFoodTrucks()).thenReturn(mockFoodTrucks);

        gestor.listarFoodTrucks();

        verify(mockFoodTruckDAO).getAllFoodTrucks();
        // agregar más aserciones si el método devuelve algo
    }

    @Test
    void testConsultarFoodTrucksCercanos() throws SQLException {
        // Implementa test utilizando la lógica de consultarFoodTrucksCercanos
    }

    @Test
    void testListarMenuDiario() throws SQLException {
        int foodTruckId = 1;
        List<Menu> mockMenus = Arrays.asList(
                new Menu(1, foodTruckId, "Menu 1", "Desc 1", new BigDecimal("10.99")),
                new Menu(2, foodTruckId, "Menu 2", "Desc 2", new BigDecimal("12.99"))
        );
        when(mockMenuDAO.getMenusByFoodTruck(foodTruckId)).thenReturn(mockMenus);

        gestor.listarMenuDiario();

        verify(mockMenuDAO).getMenusByFoodTruck(foodTruckId);
        // Agrega más según sea necesario
    }

    @Test
    void testListarPedidosPendientes() throws SQLException {
        int foodTruckId = 1;
        List<Pedido> mockPedidos = Arrays.asList(
                new Pedido(1, 1, foodTruckId, LocalDateTime.now(), "pendiente", "2 hamburguesas", 20.0),
                new Pedido(2, 2, foodTruckId, LocalDateTime.now(), "en preparación", "1 pizza", 15.0)
        );
        when(mockPedidoDAO.getPedidosByFoodTruck(foodTruckId, "pendiente")).thenReturn(mockPedidos);

        gestor.listarPedidosPendientes();

        verify(mockPedidoDAO).getPedidosByFoodTruck(foodTruckId, "pendiente");
    }

    @Test
    void testCalcularVentasPeriodo() throws SQLException {
        int anio = 2025;
        List<Object[]> mockVentas = Arrays.asList(
                new Object[]{"FoodTruck 1", 1000.0},
                new Object[]{"FoodTruck 2", 1500.0}
        );
        when(mockPedidoDAO.getVentasPorAnio(anio)).thenReturn(mockVentas);

        gestor.calcularVentasPeriodo();

        verify(mockPedidoDAO).getVentasPorAnio(anio);
    }

    @Test
    void testListarNotificacionesUsuario() throws SQLException {
        int usuarioId = 1;
        List<Notificacion> mockNotificaciones = Arrays.asList(
                new Notificacion(1, usuarioId, "Notificación 1", LocalDateTime.now(), false),
                new Notificacion(2, usuarioId, "Notificación 2", LocalDateTime.now(), false)
        );
        when(mockNotificacionDAO.getNotificacionesNoLeidas(usuarioId)).thenReturn(mockNotificaciones);

        gestor.listarNotificacionesUsuario();

        verify(mockNotificacionDAO).getNotificacionesNoLeidas(usuarioId);
    }

}
