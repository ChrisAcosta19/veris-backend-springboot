package com.veris.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "daf_tipos_identificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoIdentificacion {

    @Id
    @Column(name = "codigo_tipo_identificacion", length = 3)
    private String codigoTipoIdentificacion;

    @Column(name = "nombre_tipo_identificacion", length = 100, nullable = false)
    private String nombreTipoIdentificacion;

    @Column(name = "estado", length = 1, nullable = false)
    private String estado;
}
