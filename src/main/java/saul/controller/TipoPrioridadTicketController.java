package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.TipoPrioridadTicketRequest;
import saul.dto.response.TipoPrioridadTicketResponse;
import saul.service.TipoPrioridadTicketService;

/**
 * Controlador REST para la gestión de tipos de prioridad de tickets.
 * Proporciona endpoints para el CRUD completo de los niveles de prioridad
 * que se pueden asignar a los tickets (ej: Baja, Media, Alta, Crítica).
 */
@RestController
@RequestMapping("/api/tipos-prioridad-ticket")
@RequiredArgsConstructor
public class TipoPrioridadTicketController {

    private final TipoPrioridadTicketService tipoPrioridadTicketService;

    /**
     * Obtiene todos los tipos de prioridad registrados en el sistema con paginación.
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con la lista de tipos de prioridad
     */
    @GetMapping
    public ResponseEntity<Page<TipoPrioridadTicketResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoPrioridadTicketService.findAll(pageable));
    }

    /**
     * Busca un tipo de prioridad específico por su identificador.
     * @param id Identificador único del tipo de prioridad
     * @return Información detallada del tipo de prioridad encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoPrioridadTicketResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoPrioridadTicketService.findById(id));
    }

    /**
     * Busca tipos de prioridad por nombre con paginación.
     * Permite búsqueda parcial (contiene el texto especificado).
     * @param nombre Nombre o parte del nombre del tipo de prioridad a buscar
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los tipos de prioridad que coinciden con el nombre especificado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Page<TipoPrioridadTicketResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoPrioridadTicketService.findByNombre(nombre, pageable));
    }

    /**
     * Crea un nuevo tipo de prioridad en el sistema.
     * @param request Datos del tipo de prioridad a crear (nombre, descripción, etc.)
     * @return El tipo de prioridad creado con su información completa y estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<TipoPrioridadTicketResponse> create(@Valid @RequestBody TipoPrioridadTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoPrioridadTicketService.create(request));
    }

    /**
     * Actualiza la información de un tipo de prioridad existente.
     * @param id Identificador único del tipo de prioridad a actualizar
     * @param request Nuevos datos del tipo de prioridad
     * @return El tipo de prioridad actualizado con la información modificada
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoPrioridadTicketResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody TipoPrioridadTicketRequest request) {
        return ResponseEntity.ok(tipoPrioridadTicketService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un tipo de prioridad.
     * El tipo de prioridad se marca como eliminado pero permanece en la base de datos.
     * @param id Identificador único del tipo de prioridad a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoPrioridadTicketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un tipo de prioridad que fue eliminado lógicamente.
     * @param id Identificador único del tipo de prioridad a restaurar
     * @return Respuesta con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        tipoPrioridadTicketService.restore(id);
        return ResponseEntity.ok().build();
    }
}

