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
import saul.dto.request.BitacoraServicioRequest;
import saul.dto.response.BitacoraServicioResponse;
import saul.service.BitacoraServicioService;

import java.time.LocalDateTime;

/**
 * Controlador REST para gestionar los registros de la bitácora de servicios.
 */
@RestController
@RequestMapping("/api/bitacora-servicios")
@RequiredArgsConstructor
public class BitacoraServicioController {
    private final BitacoraServicioService bitacoraServicioService;

    /**
     * Obtiene todos los registros de bitácora de servicios de forma paginada.
     *
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con lista de registros de bitácora (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<BitacoraServicioResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(bitacoraServicioService.findAll(pageable));
    }

    /**
     * Busca un registro de bitácora de servicio específico por su ID.
     *
     * @param id Identificador único del registro de bitácora
     * @return Datos del registro de bitácora encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<BitacoraServicioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(bitacoraServicioService.findById(id));
    }

    /**
     * Obtiene todos los registros de bitácora asociados a un servicio específico.
     *
     * @param servicioId ID del servicio del cual se quieren consultar los registros
     * @param pageable   Parámetros de paginación
     * @return Página con los registros de bitácora del servicio especificado
     */
    @GetMapping("/servicio/{servicioId}")
    public ResponseEntity<Page<BitacoraServicioResponse>> findByServicio(
            @PathVariable Long servicioId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(bitacoraServicioService.findByServicio(servicioId, pageable));
    }

    /**
     * Filtra los registros de bitácora por estado del servicio.
     * Estados posibles: NORMAL, DEGRADADO, CAIDO, MANTENIMIENTO, SOPORTE_TECNICO_PROV
     *
     * @param estado   Estado del servicio a filtrar
     * @param pageable Parámetros de paginación
     * @return Página con los registros de bitácora que coinciden con el estado especificado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<BitacoraServicioResponse>> findByEstado(
            @PathVariable String estado,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(bitacoraServicioService.findByEstado(estado, pageable));
    }

    /**
     * Busca registros de bitácora creados dentro de un rango de fechas específico.
     *
     * @param inicio   Fecha y hora de inicio del rango (formato ISO 8601)
     * @param fin      Fecha y hora de fin del rango (formato ISO 8601)
     * @param pageable Parámetros de paginación
     * @return Página con los registros de bitácora creados entre las fechas especificadas
     */
    @GetMapping("/fecha")
    public ResponseEntity<Page<BitacoraServicioResponse>> findByFechaRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(bitacoraServicioService.findByFechaRango(inicio, fin, pageable));
    }

    /**
     * Obtiene todos los registros de bitácora reportados por un usuario específico.
     *
     * @param idUsuario ID del usuario que reportó los incidentes
     * @param pageable  Parámetros de paginación
     * @return Página con los registros de bitácora reportados por el usuario especificado
     */
    @GetMapping("/reportado-por/{idUsuario}")
    public ResponseEntity<Page<BitacoraServicioResponse>> findByReportadoPor(
            @PathVariable Long idUsuario,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(bitacoraServicioService.findByReportadoPor(idUsuario, pageable));
    }

    /**
     * Filtra los registros de bitácora por tipo de incidente.
     *
     * @param tipoIncidenteId ID del tipo de incidente a filtrar
     * @param pageable        Parámetros de paginación
     * @return Página con los registros de bitácora del tipo de incidente especificado
     */
    @GetMapping("/tipo-incidente/{tipoIncidenteId}")
    public ResponseEntity<Page<BitacoraServicioResponse>> findByTipoIncidente(
            @PathVariable Long tipoIncidenteId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(bitacoraServicioService.findByTipoIncidente(tipoIncidenteId, pageable));
    }

    /**
     * Crea un nuevo registro de bitácora de servicio.
     *
     * @param request Datos del registro a crear (validados)
     * @return Datos del registro de bitácora creado con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<BitacoraServicioResponse> create(@Valid @RequestBody BitacoraServicioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bitacoraServicioService.create(request));
    }

    /**
     * Actualiza los datos de un registro de bitácora de servicio existente.
     *
     * @param id    ID del registro de bitácora a actualizar
     * @param request Nuevos datos del registro (validados)
     * @return Datos actualizados del registro de bitácora
     */
    @PutMapping("/{id}")
    public ResponseEntity<BitacoraServicioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BitacoraServicioRequest request) {
        return ResponseEntity.ok(bitacoraServicioService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un registro de bitácora de servicio.
     * El registro no se elimina físicamente, solo se marca como eliminado.
     *
     * @param id ID del registro de bitácora a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bitacoraServicioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un registro de bitácora de servicio que fue eliminado lógicamente (soft delete).
     *
     * @param id ID del registro de bitácora a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        bitacoraServicioService.restore(id);
        return ResponseEntity.ok().build();
    }
}
