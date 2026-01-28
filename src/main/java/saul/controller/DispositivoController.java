package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.DispositivoRequest;
import saul.dto.response.DispositivoResponse;
import saul.service.DispositivoService;

/**
 * Controlador REST para gestionar los dispositivos del inventario.
 */
@RestController
@RequestMapping("/api/dispositivos")
@RequiredArgsConstructor
public class DispositivoController {

    private final DispositivoService dispositivoService;

    /**
     * Obtiene todos los dispositivos del sistema de forma paginada.
     * @param pageable Parámetros de paginación (página, tamaño, ordenamiento)
     * @return Página con lista de dispositivos (por defecto 20 elementos por página)
     */
    @GetMapping
    public ResponseEntity<Page<DispositivoResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(dispositivoService.findAll(pageable));
    }

    /**
     * Busca un dispositivo específico por su ID.
     * @param id Identificador único del dispositivo
     * @return Datos del dispositivo encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<DispositivoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(dispositivoService.findById(id));
    }

    /**
     * Filtra los dispositivos por su tipo de estado.
     * @param idTipoEstado ID del tipo de estado del dispositivo (ej: operativo, en reparación, dado de baja)
     * @param pageable Parámetros de paginación
     * @return Página con los dispositivos que coinciden con el tipo de estado especificado
     */
    @GetMapping("/tipo-estado/{idTipoEstado}")
    public ResponseEntity<Page<DispositivoResponse>> findByTipoEstado(
            @PathVariable Integer idTipoEstado,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(dispositivoService.findByTipoEstado(idTipoEstado, pageable));
    }

    /**
     * Busca un dispositivo por su número de serie único.
     * @param numeroSerie Número de serie del dispositivo
     * @return Datos del dispositivo con el número de serie especificado
     */
    @GetMapping("/numero-serie/{numeroSerie}")
    public ResponseEntity<DispositivoResponse> findByNumeroSerie(@PathVariable String numeroSerie) {
        return ResponseEntity.ok(dispositivoService.findByNumeroSerie(numeroSerie));
    }

    /**
     * Filtra los dispositivos por su tipo (ej: computadora, impresora, proyector).
     * @param idTipoDispositivo ID del tipo de dispositivo
     * @param pageable Parámetros de paginación
     * @return Página con los dispositivos del tipo especificado
     */
    @GetMapping("/tipo-dispositivo/{idTipoDispositivo}")
    public ResponseEntity<Page<DispositivoResponse>> findByTipoDispositivo(
            @PathVariable Integer idTipoDispositivo,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(dispositivoService.findByTipoDispositivo(idTipoDispositivo, pageable));
    }

    /**
     * Busca dispositivos por modelo (búsqueda parcial).
     * @param modelo Modelo o parte del modelo del dispositivo a buscar
     * @param pageable Parámetros de paginación
     * @return Página con los dispositivos que coinciden con el modelo especificado
     */
    @GetMapping("/modelo")
    public ResponseEntity<Page<DispositivoResponse>> findByModelo(
            @RequestParam String modelo,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(dispositivoService.findByModelo(modelo, pageable));
    }


    /**
     * Filtra los dispositivos por su ubicación (lugar) actual.
     * @param idLugar ID del lugar donde se encuentran actualmente los dispositivos
     * @param pageable Parámetros de paginación
     * @return Página con los dispositivos ubicados en el lugar especificado
     */
    @GetMapping("/ubicacion-actual/lugar/{idLugar}")
    public ResponseEntity<Page<DispositivoResponse>> findByCurrentLugar(
            @PathVariable Integer idLugar,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(dispositivoService.findByCurrentLugar(idLugar, pageable));
    }

    /**
     * Filtra los dispositivos por el departamento al que pertenece su ubicación actual.
     * @param idDepartamento ID del departamento
     * @param pageable Parámetros de paginación
     * @return Página con los dispositivos ubicados actualmente en el departamento especificado
     */
    @GetMapping("/ubicacion-actual/departamento/{idDepartamento}")
    public ResponseEntity<Page<DispositivoResponse>> findByCurrentDepartamento(
            @PathVariable Integer idDepartamento,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(dispositivoService.findByCurrentDepartamento(idDepartamento, pageable));
    }

    /**
     * Obtiene todos los dispositivos activos (no dados de baja) con su ubicación más reciente.
     * Filtra solo dispositivos que no estén en estado "Baja" y que no hayan sido eliminados lógicamente.
     * Incluye la información de su historial de ubicaciones actual (donde fechaSalida es NULL).
     * @param pageable Parámetros de paginación
     * @return Página con los dispositivos activos con su ubicación actual
     */
    @GetMapping("/activos-con-ubicacion")
    public ResponseEntity<Page<DispositivoResponse>> findAllActiveWithCurrentLocation(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(dispositivoService.findAllActiveWithCurrentLocation(pageable));
    }

    /**
     * Obtiene un dispositivo aleatorio que no ha recibido mantenimiento en el número especificado de días,
     * filtrado por tipo de dispositivo.
     *
     * Este endpoint es útil para:
     * - Seleccionar aleatoriamente dispositivos que requieren atención de mantenimiento
     * - Auditorías aleatorias de equipos rezagados
     * - Asignación aleatoria de mantenimientos preventivos
     *
     * @param diasMinimos Número mínimo de días sin recibir mantenimiento (obligatorio)
     * @param idTipoDispositivo ID del tipo de dispositivo a filtrar (obligatorio)
     * @return Un dispositivo aleatorio que cumple con los criterios especificados
     *
     * Ejemplo de uso:
     * GET /api/dispositivos/aleatorio-rezagado?diasMinimos=90&idTipoDispositivo=1
     *
     * Response exitoso (200):
     * {
     *   "idDispositivo": 25,
     *   "marca": "Dell",
     *   "modelo": "OptiPlex 3080",
     *   "numeroSerie": "ABC123456",
     *   "inventario": "INV-2024-025",
     *   "tipoDispositivo": "Computadora de Escritorio",
     *   "tipoEstado": "Operativo",
     *   "fechaCompra": "2023-05-15",
     *   ...
     * }
     *
     * Response sin resultados (404):
     * {
     *   "timestamp": "2024-01-15T10:30:00",
     *   "status": 404,
     *   "error": "Not Found",
     *   "message": "No se encontró: Dispositivo con criterio tipo=1, días sin mantenimiento>90",
     *   "path": "/api/dispositivos/aleatorio-rezagado"
     * }
     */
    @GetMapping("/aleatorio-rezagado")
    public ResponseEntity<DispositivoResponse> findRandomEquipoRezagadoEnMantenimiento(
            @RequestParam Integer diasMinimos,
            @RequestParam Integer idTipoDispositivo) {
        return ResponseEntity.ok(dispositivoService.findRandomEquipoRezagadoEnMantenimiento(diasMinimos, idTipoDispositivo));
    }

    /**
     * Crea un nuevo dispositivo en el sistema.
     * @param request Datos del dispositivo a crear (validados)
     * @return Datos del dispositivo creado con estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<DispositivoResponse> create(@Valid @RequestBody DispositivoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dispositivoService.create(request));
    }

    /**
     * Actualiza los datos de un dispositivo existente.
     * @param id ID del dispositivo a actualizar
     * @param request Nuevos datos del dispositivo (validados)
     * @return Datos actualizados del dispositivo
     */
    @PutMapping("/{id}")
    public ResponseEntity<DispositivoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DispositivoRequest request) {
        return ResponseEntity.ok(dispositivoService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un dispositivo.
     * El dispositivo no se elimina físicamente, solo se marca como eliminado.
     * @param id ID del dispositivo a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dispositivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un dispositivo que fue eliminado lógicamente (soft delete).
     * @param id ID del dispositivo a restaurar
     * @return Respuesta sin contenido con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        dispositivoService.restore(id);
        return ResponseEntity.ok().build();
    }
}
