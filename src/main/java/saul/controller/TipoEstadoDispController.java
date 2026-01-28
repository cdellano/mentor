package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.TipoEstadoDispRequest;
import saul.dto.response.TipoEstadoDispResponse;
import saul.service.TipoEstadoDispService;

/**
 * Controlador REST para la gestión de tipos de estado de dispositivos.
 * Proporciona endpoints para el CRUD completo de los estados que pueden tener los dispositivos
 * (ej: operativo, en reparación, fuera de servicio, en mantenimiento, etc.).
 */
@RestController
@RequestMapping("/api/tipos-estado-dispositivo")
@RequiredArgsConstructor
public class TipoEstadoDispController {

    private final TipoEstadoDispService tipoEstadoDispService;

    /**
     * Obtiene todos los tipos de estado de dispositivos registrados en el sistema con paginación.
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con la lista de tipos de estado de dispositivos
     */
    @GetMapping
    public ResponseEntity<Page<TipoEstadoDispResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoEstadoDispService.findAll(pageable));
    }

    /**
     * Busca un tipo de estado de dispositivo específico por su identificador.
     * @param id Identificador único del tipo de estado
     * @return Información detallada del tipo de estado de dispositivo encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoEstadoDispResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoEstadoDispService.findById(id));
    }

    /**
     * Busca tipos de estado de dispositivos por nombre con paginación.
     * Permite búsqueda parcial (contiene el texto especificado).
     * @param nombre Nombre o parte del nombre del tipo de estado a buscar
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los tipos de estado que coinciden con el nombre especificado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Page<TipoEstadoDispResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoEstadoDispService.findByNombre(nombre, pageable));
    }

    /**
     * Crea un nuevo tipo de estado de dispositivo en el sistema.
     * @param request Datos del tipo de estado a crear (nombre, descripción, etc.)
     * @return El tipo de estado creado con su información completa y estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<TipoEstadoDispResponse> create(@Valid @RequestBody TipoEstadoDispRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoEstadoDispService.create(request));
    }

    /**
     * Actualiza la información de un tipo de estado de dispositivo existente.
     * @param id Identificador único del tipo de estado a actualizar
     * @param request Nuevos datos del tipo de estado
     * @return El tipo de estado actualizado con la información modificada
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoEstadoDispResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody TipoEstadoDispRequest request) {
        return ResponseEntity.ok(tipoEstadoDispService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un tipo de estado de dispositivo.
     * El tipo de estado se marca como eliminado pero permanece en la base de datos.
     * @param id Identificador único del tipo de estado a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoEstadoDispService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un tipo de estado de dispositivo que fue eliminado lógicamente.
     * @param id Identificador único del tipo de estado a restaurar
     * @return Respuesta con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        tipoEstadoDispService.restore(id);
        return ResponseEntity.ok().build();
    }
}

