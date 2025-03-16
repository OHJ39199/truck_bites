package com.truckbites.model;

public class FoodTruck {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer propietarioId;

    public FoodTruck(Integer id, String nombre, String descripcion, Integer propietarioId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.propietarioId = propietarioId;
    }

    // Constructor sin ID para nuevos food trucks
    public FoodTruck(String nombre, String descripcion, Integer propietarioId) {
        this(null, nombre, descripcion, propietarioId);
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(Integer propietarioId) {
        this.propietarioId = propietarioId;
    }

    @Override
    public String toString() {
        return String.format("FoodTruck{id=%d, nombre='%s', descripcion='%s', propietarioId=%d}",
                id, nombre, descripcion, propietarioId);
    }
}
