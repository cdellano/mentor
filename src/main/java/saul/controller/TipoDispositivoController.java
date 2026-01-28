package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.TipoDispositivoRequest;
import saul.dto.response.TipoDispositivoResponse;
import saul.service.TipoDispositivoService;

/**
 * Controlador REST para la gestión de tipos de dispositivos.
 * Proporciona endpoints para el CRUD completo de categorías de dispositivos
 * (ej: impresoras, computadoras, escáneres, routers, etc.).
 */
@RestController
@RequestMapping("/api/tipos-dispositivo")
@RequiredArgsConstructor
public class TipoDispositivoController {

    private final TipoDispositivoService tipoDispositivoService;

    /**
     * Obtiene todos los tipos de dispositivos registrados en el sistema con paginación.
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con la lista de tipos de dispositivos
     */
    @GetMapping
    public ResponseEntity<Page<TipoDispositivoResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoDispositivoService.findAll(pageable));
    }

    /**
     * Busca un tipo de dispositivo específico por su identificador.
     * @param id Identificador único del tipo de dispositivo
     * @return Información detallada del tipo de dispositivo encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoDispositivoResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoDispositivoService.findById(id));
    }

    /**
     * Busca tipos de dispositivos por nombre con paginación.
     * Permite búsqueda parcial (contiene el texto especificado).
     * @param nombre Nombre o parte del nombre del tipo de dispositivo a buscar
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los tipos de dispositivos que coinciden con el nombre especificado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Page<TipoDispositivoResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tipoDispositivoService.findByNombre(nombre, pageable));
    }

    /**
     * Crea un nuevo tipo de dispositivo en el sistema.
     * @param request Datos del tipo de dispositivo a crear (nombre, descripción, etc.)
     * @return El tipo de dispositivo creado con su información completa y estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<TipoDispositivoResponse> create(@Valid @RequestBody TipoDispositivoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoDispositivoService.create(request));
    }

    /**
     * Actualiza la información de un tipo de dispositivo existente.
     * @param id Identificador único del tipo de dispositivo a actualizar
     * @param request Nuevos datos del tipo de dispositivo
     * @return El tipo de dispositivo actualizado con la información modificada
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoDispositivoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody TipoDispositivoRequest request) {
        return ResponseEntity.ok(tipoDispositivoService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un tipo de dispositivo.
     * El tipo de dispositivo se marca como eliminado pero permanece en la base de datos.
     * @param id Identificador único del tipo de dispositivo a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoDispositivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un tipo de dispositivo que fue eliminado lógicamente.
     * @param id Identificador único del tipo de dispositivo a restaurar
     * @return Respuesta con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        tipoDispositivoService.restore(id);
        return ResponseEntity.ok().build();
    }
}

