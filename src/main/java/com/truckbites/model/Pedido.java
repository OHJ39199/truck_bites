package com.truckbites.model;

import java.time.LocalDateTime;

public class Pedido {
    private int id;
    private int usuarioId;
    private int foodTruckId;
    private LocalDateTime fechaPedido;
    private String estado;
    private String datos;
    private double total;

    // Constructor completo
    public Pedido(int id, int usuarioId, int foodTruckId, LocalDateTime fechaPedido,
                  String estado, String datos, double total) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.foodTruckId = foodTruckId;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.datos = datos;
        this.total = total;
    }

    // Constructor sin ID para inserciones
    public Pedido(int usuarioId, int foodTruckId, LocalDateTime now, String estado,
                  String datos, double total) {
        this.usuarioId = usuarioId;
        this.foodTruckId = foodTruckId;
        this.estado = estado;
        this.datos = datos;
        this.total = total;
        this.fechaPedido = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getFoodTruckId() { return foodTruckId; }
    public void setFoodTruckId(int foodTruckId) { this.foodTruckId = foodTruckId; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDatos() { return datos; }
    public void setDatos(String datos) { this.datos = datos; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    @Override
    public String toString() {
        return String.format("Pedido #%d - Estado: %s - Total: %.2f€", id, estado, total);
    }
}