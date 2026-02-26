package com.veris.repositories;

import com.veris.entities.TipoIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoIdentificacionRepository extends JpaRepository<TipoIdentificacion, String> {

    Optional<TipoIdentificacion> findByCodigoTipoIdentificacion(String codigo);
}
