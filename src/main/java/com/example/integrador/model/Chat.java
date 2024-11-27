package com.example.integrador.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "chat") // Nombre de tabla en minúsculas, como es común en MySQL
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMensaje; // Alineado con el nombre de la columna "id_mensaje"

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_albergue", nullable = false)
    private Albergue albergue;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    private Emisor emisor;

    public Integer getIdmensaje() {
        return idMensaje;
    }

    public void setIdmensaje(Integer id_mensaje) {
        this.idMensaje = idMensaje;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Albergue getAlbergue() {
        return albergue;
    }

    public void setAlbergue(Albergue albergue) {
        this.albergue = albergue;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }


    public Emisor getEmisor() {
        return emisor;
    }

    public void setEmisor(Emisor emisor) {
        this.emisor = emisor;
    }

    public enum Emisor {
        USUARIO, ALBERGUE
    }
}
