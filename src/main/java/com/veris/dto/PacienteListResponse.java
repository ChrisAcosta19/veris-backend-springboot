package com.veris.dto;

import com.veris.entities.Paciente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteListResponse {
    private List<Paciente> pacientes;
    private Integer total;
    private Integer page;
    private Integer limit;
    private Integer totalPages;
}
