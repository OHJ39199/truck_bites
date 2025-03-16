package com.truckbites.model;

import java.math.BigDecimal;

public class Menu {
    private Integer id;
    private Integer foodTruckId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;

    // Constructor
    public Menu(Integer id, Integer foodTruckId, String nombre, String descripcion, BigDecimal precio) {
        this.id = id;
        this.foodTruckId = foodTruckId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Constructor sin ID para nuevos menús
    public Menu(Integer foodTruckId, String nombre, String descripcion, BigDecimal precio) {
        this(null, foodTruckId, nombre, descripcion, precio);
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFoodTruckId() {
        return foodTruckId;
    }

    public void setFoodTruckId(Integer foodTruckId) {
        this.foodTruckId = foodTruckId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return String.format("Menu{id=%d, foodTruckId=%d, nombre='%s', precio=%.2f}",
                id, foodTruckId, nombre, precio);
    }

    // Método para crear una copia del menú
    public Menu copy() {
        return new Menu(id, foodTruckId, nombre, descripcion, precio);
    }

    // Método para comparar menús (útil para operaciones de colecciones)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return id != null && id.equals(menu.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}