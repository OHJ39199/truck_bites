package com.truckbites.ui;

import com.truckbites.dao.*;
import com.truckbites.database.DatabaseConnection;
import java.sql.*;
import java.util.*;
import com.truckbites.service.GestorTruckBites;


public class MainApp {
    private static final Scanner scanner = new Scanner(System.in);
    public static final NotificacionDAO notificacionDAO = new NotificacionDAO();
    private static final GestorTruckBites gestor = new GestorTruckBites();

    public static void main(String[] args) {
        try {
            int opcion;
            do {
                System.out.println("\n=== Sistema de Gestión TruckBites ===");
                System.out.println("1. Lista de todos los food trucks");
                System.out.println("2. Food trucks cercanos");
                System.out.println("3. Menú diario de food truck");
                System.out.println("4. Pedidos pendientes de entrega o en preparaccion por food truck");
                System.out.println("5. Ventas por año");
                System.out.println("6. Notificaciones de usuario");
                System.out.println("7. Food trucks populares");
                System.out.println("8. Usuarios frecuentes");
                System.out.println("9. Menús populares");
                System.out.println("10. Food trucks con más reservas");
                System.out.println("11. Usuarios activos recientes");
                System.out.println("12. Pedidos entregados a un usuario");
                System.out.println("13. Gasto medio de un usuario");
                System.out.println("0. Salir");
                System.out.print("Seleccione opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch(opcion) {
                    case 1 -> gestor.listarFoodTrucks();
                    case 2 -> gestor.consultarFoodTrucksCercanos();
                    case 3 -> gestor.listarMenuDiario();
                    case 4 -> gestor.listarPedidosPendientes();
                    case 5 -> gestor.calcularVentasPeriodo();
                    case 6 -> gestor.listarNotificacionesUsuario();
                    case 7 -> gestor.listarFoodTrucksPopulares();
                    case 8 -> gestor.listarUsuariosFrecuentes();
                    case 9 -> gestor.listarMenusPopulares();
                    case 10 -> gestor.listarReservasMensuales();
                    case 11 -> gestor.obtenerUsuariosRecientes();
                    case 12-> gestor.listarPedidosEntregadosPorUsuario();
                    case 13-> gestor.calcularGastoMedioUsuario();

                }
            } while(opcion != 0);

        } catch(SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection();
        }
    }
}