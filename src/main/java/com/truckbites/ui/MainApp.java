package com.truckbites.ui;

import com.truckbites.dao.*;
import com.truckbites.database.DatabaseConnection;
import java.sql.*;
import java.util.*;
import com.truckbites.service.GestorTruckBites;


public class MainApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final FoodTruckDAO foodTruckDAO = new FoodTruckDAO();
    public static final NotificacionDAO notificacionDAO = new NotificacionDAO();
    private static final GestorTruckBites gestor = new GestorTruckBites();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    gestionarUsuarios();
                    break;
                case 2:
                    gestionarFoodTrucks();
                    break;
                case 3:
                    gestionarSistema();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n=== Menú Principal ===");
        System.out.println("1. Gestionar Usuarios");
        System.out.println("2. Gestionar Food Trucks");
        System.out.println("3. Gestionar Sistema");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void gestionarUsuarios() {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Usuarios ===");
            System.out.println("1. Crear Usuario");
            System.out.println("2. Actualizar Usuario");
            System.out.println("3. Borrar Usuario");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    UsuarioDAO.createUsuario();
                    break;
                case 2:
                    UsuarioDAO.updateUsuario();
                    break;
                case 3:
                    UsuarioDAO.deleteUsuario();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private static void gestionarFoodTrucks() {
        int opcion;
        do {
            System.out.println("\n=== Gestión de Food Trucks ===");
            System.out.println("1. Crear Food Truck");
            System.out.println("2. Actualizar Food Truck");
            System.out.println("3. Borrar Food Truck");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    crearFoodTruck();
                    break;
                case 2:
                    actualizarFoodTruck();
                    break;
                case 3:
                    borrarFoodTruck();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private static void gestionarSistema() {
        int opcion;
        do {
            System.out.println("\n=== Gestión del Sistema ===");
            System.out.println("1. Listar todos los food trucks");
            System.out.println("2. Food trucks cercanos");
            System.out.println("3. Menú diario de food truck");
            System.out.println("4. Pedidos pendientes de entrega o en preparación por food truck");
            System.out.println("5. Ventas por año");
            System.out.println("6. Notificaciones de usuario");
            System.out.println("7. Food trucks populares");
            System.out.println("8. Usuarios frecuentes");
            System.out.println("9. Menús populares");
            System.out.println("10. Food trucks con más reservas");
            System.out.println("11. Usuarios activos recientes");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            try {
                switch (opcion) {
                    case 1:
                        gestor.listarFoodTrucks();
                        break;
                    case 2:
                        gestor.consultarFoodTrucksCercanos();
                        break;
                    case 3:
                        gestor.listarMenuDiario();
                        break;
                    case 4:
                        gestor.listarPedidosPendientes();
                        break;
                    case 5:
                        gestor.calcularVentasPeriodo();
                        break;
                    case 6:
                        gestor.listarNotificacionesUsuario();
                        break;
                    case 7:
                        gestor.listarFoodTrucksPopulares();
                        break;
                    case 8:
                        gestor.listarUsuariosFrecuentes();
                        break;
                    case 9:
                        gestor.listarMenusPopulares();
                        break;
                    case 10:
                        gestor.listarReservasMensuales();
                        break;
                    case 11:
                        gestor.obtenerUsuariosRecientes();
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente de nuevo.");
                }
            } catch (SQLException e) {
                System.out.println("Error al ejecutar la operación: " + e.getMessage());
            }
        } while (opcion != 0);
    }
}