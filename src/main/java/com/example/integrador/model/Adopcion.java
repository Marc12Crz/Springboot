package com.example.integrador.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Adopciones")
public class Adopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdopcion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_perro", nullable = false)
    private Mascota mascota;

    @Temporal(TemporalType.DATE)
    private Date fechaSolicitud = new Date(); // Fecha predeterminada al crear una adopción

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_solicitud", nullable = false)
    private EstadoSolicitud estadoSolicitud = EstadoSolicitud.PENDIENTE; // Valor por defecto

    @Column(columnDefinition = "TEXT")
    private String condicionesHogar;

    private Boolean experienciaConPerros;
    private Boolean otrosAnimales;

    @Column(columnDefinition = "TEXT")
    private String motivoAdopcion;

    private Boolean responsableFinanciero;
    // Getters y Setters

    public Long getIdAdopcion() {
        return idAdopcion;
    }

    public void setIdAdopcion(Long idAdopcion) {
        this.idAdopcion = idAdopcion;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public EstadoSolicitud getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }

    public String getCondicionesHogar() {
        return condicionesHogar;
    }

    public void setCondicionesHogar(String condicionesHogar) {
        this.condicionesHogar = condicionesHogar;
    }

    public Boolean getExperienciaConPerros() {
        return experienciaConPerros;
    }

    public void setExperienciaConPerros(Boolean experienciaConPerros) {
        this.experienciaConPerros = experienciaConPerros;
    }

    public Boolean getOtrosAnimales() {
        return otrosAnimales;
    }

    public void setOtrosAnimales(Boolean otrosAnimales) {
        this.otrosAnimales = otrosAnimales;
    }

    public String getMotivoAdopcion() {
        return motivoAdopcion;
    }

    public void setMotivoAdopcion(String motivoAdopcion) {
        this.motivoAdopcion = motivoAdopcion;
    }

    public Boolean getResponsableFinanciero() {
        return responsableFinanciero;
    }

    public void setResponsableFinanciero(Boolean responsableFinanciero) {
        this.responsableFinanciero = responsableFinanciero;
    }

    public enum EstadoSolicitud {
        PENDIENTE, APROBADA, RECHAZADA
    }
}
