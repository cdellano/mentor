package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.TipoMantenimientoRequest;
import saul.dto.response.TipoMantenimientoResponse;
import saul.service.TipoMantenimientoService;

/**
 * Controlador REST para la gestión de tipos de mantenimiento.
 * Proporciona endpoints para el CRUD completo de las categorías de mantenimiento
 * que se pueden realizar a los dispositivos
 * (ej: preventivo, correctivo, predictivo, actualización de software, limpieza, etc.).
 */
@RestController
@RequestMapping("/api/tipos-mantenimiento")
@RequiredArgsConstructor
public class TipoMantenimientoController {

    private final TipoMantenimientoService tipoMantenimientoService;

    /**
     * Obtiene todos los tipos de mantenimiento registrados en el sistema con paginación.
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con la lista de tipos de mantenimiento
     */
    @GetMapping
    public ResponseEntity<Page<TipoMantenimientoResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoMantenimientoService.findAll(pageable));
    }

    /**
     * Busca un tipo de mantenimiento específico por su identificador.
     * @param id Identificador único del tipo de mantenimiento
     * @return Información detallada del tipo de mantenimiento encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoMantenimientoResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoMantenimientoService.findById(id));
    }

    /**
     * Busca tipos de mantenimiento por nombre con paginación.
     * Permite búsqueda parcial (contiene el texto especificado).
     * @param nombre Nombre o parte del nombre del tipo de mantenimiento a buscar
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los tipos de mantenimiento que coinciden con el nombre especificado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Page<TipoMantenimientoResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoMantenimientoService.findByNombre(nombre, pageable));
    }

    /**
     * Crea un nuevo tipo de mantenimiento en el sistema.
     * @param request Datos del tipo de mantenimiento a crear (nombre, descripción, etc.)
     * @return El tipo de mantenimiento creado con su información completa y estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<TipoMantenimientoResponse> create(@Valid @RequestBody TipoMantenimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoMantenimientoService.create(request));
    }

    /**
     * Actualiza la información de un tipo de mantenimiento existente.
     * @param id Identificador único del tipo de mantenimiento a actualizar
     * @param request Nuevos datos del tipo de mantenimiento
     * @return El tipo de mantenimiento actualizado con la información modificada
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoMantenimientoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody TipoMantenimientoRequest request) {
        return ResponseEntity.ok(tipoMantenimientoService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un tipo de mantenimiento.
     * El tipo de mantenimiento se marca como eliminado pero permanece en la base de datos.
     * @param id Identificador único del tipo de mantenimiento a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoMantenimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un tipo de mantenimiento que fue eliminado lógicamente.
     * @param id Identificador único del tipo de mantenimiento a restaurar
     * @return Respuesta con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        tipoMantenimientoService.restore(id);
        return ResponseEntity.ok().build();
    }
}

