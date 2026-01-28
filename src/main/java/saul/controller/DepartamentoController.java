package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.DepartamentoRequest;
import saul.dto.response.DepartamentoResponse;
import saul.service.DepartamentoService;

/**
 * Controlador REST para gestionar los departamentos.
 */
@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    /**
     * Obtiene todos los departamentos de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con lista de departamentos (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<DepartamentoResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(departamentoService.findAll(pageable));
    }

    /**
     * Busca un departamento específico por su ID.
     * @param id Identificador único del departamento
     * @return Datos del departamento encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(departamentoService.findById(id));
    }

    /**
     * Busca departamentos por nombre (búsqueda parcial).
     * @param nombre Nombre o parte del nombre del departamento a buscar
     * @param pageable Parámetros de paginación
     * @return Página con los departamentos que coinciden con el nombre especificado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Page<DepartamentoResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(departamentoService.findByNombre(nombre, pageable));
    }

    /**
     * Crea un nuevo departamento en el sistema.
     * @param request Datos del departamento a crear (validados)
     * @return Datos del departamento creado con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<DepartamentoResponse> create(@Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoService.create(request));
    }

    /**
     * Actualiza los datos de un departamento existente.
     * @param id ID del departamento a actualizar
     * @param request Nuevos datos del departamento (validados)
     * @return Datos actualizados del departamento
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.ok(departamentoService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un departamento.
     * El departamento no se elimina físicamente, solo se marca como eliminado.
     * @param id ID del departamento a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        departamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un departamento que fue eliminado lógicamente (soft delete).
     * @param id ID del departamento a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        departamentoService.restore(id);
        return ResponseEntity.ok().build();
    }
}
