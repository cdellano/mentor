package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.AnotacionRequest;
import saul.dto.response.AnotacionResponse;
import saul.service.AnotacionService;

import java.time.LocalDateTime;

/**
 * Controlador REST para gestionar las anotaciones de documentos PDF.
 */
@RestController
@RequestMapping("/api/anotaciones")
@RequiredArgsConstructor
public class AnotacionController {

    private final AnotacionService anotacionService;

    /**
     * Obtiene todas las anotaciones del sistema de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con lista de anotaciones (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<AnotacionResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(anotacionService.findAll(pageable));
    }

    /**
     * Busca una anotación específica por su ID.
     * @param id Identificador único de la anotación
     * @return Datos de la anotación encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnotacionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(anotacionService.findById(id));
    }

    /**
     * Obtiene todas las anotaciones creadas por un usuario específico de forma paginada.
     * @param idUsuario ID del usuario que creó las anotaciones
     * @param pageable Parámetros de paginación
     * @return Página con las anotaciones del usuario especificado
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<AnotacionResponse>> findByUsuario(
            @PathVariable Long idUsuario,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(anotacionService.findByUsuario(idUsuario, pageable));
    }

    /**
     * Filtra las anotaciones por número de página del documento al que pertenecen.
     * @param pagina Número de página del documento
     * @param pageable Parámetros de paginación
     * @return Página con las anotaciones que pertenecen a esa página del documento
     */
    @GetMapping("/pagina/{pagina}")
    public ResponseEntity<Page<AnotacionResponse>> findByPagina(
            @PathVariable Integer pagina,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(anotacionService.findByPagina(pagina, pageable));
    }

    /**
     * Busca anotaciones que contengan una etiqueta específica.
     * @param etiqueta Etiqueta a buscar
     * @param pageable Parámetros de paginación
     * @return Página con las anotaciones que contienen la etiqueta especificada
     */
    @GetMapping("/etiqueta")
    public ResponseEntity<Page<AnotacionResponse>> findByEtiqueta(
            @RequestParam String etiqueta,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(anotacionService.findByEtiqueta(etiqueta, pageable));
    }

    /**
     * Busca anotaciones por texto contenido en su descripción o contenido.
     * @param contenido Texto a buscar en el contenido de las anotaciones
     * @param pageable Parámetros de paginación
     * @return Página con las anotaciones que contienen el texto especificado
     */
    @GetMapping("/contenido")
    public ResponseEntity<Page<AnotacionResponse>> findByContenido(
            @RequestParam String contenido,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(anotacionService.findByContenido(contenido, pageable));
    }

    /**
     * Busca anotaciones creadas dentro de un rango de fechas específico.
     * @param inicio Fecha y hora de inicio del rango (formato ISO 8601)
     * @param fin Fecha y hora de fin del rango (formato ISO 8601)
     * @param pageable Parámetros de paginación
     * @return Página con las anotaciones creadas entre las fechas especificadas
     */
    @GetMapping("/fecha")
    public ResponseEntity<Page<AnotacionResponse>> findByFechaRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(anotacionService.findByFechaRango(inicio, fin, pageable));
    }

    /**
     * Crea una nueva anotación en el sistema.
     * @param request Datos de la anotación a crear (validados)
     * @return Datos de la anotación creada con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<AnotacionResponse> create(@Valid @RequestBody AnotacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anotacionService.create(request));
    }

    /**
     * Actualiza los datos de una anotación existente.
     * @param id ID de la anotación a actualizar
     * @param request Nuevos datos de la anotación (validados)
     * @return Datos actualizados de la anotación
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnotacionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AnotacionRequest request) {
        return ResponseEntity.ok(anotacionService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de una anotación.
     * La anotación no se elimina físicamente, solo se marca como eliminada.
     * @param id ID de la anotación a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        anotacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura una anotación que fue eliminada lógicamente (soft delete).
     * @param id ID de la anotación a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        anotacionService.restore(id);
        return ResponseEntity.ok().build();
    }
}
