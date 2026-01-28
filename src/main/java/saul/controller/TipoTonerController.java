package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.TipoTonerRequest;
import saul.dto.response.TipoTonerResponse;
import saul.service.TipoTonerService;

/**
 * Controlador REST para la gestión de tipos de tóner.
 * Proporciona endpoints para el CRUD completo de las categorías de tóner
 * para impresoras y equipos de impresión (ej: HP 85A, Canon 051, Epson T664, etc.).
 */
@RestController
@RequestMapping("/api/tipos-toner")
@RequiredArgsConstructor
public class TipoTonerController {

    private final TipoTonerService tipoTonerService;

    /**
     * Obtiene todos los tipos de tóner registrados en el sistema con paginación.
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con la lista de tipos de tóner
     */
    @GetMapping
    public ResponseEntity<Page<TipoTonerResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoTonerService.findAll(pageable));
    }

    /**
     * Busca un tipo de tóner específico por su identificador.
     * @param id Identificador único del tipo de tóner
     * @return Información detallada del tipo de tóner encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoTonerResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoTonerService.findById(id));
    }

    /**
     * Busca tipos de tóner por nombre con paginación.
     * Permite búsqueda parcial (contiene el texto especificado).
     * @param nombre Nombre o parte del nombre del tipo de tóner a buscar
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los tipos de tóner que coinciden con el nombre especificado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Page<TipoTonerResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoTonerService.findByNombre(nombre, pageable));
    }

    /**
     * Crea un nuevo tipo de tóner en el sistema.
     * @param request Datos del tipo de tóner a crear (nombre, descripción, modelo, etc.)
     * @return El tipo de tóner creado con su información completa y estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<TipoTonerResponse> create(@Valid @RequestBody TipoTonerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoTonerService.create(request));
    }

    /**
     * Actualiza la información de un tipo de tóner existente.
     * @param id Identificador único del tipo de tóner a actualizar
     * @param request Nuevos datos del tipo de tóner
     * @return El tipo de tóner actualizado con la información modificada
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoTonerResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody TipoTonerRequest request) {
        return ResponseEntity.ok(tipoTonerService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un tipo de tóner.
     * El tipo de tóner se marca como eliminado pero permanece en la base de datos.
     * @param id Identificador único del tipo de tóner a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoTonerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un tipo de tóner que fue eliminado lógicamente.
     * @param id Identificador único del tipo de tóner a restaurar
     * @return Respuesta con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        tipoTonerService.restore(id);
        return ResponseEntity.ok().build();
    }
}

