package com.veris.repositories;

import com.veris.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByIdPaciente(Long idPaciente);

    @Query("SELECT p FROM Paciente p WHERE p.numeroIdentificacion = :numeroIdentificacion")
    List<Paciente> findByNumeroIdentificacion(@Param("numeroIdentificacion") String numeroIdentificacion);

    @Query("SELECT p FROM Paciente p WHERE LOWER(p.nombreCompleto) LIKE LOWER(CONCAT('%', :nombreCompleto, '%'))")
    List<Paciente> findByNombreCompleto(@Param("nombreCompleto") String nombreCompleto);

    @Query("SELECT p FROM Paciente p WHERE p.email = :email")
    List<Paciente> findByEmail(@Param("email") String email);

    @Query("SELECT p FROM Paciente p WHERE p.estado = :estado")
    List<Paciente> findByEstado(@Param("estado") String estado);

    @Query("SELECT COALESCE(MAX(p.idPaciente), 0) FROM Paciente p")
    Long findMaxIdPaciente();
}
