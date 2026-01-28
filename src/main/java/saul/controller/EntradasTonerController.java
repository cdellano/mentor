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
import saul.dto.request.EntradasTonerRequest;
import saul.dto.response.EntradasTonerResponse;
import saul.service.EntradasTonerService;

import java.time.LocalDateTime;

/**
 * Controlador REST para gestionar las entradas de tóner al inventario.
 * Permite registrar y consultar las recepciones de tóner (compras, donaciones, devoluciones, etc.).
 */
@RestController
@RequestMapping("/api/entradas-toner")
@RequiredArgsConstructor
public class EntradasTonerController {

    private final EntradasTonerService entradasTonerService;

    /**
     * Obtiene todas las entradas de tóner del sistema de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con la lista de entradas de tóner (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<EntradasTonerResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(entradasTonerService.findAll(pageable));
    }

    /**
     * Busca una entrada de tóner específica por su ID.
     * @param id Identificador único de la entrada de tóner
     * @return Datos de la entrada de tóner encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntradasTonerResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(entradasTonerService.findById(id));
    }

    /**
     * Filtra las entradas de tóner por tipo de tóner.
     * Útil para consultar todas las recepciones de un tipo específico de tóner.
     * @param idTipoToner ID del tipo de tóner
     * @param pageable Parámetros de paginación
     * @return Página con las entradas del tipo de tóner especificado
     */
    @GetMapping("/tipo-toner/{idTipoToner}")
    public ResponseEntity<Page<EntradasTonerResponse>> findByTipoToner(
            @PathVariable Integer idTipoToner,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(entradasTonerService.findByTipoToner(idTipoToner, pageable));
    }

    /**
     * Filtra las entradas de tóner por usuario que las registró.
     * Permite auditar quién registró cada entrada de tóner al inventario.
     * @param idUsuario ID del usuario que registró las entradas
     * @param pageable Parámetros de paginación
     * @return Página con las entradas de tóner registradas por el usuario especificado
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<EntradasTonerResponse>> findByUsuario(
            @PathVariable Long idUsuario,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(entradasTonerService.findByUsuario(idUsuario, pageable));
    }

    /**
     * Busca entradas de tóner dentro de un rango de fechas.
     * Útil para reportes de recepciones en un período específico.
     * @param inicio Fecha y hora de inicio del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @param fin Fecha y hora de fin del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @param pageable Parámetros de paginación
     * @return Página con las entradas de tóner dentro del rango especificado
     */
    @GetMapping("/fecha")
    public ResponseEntity<Page<EntradasTonerResponse>> findByFechaRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(entradasTonerService.findByFechaRango(inicio, fin, pageable));
    }

    /**
     * Registra una nueva entrada de tóner al inventario.
     * Incrementa automáticamente el stock del tipo de tóner especificado.
     * @param request Datos de la entrada de tóner a registrar (validados)
     * @return Datos de la entrada registrada con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<EntradasTonerResponse> create(@Valid @RequestBody EntradasTonerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entradasTonerService.create(request));
    }

    /**
     * Actualiza los datos de una entrada de tóner existente.
     * Permite corregir errores en el registro de entradas.
     * @param id ID de la entrada de tóner a actualizar
     * @param request Nuevos datos de la entrada (validados)
     * @return Datos actualizados de la entrada de tóner
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntradasTonerResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody EntradasTonerRequest request) {
        return ResponseEntity.ok(entradasTonerService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de una entrada de tóner.
     * La entrada no se elimina físicamente, solo se marca como eliminada.
     * @param id ID de la entrada de tóner a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        entradasTonerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura una entrada de tóner que fue eliminada lógicamente (soft delete).
     * @param id ID de la entrada de tóner a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        entradasTonerService.restore(id);
        return ResponseEntity.ok().build();
    }
}

