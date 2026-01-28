package saul.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.request.UsuarioRequest;
import saul.dto.response.UsuarioResponse;
import saul.service.UsuarioService;

/**
 * Controlador REST para la gestión de usuarios del sistema.
 * Proporciona endpoints para el CRUD completo de usuarios,
 * incluyendo búsqueda por nombre, filtrado por rol y operaciones de soft delete.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Obtiene todos los usuarios registrados en el sistema con paginación.
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con la lista de usuarios
     */
    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    /**
     * Busca un usuario específico por su identificador.
     * @param id Identificador único del usuario
     * @return Información detallada del usuario encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    /**
     * Busca usuarios por nombre con paginación.
     * Permite búsqueda parcial (contiene el texto especificado).
     * @param nombre Nombre o parte del nombre del usuario a buscar
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los usuarios que coinciden con el nombre especificado
     */
    @GetMapping("/nombre")
    public ResponseEntity<Page<UsuarioResponse>> findByNombre(
            @RequestParam String nombre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findByNombre(nombre, pageable));
    }

    /**
     * Filtra usuarios por su rol con paginación.
     * @param rol Rol del usuario (ej: ADMIN, USER, TECNICO, etc.)
     * @param pageable Configuración de paginación (por defecto 20 elementos por página)
     * @return Página con los usuarios que tienen el rol especificado
     */
    @GetMapping("/rol/{rol}")
    public ResponseEntity<Page<UsuarioResponse>> findByRol(
            @PathVariable String rol,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findByRol(rol, pageable));
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * @param request Datos del usuario a crear (nombre, email, rol, contraseña, etc.)
     * @return El usuario creado con su información completa y estado HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(request));
    }

    /**
     * Actualiza la información de un usuario existente.
     * @param id Identificador único del usuario a actualizar
     * @param request Nuevos datos del usuario
     * @return El usuario actualizado con la información modificada
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    /**
     * Realiza un borrado lógico (soft delete) de un usuario.
     * El usuario se marca como eliminado pero permanece en la base de datos.
     * @param id Identificador único del usuario a eliminar
     * @return Respuesta sin contenido con estado HTTP 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Restaura un usuario que fue eliminado lógicamente.
     * @param id Identificador único del usuario a restaurar
     * @return Respuesta con estado HTTP 200 (OK)
     */
    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        usuarioService.restore(id);
        return ResponseEntity.ok().build();
    }
}

