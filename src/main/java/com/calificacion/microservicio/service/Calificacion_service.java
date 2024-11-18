package com.calificacion.microservicio.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.calificacion.microservicio.dto.Califiacion_dto;
import com.calificacion.microservicio.entities.Calificacion;
import com.calificacion.microservicio.repositories.Calificacion_repository;

@Service
public class Calificacion_service {

    @Autowired
    Calificacion_repository calificacion_repository;
    ModelMapper modelMapper;
    
    public Calificacion_service(Calificacion_repository calificacion_repository, ModelMapper modelMapper) {
        this.calificacion_repository = calificacion_repository;
        this.modelMapper = modelMapper;
    }

    // Crear una nueva calificación
    public Califiacion_dto crearCalificacion(Califiacion_dto calificacionDto) {
        // Validar que las estrellas estén entre 1 y 5
        if (calificacionDto.getEstrellas() < 1 || calificacionDto.getEstrellas() > 5) {
            throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5.");
        }

        // Convertir el DTO a entidad
        Calificacion calificacion = new Calificacion();
        calificacion.setEstrellas(calificacionDto.getEstrellas());
        calificacion.setComentario(calificacionDto.getComentario());

        // Guardar la calificación en la base de datos
        Calificacion nuevaCalificacion = calificacion_repository.save(calificacion);

        // Convertir la entidad a DTO y devolverla
        return new Califiacion_dto(nuevaCalificacion.getId_calificacion(), nuevaCalificacion.getEstrellas(), nuevaCalificacion.getComentario());
    }

    // Obtener todas las calificaciones
    public List<Calificacion> obtenerTodasLasCalificaciones() {
        return (List<Calificacion>) calificacion_repository.findAll();
    }

    // Obtener calificación por ID
    public Calificacion obtenerCalificacionPorId(Long id) {
        Calificacion calificacion = calificacion_repository.findById(id).orElseThrow(() -> 
            new IllegalArgumentException("Calificación no encontrada"));
        return calificacion;
    }

    // Actualizar una calificación existente
    public Califiacion_dto actualizarCalificacion(Long id, Califiacion_dto calificacionDto) {
        Optional<Calificacion> calificacionExistente = calificacion_repository.findById(id);

        if (calificacionExistente.isPresent()) {
            Calificacion calificacion = calificacionExistente.get();
            
            // Validar que las estrellas estén entre 1 y 5
            if (calificacionDto.getEstrellas() < 1 || calificacionDto.getEstrellas() > 5) {
                throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5.");
            }

            calificacion.setEstrellas(calificacionDto.getEstrellas());
            calificacion.setComentario(calificacionDto.getComentario());

            // Guardar la calificación actualizada
            Calificacion calificacionActualizada = calificacion_repository.save(calificacion);

            // Convertir la entidad actualizada a DTO
            return new Califiacion_dto(calificacionActualizada.getId_calificacion(), calificacionActualizada.getEstrellas(), calificacionActualizada.getComentario());
        } else {
            throw new IllegalArgumentException("Calificación no encontrada");
        }
    }

    // Eliminar una calificación por ID
    public void eliminarCalificacion(Long id) {
        calificacion_repository.deleteById(id);
    }
}

