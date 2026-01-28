package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.LugarRequest;
import saul.dto.response.LugarResponse;
import saul.service.LugarService;

/**
 * Controlador REST para gestionar los lugares o ubicaciones físicas.
 * Permite administrar espacios físicos donde se pueden ubicar dispositivos (oficinas, salas, laboratorios, etc.).
 */
@RestController
@RequestMapping("/api/lugares")
@RequiredArgsConstructor
public class LugarController {

    private final LugarService lugarService;

    /**
     * Obtiene todos los lugares del sistema de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con la lista de lugares (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<LugarResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(lugarService.findAll(pageable));
    }

    /**
     * Busca un lugar específico por su ID.
     * @param id Identificador único del lugar
     * @return Datos del lugar encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<LugarResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(lugarService.findById(id));
    }

    /**
     * Busca lugares por nombre (búsqueda parcial).
     * Útil para encontrar lugares que contengan un texto específico en su nombre.
     * @param nombre Nombre o parte del nombre del lugar a buscar
     * @param pageable Parámetros de paginación
     * @return Página con los lugares que coinciden con el nombre especificado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Page<LugarResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(lugarService.findByNombre(nombre, pageable));
    }

    /**
     * Filtra los lugares por el departamento al que pertenecen.
     * Permite obtener todos los espacios físicos de un departamento específico.
     * @param idDepartamento ID del departamento
     * @param pageable Parámetros de paginación
     * @return Página con los lugares del departamento especificado
     */
    @GetMapping("/departamento/{idDepartamento}")
    public ResponseEntity<Page<LugarResponse>> findByDepartamento(
            @PathVariable Integer idDepartamento,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(lugarService.findByDepartamento(idDepartamento, pageable));
    }

    /**
     * Crea un nuevo lugar en el sistema.
     * @param request Datos del lugar a crear (validados)
     * @return Datos del lugar creado con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<LugarResponse> create(@Valid @RequestBody LugarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lugarService.create(request));
    }

    /**
     * Actualiza los datos de un lugar existente.
     * @param id ID del lugar a actualizar
     * @param request Nuevos datos del lugar (validados)
     * @return Datos actualizados del lugar
     */
    @PutMapping("/{id}")
    public ResponseEntity<LugarResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody LugarRequest request) {
        return ResponseEntity.ok(lugarService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un lugar.
     * El lugar no se elimina físicamente, solo se marca como eliminado.
     * @param id ID del lugar a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        lugarService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un lugar que fue eliminado lógicamente (soft delete).
     * @param id ID del lugar a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        lugarService.restore(id);
        return ResponseEntity.ok().build();
    }
}

