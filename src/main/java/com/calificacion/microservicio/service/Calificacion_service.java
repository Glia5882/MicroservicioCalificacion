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
// Crear una nueva calificación
public Califiacion_dto crearCalificacion(Califiacion_dto calificacionDto) {
    // Validar la calificación
    validarCalificacion(calificacionDto);

    // Convertir el DTO a entidad usando ModelMapper
    Calificacion calificacion = modelMapper.map(calificacionDto, Calificacion.class);

    // Guardar la calificación en la base de datos
    Calificacion nuevaCalificacion = calificacion_repository.save(calificacion);

    // Convertir la entidad guardada de vuelta a DTO y devolverla
    return modelMapper.map(nuevaCalificacion, Califiacion_dto.class);
}


    // Obtener todas las calificaciones
    public List<Califiacion_dto> obtenerTodasLasCalificaciones() {
        return calificacion_repository.findAll().stream()
                .map(calificacion -> modelMapper.map(calificacion, Califiacion_dto.class))
                .collect(Collectors.toList());
    }
        // Obtener calificaciones por servicio
    public List<Califiacion_dto> obtenerCalificacionesPorServicio(Long servicioId) {
        return calificacion_repository.findByServicioId(servicioId).stream()
                .map(calificacion -> modelMapper.map(calificacion, Califiacion_dto.class))
                .collect(Collectors.toList());
    }

    public List<Califiacion_dto> obtenerCalificacionesPorCliente(Long clienteId) {
        return calificacion_repository.findByClienteId(clienteId).stream()
                .map(calificacion -> modelMapper.map(calificacion, Califiacion_dto.class))
                .collect(Collectors.toList());
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

    private void validarCalificacion(Califiacion_dto calificacionDto) {
        if (calificacionDto.getEstrellas() < 1 || calificacionDto.getEstrellas() > 5) {
            throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5.");
        }
        if (calificacionDto.getComentario() != null && calificacionDto.getComentario().length() > 1000) {
            throw new IllegalArgumentException("El comentario no puede exceder los 1000 caracteres.");
        }
    }

    // Eliminar una calificación por ID
    public void eliminarCalificacion(Long id) {
        calificacion_repository.deleteById(id);
    }
}

