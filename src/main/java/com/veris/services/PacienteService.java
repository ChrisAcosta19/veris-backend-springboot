package com.veris.services;

import com.veris.dto.PacienteCreateRequest;
import com.veris.dto.PacienteListResponse;
import com.veris.dto.PacienteUpdateRequest;
import com.veris.entities.Paciente;
import com.veris.entities.TipoIdentificacion;
import com.veris.repositories.PacienteRepository;
import com.veris.repositories.TipoIdentificacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TipoIdentificacionRepository tipoIdentificacionRepository;

    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Email validation
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return true; // Email is optional
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Format full name
    private String formatFullName(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) {
        return String.join(" ", 
            primerNombre,
            segundoNombre != null ? segundoNombre : "",
            primerApellido,
            segundoApellido != null ? segundoApellido : ""
        ).replaceAll("\\s+", " ").trim();
    }

    // CREATE Paciente
    public Paciente createPaciente(PacienteCreateRequest request, String usuario) throws Exception {
        // Validate required fields
        if (request.getNumero_identificacion() == null || request.getCodigo_tipo_identificacion() == null ||
            request.getPrimer_nombre() == null || request.getPrimer_apellido() == null) {
            throw new IllegalArgumentException("Required fields missing: numero_identificacion, codigo_tipo_identificacion, primer_nombre, primer_apellido");
        }

        // Validate email format
        if (request.getEmail() != null && !isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Verify tipo identificacion exists
        Optional<TipoIdentificacion> tipoIde = tipoIdentificacionRepository.findByCodigoTipoIdentificacion(
            request.getCodigo_tipo_identificacion()
        );
        if (tipoIde.isEmpty()) {
            throw new IllegalArgumentException("El tipo de identificación proporcionado no existe en base_datos.");
        }

        // Generate next ID
        Long nextId = pacienteRepository.findMaxIdPaciente() + 1;

        // Create Paciente
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(nextId);
        paciente.setNumeroIdentificacion(request.getNumero_identificacion());
        paciente.setTipoIdentificacion(tipoIde.get());
        paciente.setPrimerNombre(request.getPrimer_nombre());
        paciente.setSegundoNombre(request.getSegundo_nombre());
        paciente.setPrimerApellido(request.getPrimer_apellido());
        paciente.setSegundoApellido(request.getSegundo_apellido());
        paciente.setNombreCompleto(formatFullName(
            request.getPrimer_nombre(),
            request.getSegundo_nombre(),
            request.getPrimer_apellido(),
            request.getSegundo_apellido()
        ));
        paciente.setEmail(request.getEmail());
        paciente.setEstado("A");
        paciente.setUsuarioIngreso(usuario != null ? usuario : "SISTEMA");

        return pacienteRepository.save(paciente);
    }

    // READ All Pacientes with filters and pagination
    public PacienteListResponse getPacientes(
            Integer page,
            Integer limit,
            String numeroIdentificacion,
            String nombreCompleto,
            String email,
            String estado) {

        page = page != null && page > 0 ? page : 1;
        limit = limit != null && limit > 0 ? limit : 10;
        String estadoFilter = estado != null ? estado : "A"; // Default to 'A'

        // Get all pacientes first, then apply filters
        List<Paciente> allPacientes = pacienteRepository.findAll();

        // Apply filters
        List<Paciente> filteredPacientes = allPacientes.stream()
            .filter(p -> estadoFilter == null || p.getEstado().equals(estadoFilter))
            .filter(p -> numeroIdentificacion == null || p.getNumeroIdentificacion().equals(numeroIdentificacion))
            .filter(p -> email == null || p.getEmail().equals(email))
            .filter(p -> nombreCompleto == null || p.getNombreCompleto().toLowerCase().contains(nombreCompleto.toLowerCase()))
            .collect(Collectors.toList());

        int total = filteredPacientes.size();
        int totalPages = (int) Math.ceil((double) total / limit);

        // Apply pagination
        List<Paciente> paginatedPacientes = filteredPacientes.stream()
            .skip((long) (page - 1) * limit)
            .limit(limit)
            .collect(Collectors.toList());

        return PacienteListResponse.builder()
            .pacientes(paginatedPacientes)
            .total(total)
            .page(page)
            .limit(limit)
            .totalPages(totalPages)
            .build();
    }

    // READ Paciente by ID
    public Paciente getPacienteById(Long id) throws Exception {
        Optional<Paciente> paciente = pacienteRepository.findByIdPaciente(id);
        if (paciente.isEmpty()) {
            throw new Exception("Paciente no encontrado");
        }
        return paciente.get();
    }

    // UPDATE Paciente
    public Paciente updatePaciente(Long id, PacienteUpdateRequest request, String usuario) throws Exception {
        Optional<Paciente> pacienteOpt = pacienteRepository.findByIdPaciente(id);
        if (pacienteOpt.isEmpty()) {
            throw new Exception("Paciente no encontrado");
        }

        Paciente paciente = pacienteOpt.get();

        // Cannot modify numero_identificacion or tipo_identificacion
        if (request.getPrimer_nombre() == null && request.getPrimer_apellido() == null &&
            request.getSegundo_nombre() == null && request.getSegundo_apellido() == null && 
            request.getEmail() == null) {
            throw new IllegalArgumentException("At least one field must be provided for update");
        }

        // Validate email format if provided
        if (request.getEmail() != null && !isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Update fields
        if (request.getPrimer_nombre() != null) {
            paciente.setPrimerNombre(request.getPrimer_nombre());
        }
        if (request.getSegundo_nombre() != null) {
            paciente.setSegundoNombre(request.getSegundo_nombre());
        }
        if (request.getPrimer_apellido() != null) {
            paciente.setPrimerApellido(request.getPrimer_apellido());
        }
        if (request.getSegundo_apellido() != null) {
            paciente.setSegundoApellido(request.getSegundo_apellido());
        }
        if (request.getEmail() != null) {
            paciente.setEmail(request.getEmail());
        }

        paciente.setNombreCompleto(formatFullName(
            paciente.getPrimerNombre(),
            paciente.getSegundoNombre(),
            paciente.getPrimerApellido(),
            paciente.getSegundoApellido()
        ));

        paciente.setUsuarioModificacion(usuario != null ? usuario : "SISTEMA");

        return pacienteRepository.save(paciente);
    }

    // DELETE Paciente (Logical Delete)
    public void deletePaciente(Long id, String usuario) throws Exception {
        Optional<Paciente> pacienteOpt = pacienteRepository.findByIdPaciente(id);
        if (pacienteOpt.isEmpty()) {
            throw new Exception("Paciente no encontrado o ya estaba inactivo");
        }

        Paciente paciente = pacienteOpt.get();
        if (!"A".equals(paciente.getEstado())) {
            throw new Exception("Paciente no encontrado o ya estaba inactivo");
        }

        paciente.setEstado("I");
        paciente.setUsuarioModificacion(usuario != null ? usuario : "SISTEMA");

        pacienteRepository.save(paciente);
    }
}
