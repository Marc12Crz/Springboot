package com.example.integrador.model;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_formulario") // Nombre de la tabla en la base de datos
public class PreguntaFormulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pregunta;

    @ManyToOne
    @JoinColumn(name = "id_albergue", nullable = false) // Referencia a la columna en la BD
    private Albergue albergue;


    private String pregunta;

    // Getters y Setters
    public Integer getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(Integer id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public Albergue getAlbergue() {
        return albergue;
    }

    public void setAlbergue(Albergue albergue) {
        this.albergue = albergue;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
}
