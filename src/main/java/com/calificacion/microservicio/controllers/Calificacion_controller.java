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
@RequestMapping(value = "/api/v1/calificacion")
public class Calificacion_controller {

    @Autowired
    Calificacion_service calificacion_service;

    public Calificacion_controller(Calificacion_service calificacion_service) {
        this.calificacion_service = calificacion_service;
    }

    // Crear una nueva calificación
    @PostMapping
    public ResponseEntity<Califiacion_dto> crearCalificacion(@RequestBody Califiacion_dto calificacionDto) {
        try {
            Califiacion_dto nuevaCalificacion = calificacion_service.crearCalificacion(calificacionDto);
            return new ResponseEntity<>(nuevaCalificacion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todas las calificaciones
    @GetMapping
    public ResponseEntity<List<Calificacion>> obtenerTodasLasCalificaciones() {
        List<Calificacion> calificaciones = calificacion_service.obtenerTodasLasCalificaciones();
        return new ResponseEntity<>(calificaciones, HttpStatus.OK);
    }

    // Obtener calificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> obtenerCalificacionPorId(@PathVariable Long id) {
        try {
            Calificacion calificacion = calificacion_service.obtenerCalificacionPorId(id);
            return new ResponseEntity<>(calificacion, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar calificación por ID
    @PutMapping("/{id}")
    public ResponseEntity<Califiacion_dto> actualizarCalificacion(@PathVariable Long id, @RequestBody Califiacion_dto calificacionDto) {
        try {
            Califiacion_dto calificacionActualizada = calificacion_service.actualizarCalificacion(id, calificacionDto);
            return new ResponseEntity<>(calificacionActualizada, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar calificación por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCalificacion(@PathVariable Long id) {
        calificacion_service.eliminarCalificacion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/servicio/{idServicio}")
    public ResponseEntity<List<Califiacion_dto>> obtenerCalificacionesPorServicio(@PathVariable Long idServicio) {
    List<Califiacion_dto> calificaciones = calificacion_service.obtenerCalificacionesPorServicio(idServicio);
    return ResponseEntity.ok(calificaciones);
}


    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> obtenerCalificacionesPorCliente(@PathVariable Long id) {
        try {
            List<Califiacion_dto> calificaciones = calificacion_service.obtenerCalificacionesPorCliente(id);
            return ResponseEntity.ok(calificaciones);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
}
