package com.calificacion.microservicio.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.calificacion.microservicio.entities.Calificacion;

@Repository
public interface Calificacion_repository extends CrudRepository<Calificacion, Long> {
    List<Calificacion> findByServicioId(Long servicioId); // Consultar calificaciones por servicio
    List<Calificacion> findByClienteId(Long clienteId);
}
