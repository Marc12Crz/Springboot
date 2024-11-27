package com.example.integrador.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_empresa;

    private String nombre;
    private String direccion;
    private String telefono;
    private String correo;

    @Enumerated(EnumType.STRING)
    private TipoProducto tipo_producto;

    public Integer getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(Integer id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public TipoProducto getTipo_producto() {
        return tipo_producto;
    }

    public void setTipo_producto(TipoProducto tipo_producto) {
        this.tipo_producto = tipo_producto;
    }

    // Getters y Setters

    public enum TipoProducto {
        COMIDA, CAMAS, VACUNAS, OTROS
    }
}
