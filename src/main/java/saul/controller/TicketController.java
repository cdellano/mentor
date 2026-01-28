package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.TicketRequest;
import saul.dto.response.TicketResponse;
import saul.service.TicketService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * Obtiene todos los tickets registrados en el sistema con paginación.
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con la lista de tickets
     */
    @GetMapping
    public ResponseEntity<Page<TicketResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ticketService.findAll(pageable));
    }

    /**
     * Busca un ticket específico por su identificador.
     * @param id Identificador único del ticket
     * @return Información detallada del ticket encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    /**
     * Filtra los tickets por su estado con paginación.
     * @param idEstado Identificador del estado del ticket (ej: abierto, en proceso, cerrado)
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los tickets que coinciden con el estado especificado
     */
    @GetMapping("/estado/{idEstado}")
    public ResponseEntity<Page<TicketResponse>> findByEstado(
            @PathVariable Integer idEstado,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ticketService.findByEstado(idEstado, pageable));
    }

    /**
     * Filtra los tickets por su nivel de prioridad con paginación.
     * @param idPrioridad Identificador del nivel de prioridad (ej: baja, media, alta, crítica)
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los tickets que coinciden con la prioridad especificada
     */
    @GetMapping("/prioridad/{idPrioridad}")
    public ResponseEntity<Page<TicketResponse>> findByPrioridad(
            @PathVariable Integer idPrioridad,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ticketService.findByPrioridad(idPrioridad, pageable));
    }

    /**
     * Filtra los tickets por departamento con paginación.
     * @param idDepartamento Identificador del departamento
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los tickets que pertenecen al departamento especificado
     */
    @GetMapping("/departamento/{idDepartamento}")
    public ResponseEntity<Page<TicketResponse>> findByDepartamento(
            @PathVariable Integer idDepartamento,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ticketService.findByDepartamento(idDepartamento, pageable));
    }

    /**
     * Busca tickets creados dentro de un rango de fechas específico con paginación.
     * @param inicio Fecha y hora de inicio del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @param fin Fecha y hora de fin del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los tickets creados dentro del rango de fechas especificado
     */
    @GetMapping("/fecha")
    public ResponseEntity<Page<TicketResponse>> findByFechaRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ticketService.findByFechaRango(inicio, fin, pageable));
    }

    /**
     * Crea un nuevo ticket en el sistema.
     * @param request Datos del ticket a crear (título, descripción, prioridad, tipo de incidente, etc.)
     * @return El ticket creado con su información completa y estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody TicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.create(request));
    }

    /**
     * Actualiza la información de un ticket existente.
     * @param id Identificador único del ticket a actualizar
     * @param request Nuevos datos del ticket
     * @return El ticket actualizado con la información modificada
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un ticket.
     * El ticket se marca como eliminado pero permanece en la base de datos.
     * @param id Identificador único del ticket a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un ticket que fue eliminado lógicamente.
     * @param id Identificador único del ticket a restaurar
     * @return Respuesta con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        ticketService.restore(id);
        return ResponseEntity.ok().build();
    }
}

