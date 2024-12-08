package com.example.integrador.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perro") // Asegúrate de que coincide con el nombre de la columna en la base de datos
    private Integer idPerro;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false)
    private String raza;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tamano tamano;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Column(nullable = false)
    private Boolean vacunasCompletas;

    @Column(nullable = false)
    private Boolean esterilizado;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String imagen;

    @ManyToOne
    @JoinColumn(name = "id_albergue", nullable = false)
    private Albergue albergue;

    // Getters y Setters

    public Integer getIdPerro() {
        return idPerro;
    }

    public void setIdPerro(Integer idPerro) {
        this.idPerro = idPerro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Tamano getTamano() {
        return tamano;
    }

    public void setTamano(Tamano tamano) {
        this.tamano = tamano;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Boolean getVacunasCompletas() {
        return vacunasCompletas;
    }

    public void setVacunasCompletas(Boolean vacunasCompletas) {
        this.vacunasCompletas = vacunasCompletas;
    }

    public Boolean getEsterilizado() {
        return esterilizado;
    }

    public void setEsterilizado(Boolean esterilizado) {
        this.esterilizado = esterilizado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Albergue getAlbergue() {
        return albergue;
    }

    public void setAlbergue(Albergue albergue) {
        this.albergue = albergue;
    }

    // Enumeraciones para tamaño y sexo
    public enum Tamano {
        pequeno, mediano, grande
    }

    public enum Sexo {
        macho, hembra
    }
}
