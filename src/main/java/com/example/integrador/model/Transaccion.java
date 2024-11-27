package com.example.integrador.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_transaccion;

    @ManyToOne
    @JoinColumn(name = "id_donacion", nullable = false)
    private Donacion donacion;

    @ManyToOne
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    private EstadoTransaccion estado_transaccion;

    @Temporal(TemporalType.DATE)
    private Date fecha_entrega;

    public Integer getId_transaccion() {
        return id_transaccion;
    }

    public void setId_transaccion(Integer id_transaccion) {
        this.id_transaccion = id_transaccion;
    }

    public Date getFecha_entrega() {
        return fecha_entrega;
    }

    public void setFecha_entrega(Date fecha_entrega) {
        this.fecha_entrega = fecha_entrega;
    }

    public EstadoTransaccion getEstado_transaccion() {
        return estado_transaccion;
    }

    public void setEstado_transaccion(EstadoTransaccion estado_transaccion) {
        this.estado_transaccion = estado_transaccion;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Donacion getDonacion() {
        return donacion;
    }

    public void setDonacion(Donacion donacion) {
        this.donacion = donacion;
    }

    // Getters y Setters

    public enum EstadoTransaccion {
        PENDIENTE, COMPLETADA, FALLIDA
    }
}
