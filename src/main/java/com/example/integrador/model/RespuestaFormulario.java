package com.example.integrador.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RespuestasFormulario")
public class RespuestaFormulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_respuesta;

    @ManyToOne
    @JoinColumn(name = "id_adopcion", nullable = false)
    private Adopcion adopcion;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", nullable = false)
    private PreguntaFormulario pregunta;

    @Column(columnDefinition = "TEXT")
    private String respuesta;

    // Getters y Setters
    public Integer getId_respuesta() {
        return id_respuesta;
    }

    public void setId_respuesta(Integer id_respuesta) {
        this.id_respuesta = id_respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public PreguntaFormulario getPregunta() {
        return pregunta;
    }

    public void setPregunta(PreguntaFormulario pregunta) {
        this.pregunta = pregunta;
    }

    public Adopcion getAdopcion() {
        return adopcion;
    }

    public void setAdopcion(Adopcion adopcion) {
        this.adopcion = adopcion;
    }
}
