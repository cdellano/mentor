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
import saul.dto.request.SalidasTonerRequest;
import saul.dto.response.SalidasTonerResponse;
import saul.service.SalidasTonerService;

import java.time.LocalDateTime;

/**
 * Controlador REST para gestionar las salidas de tóner del inventario.
 * Permite registrar y consultar las entregas de tóner a usuarios o departamentos.
 */
@RestController
@RequestMapping("/api/salidas-toner")
@RequiredArgsConstructor
public class SalidasTonerController {

    private final SalidasTonerService salidasTonerService;

    /**
     * Obtiene todas las salidas de tóner del sistema de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con la lista de salidas de tóner (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<SalidasTonerResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(salidasTonerService.findAll(pageable));
    }

    /**
     * Busca una salida de tóner específica por su ID.
     * @param id Identificador único de la salida de tóner
     * @return Datos de la salida de tóner encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<SalidasTonerResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(salidasTonerService.findById(id));
    }

    /**
     * Filtra las salidas de tóner por tipo de tóner.
     * Útil para consultar todas las entregas de un tipo específico de tóner.
     * @param idTipoToner ID del tipo de tóner
     * @param pageable Parámetros de paginación
     * @return Página con las salidas del tipo de tóner especificado
     */
    @GetMapping("/tipo-toner/{idTipoToner}")
    public ResponseEntity<Page<SalidasTonerResponse>> findByTipoToner(
            @PathVariable Integer idTipoToner,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(salidasTonerService.findByTipoToner(idTipoToner, pageable));
    }

    /**
     * Filtra las salidas de tóner por usuario que las registró.
     * Permite auditar quién registró cada salida de tóner del inventario.
     * @param idUsuario ID del usuario que registró las salidas
     * @param pageable Parámetros de paginación
     * @return Página con las salidas de tóner registradas por el usuario especificado
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<SalidasTonerResponse>> findByUsuario(
            @PathVariable Long idUsuario,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(salidasTonerService.findByUsuario(idUsuario, pageable));
    }

    /**
     * Busca salidas de tóner dentro de un rango de fechas.
     * Útil para reportes de entregas en un período específico.
     * @param inicio Fecha y hora de inicio del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @param fin Fecha y hora de fin del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @param pageable Parámetros de paginación
     * @return Página con las salidas de tóner dentro del rango especificado
     */
    @GetMapping("/fecha")
    public ResponseEntity<Page<SalidasTonerResponse>> findByFechaRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(salidasTonerService.findByFechaRango(inicio, fin, pageable));
    }

    /**
     * Registra una nueva salida de tóner del inventario.
     * Decrementa automáticamente el stock del tipo de tóner especificado.
     * @param request Datos de la salida de tóner a registrar (validados)
     * @return Datos de la salida registrada con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<SalidasTonerResponse> create(@Valid @RequestBody SalidasTonerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salidasTonerService.create(request));
    }

    /**
     * Actualiza los datos de una salida de tóner existente.
     * Permite corregir errores en el registro de salidas.
     * @param id ID de la salida de tóner a actualizar
     * @param request Nuevos datos de la salida (validados)
     * @return Datos actualizados de la salida de tóner
     */
    @PutMapping("/{id}")
    public ResponseEntity<SalidasTonerResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody SalidasTonerRequest request) {
        return ResponseEntity.ok(salidasTonerService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de una salida de tóner.
     * La salida no se elimina físicamente, solo se marca como eliminada.
     * @param id ID de la salida de tóner a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        salidasTonerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura una salida de tóner que fue eliminada lógicamente (soft delete).
     * @param id ID de la salida de tóner a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Integer id) {
        salidasTonerService.restore(id);
        return ResponseEntity.ok().build();
    }
}

