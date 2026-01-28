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
import saul.dto.request.MantenimientoRequest;
import saul.dto.response.MantenimientoResponse;
import saul.service.MantenimientoService;

import java.time.LocalDate;

/**
 * Controlador REST para gestionar los mantenimientos de dispositivos.
 * Permite registrar y consultar mantenimientos preventivos y correctivos realizados a los dispositivos.
 */
@RestController
@RequestMapping("/api/mantenimientos")
@RequiredArgsConstructor
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    /**
     * Obtiene todos los mantenimientos registrados en el sistema de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con la lista de mantenimientos (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<MantenimientoResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(mantenimientoService.findAll(pageable));
    }

    /**
     * Busca un mantenimiento específico por su ID.
     * @param id Identificador único del mantenimiento
     * @return Datos del mantenimiento encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(mantenimientoService.findById(id));
    }

    /**
     * Obtiene todo el historial de mantenimientos de un dispositivo específico.
     * Útil para consultar el registro completo de mantenimientos realizados a un equipo.
     * @param idDispositivo ID del dispositivo
     * @param pageable Parámetros de paginación
     * @return Página con todos los mantenimientos del dispositivo
     */
    @GetMapping("/dispositivo/{idDispositivo}")
    public ResponseEntity<Page<MantenimientoResponse>> findByDispositivo(
            @PathVariable Long idDispositivo,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(mantenimientoService.findByDispositivo(idDispositivo, pageable));
    }

    /**
     * Filtra los mantenimientos por tipo (preventivo, correctivo, etc.).
     * Permite obtener todos los mantenimientos de una categoría específica.
     * @param idTipoMantenimiento ID del tipo de mantenimiento
     * @param pageable Parámetros de paginación
     * @return Página con los mantenimientos del tipo especificado
     */
    @GetMapping("/tipo-mantenimiento/{idTipoMantenimiento}")
    public ResponseEntity<Page<MantenimientoResponse>> findByTipoMantenimiento(
            @PathVariable Integer idTipoMantenimiento,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(mantenimientoService.findByTipoMantenimiento(idTipoMantenimiento, pageable));
    }

    /**
     * Busca mantenimientos realizados dentro de un rango de fechas.
     * Útil para reportes de mantenimientos en un período específico.
     * @param inicio Fecha de inicio del rango (formato ISO: yyyy-MM-dd)
     * @param fin Fecha de fin del rango (formato ISO: yyyy-MM-dd)
     * @param pageable Parámetros de paginación
     * @return Página con los mantenimientos dentro del rango especificado
     */
    @GetMapping("/fecha")
    public ResponseEntity<Page<MantenimientoResponse>> findByFechaRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(mantenimientoService.findByFechaRango(inicio, fin, pageable));
    }

    /**
     * Registra un nuevo mantenimiento para un dispositivo.
     * Permite documentar mantenimientos preventivos, correctivos o de cualquier otro tipo.
     * @param request Datos del mantenimiento a registrar (validados)
     * @return Datos del mantenimiento registrado con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<MantenimientoResponse> create(@Valid @RequestBody MantenimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mantenimientoService.create(request));
    }

    /**
     * Actualiza los datos de un mantenimiento existente.
     * Permite corregir o completar información de mantenimientos ya registrados.
     * @param id ID del mantenimiento a actualizar
     * @param request Nuevos datos del mantenimiento (validados)
     * @return Datos actualizados del mantenimiento
     */
    @PutMapping("/{id}")
    public ResponseEntity<MantenimientoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MantenimientoRequest request) {
        return ResponseEntity.ok(mantenimientoService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un mantenimiento.
     * El mantenimiento no se elimina físicamente, solo se marca como eliminado.
     * @param id ID del mantenimiento a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mantenimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un mantenimiento que fue eliminado lógicamente (soft delete).
     * @param id ID del mantenimiento a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        mantenimientoService.restore(id);
        return ResponseEntity.ok().build();
    }
}

