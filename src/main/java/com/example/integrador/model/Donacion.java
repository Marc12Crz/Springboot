package com.example.integrador.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;

@Entity
@Table(name = "Donaciones")
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_donacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @NotNull
    @Min(1)
    private Integer cantidad;

    @Temporal(TemporalType.TIMESTAMP)
    @PastOrPresent
    private Date fecha_donacion;

    @NotNull
    @Min(0)
    private Double monto_total;

    @ManyToOne
    @JoinColumn(name = "id_albergue", nullable = false)
    private Albergue albergue;

    public Integer getId_donacion() {
        return id_donacion;
    }

    public void setId_donacion(Integer id_donacion) {
        this.id_donacion = id_donacion;
    }

    public Albergue getAlbergue() {
        return albergue;
    }

    public void setAlbergue(Albergue albergue) {
        this.albergue = albergue;
    }

    public Double getMonto_total() {
        return monto_total;
    }

    public void setMonto_total(Double monto_total) {
        this.monto_total = monto_total;
    }

    public Date getFecha_donacion() {
        return fecha_donacion;
    }

    public void setFecha_donacion(Date fecha_donacion) {
        this.fecha_donacion = fecha_donacion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // Getters y Setters
}
