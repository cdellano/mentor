package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.EstadoTicketRequest;
import saul.dto.response.EstadoTicketResponse;
import saul.service.EstadoTicketService;

/**
 * Controlador REST para gestionar los estados de los tickets.
 * Permite administrar los diferentes estados por los que puede pasar un ticket (ej: Abierto, En Progreso, Cerrado, etc.).
 */
@RestController
@RequestMapping("/api/estados-ticket")
@RequiredArgsConstructor
public class EstadoTicketController {

    private final EstadoTicketService estadoTicketService;

    /**
     * Obtiene todos los estados de ticket del sistema de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con la lista de estados de ticket (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<EstadoTicketResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(estadoTicketService.findAll(pageable));
    }

    /**
     * Busca un estado de ticket específico por su ID.
     * @param id Identificador único del estado de ticket
     * @return Datos del estado de ticket encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstadoTicketResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(estadoTicketService.findById(id));
    }

    /**
     * Busca estados de ticket por nombre (búsqueda parcial).
     * Útil para filtrar estados que contengan un texto específico en su nombre.
     * @param nombre Nombre o parte del nombre del estado a buscar
     * @param pageable Parámetros de paginación
     * @return Página con los estados de ticket que coinciden con el nombre especificado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Page<EstadoTicketResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(estadoTicketService.findByNombre(nombre, pageable));
    }

    /**
     * Crea un nuevo estado de ticket en el sistema.
     * @param request Datos del estado de ticket a crear (validados)
     * @return Datos del estado de ticket creado con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<EstadoTicketResponse> create(@Valid @RequestBody EstadoTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoTicketService.create(request));
    }

    /**
     * Actualiza los datos de un estado de ticket existente.
     * @param id ID del estado de ticket a actualizar
     * @param request Nuevos datos del estado de ticket (validados)
     * @return Datos actualizados del estado de ticket
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstadoTicketResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody EstadoTicketRequest request) {
        return ResponseEntity.ok(estadoTicketService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un estado de ticket.
     * El estado no se elimina físicamente, solo se marca como eliminado.
     * @param id ID del estado de ticket a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        estadoTicketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un estado de ticket que fue eliminado lógicamente (soft delete).
     * @param id ID del estado de ticket a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        estadoTicketService.restore(id);
        return ResponseEntity.ok().build();
    }
}

