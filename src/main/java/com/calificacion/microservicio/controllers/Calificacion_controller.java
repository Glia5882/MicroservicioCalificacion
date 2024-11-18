package com.calificacion.microservicio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calificacion.microservicio.dto.Califiacion_dto;
import com.calificacion.microservicio.entities.Calificacion;
import com.calificacion.microservicio.service.Calificacion_service;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/calificacion", produces = MediaType.APPLICATION_JSON_VALUE)
public class Calificacion_controller {

    @Autowired
    Calificacion_service calificacion_service;

    public Calificacion_controller(Calificacion_service calificacion_service) {
        this.calificacion_service = calificacion_service;
    }

    // Crear una nueva calificación
    @PostMapping
    public ResponseEntity<?> crearCalificacion(@RequestBody Califiacion_dto calificacionDto) {
        try {
            Califiacion_dto nuevaCalificacion = calificacion_service.crearCalificacion(calificacionDto);
            return new ResponseEntity<>(nuevaCalificacion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear la calificación: " + e.getMessage());
        }
    }

    // Obtener todas las calificaciones
    @GetMapping
    public ResponseEntity<List<Califiacion_dto>> obtenerTodasLasCalificaciones() {
        List<Califiacion_dto> calificaciones = calificacion_service.obtenerTodasLasCalificaciones()
                .stream()
                .map(calificacion -> new Califiacion_dto(
                        calificacion.getId_calificacion(),
                        calificacion.getEstrellas(),
                        calificacion.getComentario()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    // Obtener calificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCalificacionPorId(@PathVariable Long id) {
        try {
            Califiacion_dto calificacion = calificacion_service.obtenerCalificacionPorIdDto(id);
            return new ResponseEntity<>(calificacion, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Calificación no encontrada: " + e.getMessage());
        }
    }

    // Actualizar calificación por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCalificacion(@PathVariable Long id, @RequestBody Califiacion_dto calificacionDto) {
        try {
            Califiacion_dto calificacionActualizada = calificacion_service.actualizarCalificacion(id, calificacionDto);
            return new ResponseEntity<>(calificacionActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error al actualizar la calificación: " + e.getMessage());
        }
    }

    // Eliminar calificación por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCalificacion(@PathVariable Long id) {
        try {
            calificacion_service.eliminarCalificacion(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error al eliminar la calificación: " + e.getMessage());
        }
    }
}
