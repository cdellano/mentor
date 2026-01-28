package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.AnotacionRequest;
import saul.dto.response.AnotacionResponse;
import saul.entity.Anotacion;
import saul.entity.Usuario;
import saul.exception.ResourceNotFoundException;
import saul.repository.AnotacionRepository;
import saul.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AnotacionService {

    private final AnotacionRepository anotacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntityMapper mapper;

    public Page<AnotacionResponse> findAll(Pageable pageable) {
        return anotacionRepository.findAll(pageable).map(mapper::toResponse);
    }

    public AnotacionResponse findById(Long id) {
        Anotacion anotacion = anotacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion", "id", id));
        return mapper.toResponse(anotacion);
    }

    public Page<AnotacionResponse> findByUsuario(Long idUsuario, Pageable pageable) {
        return anotacionRepository.findByUsuarioIdUsuario(idUsuario, pageable).map(mapper::toResponse);
    }

    public Page<AnotacionResponse> findByPagina(Integer pagina, Pageable pageable) {
        return anotacionRepository.findByPagina(pagina, pageable).map(mapper::toResponse);
    }

    public Page<AnotacionResponse> findByEtiqueta(String etiqueta, Pageable pageable) {
        return anotacionRepository.findByEtiquetaContaining(etiqueta, pageable).map(mapper::toResponse);
    }

    public Page<AnotacionResponse> findByContenido(String contenido, Pageable pageable) {
        return anotacionRepository.findByContenidoContaining(contenido, pageable).map(mapper::toResponse);
    }

    public Page<AnotacionResponse> findByFechaRango(LocalDateTime inicio, LocalDateTime fin, Pageable pageable) {
        return anotacionRepository.findByFechaAnotacionBetween(inicio, fin, pageable).map(mapper::toResponse);
    }

    public AnotacionResponse create(AnotacionRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuario()));

        Anotacion anotacion = new Anotacion();
        anotacion.setUsuario(usuario);
        anotacion.setTitulo(request.getTitulo());
        anotacion.setContenido(request.getContenido());
        anotacion.setPagina(request.getPagina());
        anotacion.setEtiquetas(request.getEtiquetas());
        anotacion.setImportante(request.getImportante() != null ? request.getImportante() : false);
        anotacion.setFechaAnotacion(LocalDateTime.now());
        anotacion.setBorrado(false);

        return mapper.toResponse(anotacionRepository.save(anotacion));
    }

    public AnotacionResponse update(Long id, AnotacionRequest request) {
        Anotacion anotacion = anotacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion", "id", id));

        if (request.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuario()));
            anotacion.setUsuario(usuario);
        }

        anotacion.setTitulo(request.getTitulo());
        anotacion.setContenido(request.getContenido());
        anotacion.setPagina(request.getPagina());
        anotacion.setEtiquetas(request.getEtiquetas());
        if (request.getImportante() != null) {
            anotacion.setImportante(request.getImportante());
        }

        return mapper.toResponse(anotacionRepository.save(anotacion));
    }

    public void delete(Long id) {
        Anotacion anotacion = anotacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion", "id", id));
        anotacion.setBorrado(true);
        anotacionRepository.save(anotacion);
    }

    public void restore(Long id) {
        Anotacion anotacion = anotacionRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotacion eliminada", "id", id));
        anotacion.setBorrado(false);
        anotacionRepository.save(anotacion);
    }
}

