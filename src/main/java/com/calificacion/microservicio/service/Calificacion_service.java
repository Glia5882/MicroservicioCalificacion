package com.calificacion.microservicio.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.calificacion.microservicio.dto.Califiacion_dto;
import com.calificacion.microservicio.entities.Calificacion;
import com.calificacion.microservicio.repositories.Calificacion_repository;
import scala.collection.JavaConverters;

@Service
public class Calificacion_service {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    Calificacion_repository calificacion_repository;
    ModelMapper modelMapper;
    
    // Método para obtener información de un cliente desde el microservicio Cliente
    public Map<String, Object> obtenerClientePorId(Long id) {
        String clienteUrl = "http://localhost:8081/api/v1/cliente/" + id; // Endpoint del microservicio Cliente
        try {
            return restTemplate.getForObject(clienteUrl, Map.class); // Realiza la solicitud y mapea a Map
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el microservicio Cliente: " + e.getMessage());
        }
    }

    public Calificacion_service(Calificacion_repository calificacion_repository, ModelMapper modelMapper) {
        this.calificacion_repository = calificacion_repository;
        this.modelMapper = modelMapper;
    }

    public List<Califiacion_dto> obtenerCalificacionesPorServicio(Long idServicio) {
        // Obtén la lista de calificaciones directamente como una colección Java
        List<Calificacion> calificaciones = calificacion_repository.findByIdServicio(idServicio);
    
        // Mapea las entidades a DTOs
        return calificaciones.stream()
                .map(calificacion -> modelMapper.map(calificacion, Califiacion_dto.class))
                .collect(Collectors.toList());
    }
    
    

    // Crear una nueva calificación
    public Califiacion_dto crearCalificacion(Califiacion_dto calificacionDto) {
        
        if (calificacionDto.getEstrellas() < 1 || calificacionDto.getEstrellas() > 5) {
            throw new IllegalArgumentException("Las estrellas deben estar entre 1 y 5.");
        }
    
        // Validar que el servicio exista (puedes agregar una validación adicional aquí)
        if (calificacionDto.getIdServicio() == null) {
            throw new IllegalArgumentException("Debe especificar el ID del servicio.");
        }
    
        Calificacion calificacion = modelMapper.map(calificacionDto, Calificacion.class);
        Calificacion nuevaCalificacion = calificacion_repository.save(calificacion);
        return modelMapper.map(nuevaCalificacion, Califiacion_dto.class);
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
    
            // Actualizar los campos de la entidad existente
            calificacion.setEstrellas(calificacionDto.getEstrellas());
            calificacion.setComentario(calificacionDto.getComentario());
    
            // Guardar la calificación actualizada en el repositorio
            Calificacion calificacionActualizada = calificacion_repository.save(calificacion);
    
            // Retornar la entidad actualizada convertida a DTO
            return new Califiacion_dto(
                calificacionActualizada.getId_calificacion(), 
                calificacionActualizada.getEstrellas(),       
                calificacionActualizada.getComentario(),     
                calificacionActualizada.getIdCliente(),
                calificacionActualizada.getIdServicio()
            );
        } else {
            throw new IllegalArgumentException("Calificación no encontrada");
        }
    }
    

    public List<Califiacion_dto> obtenerCalificacionesPorCliente(Long idCliente) {
        // Obtén la lista de calificaciones directamente como una colección Java
        List<Calificacion> calificaciones = calificacion_repository.findByIdCliente(idCliente);
    
        // Mapea las entidades a DTOs
        return calificaciones.stream()
                .map(calificacion -> modelMapper.map(calificacion, Califiacion_dto.class))
                .collect(Collectors.toList());
    }
    

    // Eliminar una calificación por ID
    public void eliminarCalificacion(Long id) {
        calificacion_repository.deleteById(id);
    }
}

