package com.veris.controllers;

import com.veris.dto.ApiResponse;
import com.veris.dto.PacienteCreateRequest;
import com.veris.dto.PacienteListResponse;
import com.veris.dto.PacienteUpdateRequest;
import com.veris.entities.Paciente;
import com.veris.services.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes", description = "Endpoints para gestión de pacientes")
@SecurityRequirement(name = "bearerAuth")
public class PacienteController {

        @Autowired
        private PacienteService pacienteService;

        @PostMapping
        @Operation(summary = "Crear paciente", description = "Crea un nuevo paciente")
        public ResponseEntity<ApiResponse<Paciente>> createPaciente(@RequestBody PacienteCreateRequest request,
                        HttpServletRequest httpRequest) {
                try {
                        String usuario = (String) httpRequest.getAttribute("user");
                        Paciente paciente = pacienteService.createPaciente(request, usuario);

                        return ResponseEntity.status(HttpStatus.CREATED).body(
                                        ApiResponse.<Paciente>builder()
                                                        .code(201)
                                                        .success(true)
                                                        .message("Paciente creado exitosamente")
                                                        .data(paciente)
                                                        .build());
                } catch (IllegalArgumentException e) {
                        log.warn("Validation error: {}", e.getMessage());
                        return ResponseEntity.badRequest().body(
                                        ApiResponse.<Paciente>builder()
                                                        .code(400)
                                                        .success(false)
                                                        .message(e.getMessage())
                                                        .errorData(new Object())
                                                        .build());
                } catch (Exception e) {
                        log.error("Error creating paciente: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                        ApiResponse.<Paciente>builder()
                                                        .code(500)
                                                        .success(false)
                                                        .message("Error interno creando el paciente")
                                                        .errorData(new Object())
                                                        .build());
                }
        }

        @GetMapping
        @Operation(summary = "Listar pacientes", description = "Obtiene lista de pacientes con filtros y paginación")
        public ResponseEntity<ApiResponse<PacienteListResponse>> listPacientes(
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "numero_identificacion", required = false) String numeroIdentificacion,
                        @RequestParam(value = "nombre_completo", required = false) String nombreCompleto,
                        @RequestParam(value = "email", required = false) String email,
                        @RequestParam(value = "estado", required = false) String estado) {
                try {
                        PacienteListResponse response = pacienteService.getPacientes(
                                        page, limit, numeroIdentificacion, nombreCompleto, email, estado);

                        return ResponseEntity.ok(
                                        ApiResponse.<PacienteListResponse>builder()
                                                        .code(200)
                                                        .success(true)
                                                        .message("Pacientes obtenidos exitosamente")
                                                        .data(response)
                                                        .build());
                } catch (Exception e) {
                        log.error("Error fetching pacientes: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                        ApiResponse.<PacienteListResponse>builder()
                                                        .code(500)
                                                        .success(false)
                                                        .message("Error obteniendo pacientes")
                                                        .errorData(new Object())
                                                        .build());
                }
        }

        @GetMapping("/{idPaciente}")
        @Operation(summary = "Obtener paciente por ID", description = "Obtiene información completa de un paciente")
        public ResponseEntity<ApiResponse<Paciente>> getPacienteById(@PathVariable Long idPaciente) {
                try {
                        Paciente paciente = pacienteService.getPacienteById(idPaciente);

                        return ResponseEntity.ok(
                                        ApiResponse.<Paciente>builder()
                                                        .code(200)
                                                        .success(true)
                                                        .message("Paciente obtenido exitosamente")
                                                        .data(paciente)
                                                        .build());
                } catch (Exception e) {
                        log.warn("Paciente not found: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                        ApiResponse.<Paciente>builder()
                                                        .code(404)
                                                        .success(false)
                                                        .message("Paciente no encontrado")
                                                        .errorData(new Object())
                                                        .build());
                }
        }

        @PutMapping("/{idPaciente}")
        @Operation(summary = "Actualizar paciente", description = "Actualiza la información de un paciente")
        public ResponseEntity<ApiResponse<Paciente>> updatePaciente(
                        @PathVariable Long idPaciente,
                        @RequestBody PacienteUpdateRequest request,
                        HttpServletRequest httpRequest) {
                try {
                        String usuario = (String) httpRequest.getAttribute("user");
                        Paciente paciente = pacienteService.updatePaciente(idPaciente, request, usuario);

                        return ResponseEntity.ok(
                                        ApiResponse.<Paciente>builder()
                                                        .code(200)
                                                        .success(true)
                                                        .message("Paciente actualizado exitosamente")
                                                        .data(paciente)
                                                        .build());
                } catch (IllegalArgumentException e) {
                        log.warn("Validation error: {}", e.getMessage());
                        return ResponseEntity.badRequest().body(
                                        ApiResponse.<Paciente>builder()
                                                        .code(400)
                                                        .success(false)
                                                        .message(e.getMessage())
                                                        .errorData(new Object())
                                                        .build());
                } catch (Exception e) {
                        log.warn("Paciente not found: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                        ApiResponse.<Paciente>builder()
                                                        .code(404)
                                                        .success(false)
                                                        .message("Paciente no encontrado")
                                                        .errorData(new Object())
                                                        .build());
                }
        }

        @DeleteMapping("/{idPaciente}")
        @Operation(summary = "Eliminar paciente", description = "Realiza baja lógica de un paciente")
        public ResponseEntity<ApiResponse<Object>> deletePaciente(@PathVariable Long idPaciente,
                        HttpServletRequest httpRequest) {
                try {
                        String usuario = (String) httpRequest.getAttribute("user");
                        pacienteService.deletePaciente(idPaciente, usuario);

                        return ResponseEntity.ok(
                                        ApiResponse.<Object>builder()
                                                        .code(200)
                                                        .success(true)
                                                        .message("Paciente eliminado exitosamente (baja lógica)")
                                                        .data(new Object())
                                                        .build());
                } catch (Exception e) {
                        log.warn("Error deleting paciente: {}", e.getMessage());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                        ApiResponse.<Object>builder()
                                                        .code(404)
                                                        .success(false)
                                                        .message("Paciente no encontrado o ya estaba inactivo")
                                                        .errorData(new Object())
                                                        .build());
                }
        }
}
