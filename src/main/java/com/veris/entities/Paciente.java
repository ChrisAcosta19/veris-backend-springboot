package com.veris.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mgm_pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @Column(name = "id_paciente")
    private Long idPaciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "codigo_tipo_identificacion", referencedColumnName = "codigo_tipo_identificacion")
    private TipoIdentificacion tipoIdentificacion;

    @Column(name = "numero_identificacion", length = 20, nullable = false)
    private String numeroIdentificacion;

    @Column(name = "primer_nombre", length = 50, nullable = false)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 50)
    private String segundoNombre;

    @Column(name = "primer_apellido", length = 50, nullable = false)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 50)
    private String segundoApellido;

    @Column(name = "nombre_completo", length = 200, nullable = false)
    private String nombreCompleto;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "estado", length = 1, nullable = false)
    private String estado;

    @CreationTimestamp
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "usuario_ingreso", length = 50)
    private String usuarioIngreso;

    @UpdateTimestamp
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_modificacion", length = 50)
    private String usuarioModificacion;
}
