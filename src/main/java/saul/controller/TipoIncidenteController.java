package saul.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.TipoIncidenteRequest;
import saul.dto.response.TipoIncidenteResponse;
import saul.service.TipoIncidenteService;

/**
 * Controlador REST para la gestión de tipos de incidente.
 * Proporciona endpoints para el CRUD completo de las categorías de incidentes
 * que pueden ocurrir en tickets y bitácoras de servicio
 * (ej: falla de hardware, problema de software, falla de red, problema de seguridad, etc.).
 */
@RestController
@RequestMapping("/api/tipos-incidente")
@RequiredArgsConstructor
public class TipoIncidenteController {

    private final TipoIncidenteService tipoIncidenteService;

    /**
     * Obtiene todos los tipos de incidente registrados en el sistema con paginación.
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con la lista de tipos de incidente
     */
    @GetMapping
    public ResponseEntity<Page<TipoIncidenteResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoIncidenteService.findAll(pageable));
    }

    /**
     * Busca un tipo de incidente específico por su identificador.
     * @param id Identificador único del tipo de incidente
     * @return Información detallada del tipo de incidente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoIncidenteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tipoIncidenteService.findById(id));
    }

    /**
     * Crea un nuevo tipo de incidente en el sistema.
     * @param request Datos del tipo de incidente a crear (nombre, descripción, etc.)
     * @return El tipo de incidente creado con su información completa y estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<TipoIncidenteResponse> create(@Valid @RequestBody TipoIncidenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoIncidenteService.create(request));
    }

    /**
     * Actualiza la información de un tipo de incidente existente.
     * @param id Identificador único del tipo de incidente a actualizar
     * @param request Nuevos datos del tipo de incidente
     * @return El tipo de incidente actualizado con la información modificada
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoIncidenteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TipoIncidenteRequest request) {
        return ResponseEntity.ok(tipoIncidenteService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un tipo de incidente.
     * El tipo de incidente se marca como eliminado pero permanece en la base de datos.
     * @param id Identificador único del tipo de incidente a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoIncidenteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un tipo de incidente que fue eliminado lógicamente.
     * @param id Identificador único del tipo de incidente a restaurar
     * @return Respuesta con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        tipoIncidenteService.restore(id);
        return ResponseEntity.ok().build();
    }
}
