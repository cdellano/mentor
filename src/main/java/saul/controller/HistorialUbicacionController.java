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
import saul.dto.request.HistorialUbicacionRequest;
import saul.dto.response.HistorialUbicacionResponse;
import saul.service.HistorialUbicacionService;

import java.time.LocalDateTime;

/**
 * Controlador REST para gestionar el historial de ubicaciones de los dispositivos.
 * Permite registrar y consultar los movimientos de dispositivos entre diferentes lugares.
 */
@RestController
@RequestMapping("/api/historial-ubicacion")
@RequiredArgsConstructor
public class HistorialUbicacionController {

    private final HistorialUbicacionService historialUbicacionService;

    /**
     * Obtiene todos los registros del historial de ubicaciones de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con la lista de registros del historial (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<HistorialUbicacionResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(historialUbicacionService.findAll(pageable));
    }

    /**
     * Busca un registro específico del historial de ubicaciones por su ID.
     * @param id Identificador único del registro del historial
     * @return Datos del registro del historial encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistorialUbicacionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(historialUbicacionService.findById(id));
    }

    /**
     * Obtiene todo el historial de ubicaciones de un dispositivo específico.
     * Muestra todos los movimientos del dispositivo ordenados cronológicamente.
     * @param idDispositivo ID del dispositivo
     * @param pageable Parámetros de paginación
     * @return Página con todos los registros de ubicaciones del dispositivo
     */
    @GetMapping("/dispositivo/{idDispositivo}")
    public ResponseEntity<Page<HistorialUbicacionResponse>> findByDispositivo(
            @PathVariable Long idDispositivo,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(historialUbicacionService.findByDispositivo(idDispositivo, pageable));
    }

    /**
     * Obtiene la ubicación actual de un dispositivo específico.
     * Devuelve el registro del historial donde fechaSalida es NULL, es decir, la ubicación actual.
     * @param idDispositivo ID del dispositivo
     * @return Ubicación actual del dispositivo
     */
    @GetMapping("/dispositivo/{idDispositivo}/actual")
    public ResponseEntity<HistorialUbicacionResponse> findCurrentByDispositivo(
            @PathVariable Long idDispositivo) {
        return ResponseEntity.ok(historialUbicacionService.findCurrentByDispositivo(idDispositivo));
    }

    /**
     * Obtiene el historial de todos los dispositivos que han estado en un lugar específico.
     * Incluye tanto dispositivos que actualmente están en el lugar como los que estuvieron anteriormente.
     * @param idLugar ID del lugar
     * @param pageable Parámetros de paginación
     * @return Página con los registros del historial asociados al lugar
     */
    @GetMapping("/lugar/{idLugar}")
    public ResponseEntity<Page<HistorialUbicacionResponse>> findByLugar(
            @PathVariable Integer idLugar,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(historialUbicacionService.findByLugar(idLugar, pageable));
    }

    /**
     * Busca registros del historial dentro de un rango de fechas.
     * Útil para auditorías o reportes de movimientos en un período específico.
     * @param inicio Fecha y hora de inicio del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @param fin Fecha y hora de fin del rango (formato ISO: yyyy-MM-dd'T'HH:mm:ss)
     * @param pageable Parámetros de paginación
     * @return Página con los registros del historial dentro del rango especificado
     */
    @GetMapping("/fecha")
    public ResponseEntity<Page<HistorialUbicacionResponse>> findByFechaRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(historialUbicacionService.findByFechaRango(inicio, fin, pageable));
    }

    /**
     * Registra un nuevo movimiento de dispositivo (entrada a una nueva ubicación).
     * Crea un registro de historial cuando un dispositivo es trasladado a un nuevo lugar.
     * @param request Datos del nuevo registro del historial (validados)
     * @return Datos del registro creado con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<HistorialUbicacionResponse> create(@Valid @RequestBody HistorialUbicacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historialUbicacionService.create(request));
    }

    /**
     * Actualiza un registro existente del historial de ubicaciones.
     * Permite modificar fechas o agregar notas sobre un movimiento ya registrado.
     * @param id ID del registro del historial a actualizar
     * @param request Nuevos datos del registro (validados)
     * @return Datos actualizados del registro
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistorialUbicacionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody HistorialUbicacionRequest request) {
        return ResponseEntity.ok(historialUbicacionService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un registro del historial.
     * El registro no se elimina físicamente, solo se marca como eliminado.
     * @param id ID del registro del historial a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        historialUbicacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un registro del historial que fue eliminado lógicamente (soft delete).
     * @param id ID del registro del historial a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        historialUbicacionService.restore(id);
        return ResponseEntity.ok().build();
    }
}

