package com.truckbites.service;

import com.truckbites.dao.*;
import com.truckbites.database.DatabaseConnection;
import com.truckbites.model.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import static com.truckbites.ui.MainApp.notificacionDAO;


public class GestorTruckBites {
    private static Scanner scanner = null;
    private final UsuarioDAO usuarioDAO;
    private final FoodTruckDAO foodTruckDAO;
    private static MenuDAO menuDAO = new MenuDAO();
    private final PedidoDAO pedidoDAO;

    public GestorTruckBites() {
        this.scanner = new Scanner(System.in);
        this.usuarioDAO = new UsuarioDAO();
        this.foodTruckDAO = new FoodTruckDAO();
        this.menuDAO = new MenuDAO();
        this.pedidoDAO = new PedidoDAO();
    }

    //listar todos los foodtrucks
    public void listarFoodTrucks() throws SQLException {
        List<FoodTruck> foodTrucks = foodTruckDAO.getAllFoodTrucks();
        System.out.println("\nLista de Food Trucks:");
        for (FoodTruck ft : foodTrucks) {
            System.out.println("-(ID: " + ft.getId() + ")"+ ft.getNombre()  );
        }
    }

    // 1. Obtener food trucks cercanos
    public static void consultarFoodTrucksCercanos() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la localidad: ");
        String localidad = scanner.nextLine().trim();

        System.out.print("Ingrese la calle (opcional, presione Enter para omitir): ");
        String calle = scanner.nextLine().trim();

        String sql = "SELECT f.id, f.nombre, u.localidad, u.calle, u.fecha_ini, u.fecha_fin " +
                "FROM FoodTruck f " +
                "JOIN Ubicacion u ON f.id = u.food_truck_id " +
                "WHERE u.localidad = ? " +
                (calle.isEmpty() ? "" : "AND u.calle LIKE ? ") +
                "AND u.fecha_ini <= NOW() AND u.fecha_fin >= NOW()";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, localidad);
            if (!calle.isEmpty()) {
                stmt.setString(2, "%" + calle + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\nFood trucks cercanos en " + localidad + ":");
                boolean encontrado = false;
                while (rs.next()) {
                    encontrado = true;
                    System.out.printf("- %s (ID: %d)%n", rs.getString("nombre"), rs.getInt("id"));
                    System.out.printf("  Ubicación: %s, %s%n", rs.getString("localidad"), rs.getString("calle"));
                    System.out.printf("  Disponible desde: %s hasta: %s%n",
                            rs.getTimestamp("fecha_ini").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                            rs.getTimestamp("fecha_fin").toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                    System.out.println();
                }
                if (!encontrado) {
                    System.out.println("No se encontraron food trucks en la ubicación especificada.");
                }
            }
        }
    }

    // 2. Listar productos menús  de un food truck
    public static void listarMenuDiario() throws SQLException {
        System.out.print("ID del food truck: ");
        int foodTruckId = scanner.nextInt();

        List<Menu> menus = menuDAO.getMenusByFoodTruck(foodTruckId);
        System.out.println("\nMenú:");
        menus.forEach(menu -> System.out.printf("- %s: %.2f€%n",
                menu.getNombre(),
                menu.getPrecio()));
    }

    // 3. Recuperar pedidos pendientes de preparacion o entrega para un food truck
    public static void listarPedidosPendientes() throws SQLException {
        System.out.print("ID del food truck: ");
        int foodTruckId = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        String sql = "SELECT p.id, p.estado, p.total, u.nombre AS nombre_usuario " +
                "FROM Pedido p " +
                "JOIN Usuario u ON p.usuario_id = u.id " +
                "WHERE p.food_truck_id = ? AND p.estado != 'entregado' " +
                "ORDER BY p.fecha_pedido ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, foodTruckId);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nPedidos pendientes:");
            boolean hayPedidos = false;
            while (rs.next()) {
                hayPedidos = true;
                System.out.printf("- Pedido #%d: %s%n", rs.getInt("id"), rs.getString("estado"));
                System.out.printf("  Cliente: %s%n", rs.getString("nombre_usuario"));
                System.out.printf("  Total: %.2f€%n", rs.getDouble("total"));
                System.out.println();
            }

            if (!hayPedidos) {
                System.out.println("No hay pedidos pendientes para este food truck.");
            }
        }
    }

    // 4. Calcular ventas totales por año
    public static void calcularVentasPeriodo() throws SQLException {
        System.out.print("Año para consultar ventas: ");
        int anio = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        String sql = "SELECT f.nombre, SUM(p.total) AS ventas " +
                "FROM Pedido p JOIN FoodTruck f ON p.food_truck_id = f.id " +
                "WHERE YEAR(p.fecha_pedido) = ? " +
                "GROUP BY f.id ORDER BY ventas DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, anio);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\nVentas por food truck en el año " + anio + ":");
                boolean hayResultados = false;
                while (rs.next()) {
                    hayResultados = true;
                    String nombreFoodTruck = rs.getString("nombre");
                    double ventasTotales = rs.getDouble("ventas");
                    System.out.printf("- %s: %.2f€%n", nombreFoodTruck, ventasTotales);
                }

                if (!hayResultados) {
                    System.out.println("No se encontraron ventas para el año " + anio + ".");
                }
            }
        }
    }

    // 5. Usuarios con pedidos recientes
    public static void obtenerUsuariosRecientes() throws SQLException {
        String sql = "SELECT DISTINCT u.* FROM Usuario u " +
                "JOIN Pedido p ON u.id = p.usuario_id " +
                "WHERE p.fecha_pedido >= CURDATE() - INTERVAL 7 DAY";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nUsuarios con pedidos recientes:");
            while(rs.next()) {
                System.out.printf("- %s (%s)%n",
                        rs.getString("nombre"),
                        rs.getString("email"));
            }
        }
    }

    // 6. Listar notificaciones activas
    public static void listarNotificacionesUsuario() throws SQLException {
        System.out.print("ID de usuario: ");
        int usuarioId = scanner.nextInt();

        List<Notificacion> notificaciones = notificacionDAO.getNotificacionesNoLeidas(usuarioId);
        System.out.println("\nNotificaciones activas:");
        notificaciones.forEach(n -> System.out.printf("- %s%n", n.getMensaje()));
    }

    // 7. Food trucks populares
    public static void listarFoodTrucksPopulares() throws SQLException {
        String sql = "SELECT f.nombre, COUNT(p.id) AS total_pedidos " +
                "FROM FoodTruck f LEFT JOIN Pedido p ON f.id = p.food_truck_id " +
                "GROUP BY f.id ORDER BY total_pedidos DESC LIMIT 5";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nFood trucks más populares:");
            int pos = 1;
            while(rs.next()) {
                System.out.printf("%d. %s (%d pedidos)%n",
                        pos++,
                        rs.getString("nombre"),
                        rs.getInt("total_pedidos"));
            }
        }
    }

    // 8. Usuarios frecuentes
    public static void listarUsuariosFrecuentes() throws SQLException {
        String sql = "SELECT u.nombre, COUNT(p.id) AS total_pedidos " +
                "FROM Usuario u LEFT JOIN Pedido p ON u.id = p.usuario_id " +
                "GROUP BY u.id ORDER BY total_pedidos DESC LIMIT 5";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nUsuarios más activos:");
            int pos = 1;
            while(rs.next()) {
                System.out.printf("%d. %s (%d pedidos)%n",
                        pos++,
                        rs.getString("nombre"),
                        rs.getInt("total_pedidos"));
            }
        }
    }

    // 9. Menús populares
    public static void listarMenusPopulares() throws SQLException {
        String sql = "SELECT m.nombre, COUNT(*) AS total_pedidos " +
                "FROM Menu m JOIN Pedido p ON m.food_truck_id = p.food_truck_id " +
                "GROUP BY m.id ORDER BY total_pedidos DESC LIMIT 5";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nMenús más populares:");
            int pos = 1;
            while(rs.next()) {
                System.out.printf("%d. %s (%d pedidos)%n",
                        pos++,
                        rs.getString("nombre"),
                        rs.getInt("total_pedidos"));
            }
        }
    }

    // 10. Reservas mensuales
    public static void listarReservasMensuales() throws SQLException {
        String sql = "SELECT YEAR(fecha_pedido) AS año, MONTH(fecha_pedido) AS mes, " +
                "f.nombre, COUNT(*) AS total_pedidos " +
                "FROM Pedido p JOIN FoodTruck f ON p.food_truck_id = f.id " +
                "GROUP BY año, mes, f.id ORDER BY año DESC, mes DESC, total_pedidos DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nPedidos por mes:");
            while(rs.next()) {
                System.out.printf("%d-%02d: %s (%d pedidos)%n",
                        rs.getInt("año"),
                        rs.getInt("mes"),
                        rs.getString("nombre"),
                        rs.getInt("total_pedidos"));
            }
        }
    }
}