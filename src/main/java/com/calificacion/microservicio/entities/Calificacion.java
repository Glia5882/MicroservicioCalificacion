package com.calificacion.microservicio.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_calificacion;

    private int estrellas; // Calificación entre 1 y 5

    @Column(length = 1000) // Limitar longitud del comentario
    private String comentario;

    private long clienteId; // Identificador del cliente que realiza la calificación

    private long servicioId; // Identificador del servicio calificado

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaCreacion; // Fecha y hora de creación de la calificación
}
