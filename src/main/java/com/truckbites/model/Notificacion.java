package com.truckbites.model;

import java.time.LocalDateTime;

public class Notificacion {
    private Integer id;
    private Integer usuarioId;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private Boolean leida;

    // Constructor completo
    public Notificacion(Integer id, Integer usuarioId, String mensaje, LocalDateTime fechaCreacion, Boolean leida) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.mensaje = mensaje;
        this.fechaCreacion = fechaCreacion;
        this.leida = leida;
    }

    // Constructor sin ID para nuevas notificaciones
    public Notificacion(Integer usuarioId, String mensaje) {
        this.usuarioId = usuarioId;
        this.mensaje = mensaje;
        this.fechaCreacion = LocalDateTime.now();
        this.leida = false; // Por defecto, la notificación no está leída
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getLeida() {
        return leida;
    }

    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    @Override
    public String toString() {
        return String.format("Notificacion{id=%d, usuarioId=%d, mensaje='%s', fechaCreacion=%s, leida=%b}",
                id, usuarioId, mensaje, fechaCreacion, leida);
    }
}
