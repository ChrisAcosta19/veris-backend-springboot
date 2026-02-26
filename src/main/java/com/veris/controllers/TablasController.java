package com.veris.controllers;

import com.veris.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/tablas")
@Tag(name = "Utilidades", description = "Endpoints de utilidad")
public class TablasController {

    @Autowired
    private EntityManager entityManager;

    @GetMapping
    @Operation(summary = "Obtener tablas", description = "Lista todas las tablas en el esquema del usuario actual de Oracle")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getTablas() {
        try {
            // Ejecutamos la consulta nativa en Oracle
            @SuppressWarnings("unchecked")
            List<String> tablas = entityManager.createNativeQuery("SELECT table_name FROM user_tables").getResultList();

            // Mapeamos a la estructura esperada: [{"TABLE_NAME": "nombre"}, ...]
            // equivalente al AppDataSource de TypeORM
            List<Map<String, String>> formattedTablas = tablas.stream()
                    .map(nombre -> Collections.singletonMap("TABLE_NAME", nombre))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(
                    ApiResponse.<List<Map<String, String>>>builder()
                            .code(200)
                            .success(true)
                            .message("Tablas obtenidas exitosamente")
                            .data(formattedTablas)
                            .build());
        } catch (Exception e) {
            log.error("Error obteniendo las tablas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.<List<Map<String, String>>>builder()
                            .code(500)
                            .success(false)
                            .message("Error obteniendo las tablas")
                            .errorData(Collections.singletonMap("detail", e.getMessage()))
                            .build());
        }
    }
}
