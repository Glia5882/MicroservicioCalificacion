package com.calificacion.microservicio.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.calificacion.microservicio.entities.Calificacion;

import java.util.List;

@Repository
public interface Calificacion_repository extends CrudRepository<Calificacion, Long> {
    List<Calificacion> findByIdCliente(Long idCliente);
    List<Calificacion> findByIdServicio(Long idServicio);
}
