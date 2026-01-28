package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.UsuarioRequest;
import saul.dto.response.UsuarioResponse;
import saul.entity.Usuario;
import saul.exception.ResourceNotFoundException;
import saul.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EntityMapper mapper;

    public Page<UsuarioResponse> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(mapper::toResponse);
    }

    public UsuarioResponse findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        return mapper.toResponse(usuario);
    }

    public Page<UsuarioResponse> findByNombre(String nombre, Pageable pageable) {
        return usuarioRepository.findByNombreContaining(nombre, pageable)
                .map(mapper::toResponse);
    }

    public Page<UsuarioResponse> findByRol(String rol, Pageable pageable) {
        return usuarioRepository.findByRol(rol, pageable)
                .map(mapper::toResponse);
    }

    public UsuarioResponse create(UsuarioRequest request) {
        Usuario usuario = mapper.toEntity(request);
        usuario.setBorrado(false);
        return mapper.toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponse update(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        mapper.updateEntity(usuario, request);
        return mapper.toResponse(usuarioRepository.save(usuario));
    }

    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
        usuario.setBorrado(true);
        usuarioRepository.save(usuario);
    }

    public void restore(Long id) {
        Usuario usuario = usuarioRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario eliminado", "id", id));
        usuario.setBorrado(false);
        usuarioRepository.save(usuario);
    }
}

