package saul.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.ServicioRequest;
import saul.dto.response.ServicioResponse;
import saul.service.ServicioService;

/**
 * Controlador REST para gestionar los servicios informáticos.
 * Permite administrar servicios como correo electrónico, servidores, aplicaciones web, etc.
 */
@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {
    private final ServicioService servicioService;

    /**
     * Obtiene todos los servicios del sistema de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con la lista de servicios (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<ServicioResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(servicioService.findAll(pageable));
    }

    /**
     * Busca un servicio específico por su ID.
     * @param id Identificador único del servicio
     * @return Datos del servicio encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.findById(id));
    }

    /**
     * Busca servicios por nombre (búsqueda parcial).
     * Útil para encontrar servicios que contengan un texto específico en su nombre.
     * @param nombre Nombre o parte del nombre del servicio a buscar
     * @param pageable Parámetros de paginación
     * @return Página con los servicios que coinciden con el nombre especificado
     */
    @GetMapping("/buscar")
    public ResponseEntity<Page<ServicioResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(servicioService.findByNombre(nombre, pageable));
    }

    /**
     * Obtiene todos los servicios activos del sistema.
     * Devuelve solo servicios que están operativos y en uso.
     * @param pageable Parámetros de paginación
     * @return Página con los servicios activos
     */
    @GetMapping("/activos")
    public ResponseEntity<Page<ServicioResponse>> findActivos(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(servicioService.findByActivo(true, pageable));
    }

    /**
     * Obtiene todos los servicios inactivos del sistema.
     * Devuelve servicios que están fuera de operación o descontinuados.
     * @param pageable Parámetros de paginación
     * @return Página con los servicios inactivos
     */
    @GetMapping("/inactivos")
    public ResponseEntity<Page<ServicioResponse>> findInactivos(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(servicioService.findByActivo(false, pageable));
    }

    /**
     * Registra un nuevo servicio informático en el sistema.
     * @param request Datos del servicio a crear (validados)
     * @return Datos del servicio creado con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<ServicioResponse> create(@Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.create(request));
    }

    /**
     * Actualiza los datos de un servicio existente.
     * @param id ID del servicio a actualizar
     * @param request Nuevos datos del servicio (validados)
     * @return Datos actualizados del servicio
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.ok(servicioService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un servicio.
     * El servicio no se elimina físicamente, solo se marca como eliminado.
     * @param id ID del servicio a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un servicio que fue eliminado lógicamente (soft delete).
     * @param id ID del servicio a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        servicioService.restore(id);
        return ResponseEntity.ok().build();
    }
}
