package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.EntradasTonerRequest;
import saul.dto.response.EntradasTonerResponse;
import saul.entity.EntradasToner;
import saul.entity.TipoToner;
import saul.entity.Usuario;
import saul.exception.ResourceNotFoundException;
import saul.repository.EntradasTonerRepository;
import saul.repository.TipoTonerRepository;
import saul.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class EntradasTonerService {

    private final EntradasTonerRepository entradasTonerRepository;
    private final TipoTonerRepository tipoTonerRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntityMapper mapper;

    public Page<EntradasTonerResponse> findAll(Pageable pageable) {
        return entradasTonerRepository.findAll(pageable).map(mapper::toResponse);
    }

    public EntradasTonerResponse findById(Integer id) {
        EntradasToner entrada = entradasTonerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntradasToner", "id", id));
        return mapper.toResponse(entrada);
    }

    public Page<EntradasTonerResponse> findByTipoToner(Integer idTipoToner, Pageable pageable) {
        return entradasTonerRepository.findByTipoTonerId(idTipoToner, pageable)
                .map(mapper::toResponse);
    }

    public Page<EntradasTonerResponse> findByUsuario(Long idUsuario, Pageable pageable) {
        return entradasTonerRepository.findByUsuarioEntradaIdUsuario(idUsuario, pageable)
                .map(mapper::toResponse);
    }

    public Page<EntradasTonerResponse> findByFechaRango(LocalDateTime inicio, LocalDateTime fin, Pageable pageable) {
        return entradasTonerRepository.findByFechaEntradaBetween(inicio, fin, pageable)
                .map(mapper::toResponse);
    }

    public EntradasTonerResponse create(EntradasTonerRequest request) {
        EntradasToner entrada = mapper.toEntity(request);

        Usuario usuario = usuarioRepository.findById(request.getIdUsuarioEntrada())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuarioEntrada()));
        entrada.setUsuarioEntrada(usuario);

        TipoToner tipoToner = tipoTonerRepository.findById(request.getIdTipoToner())
                .orElseThrow(() -> new ResourceNotFoundException("TipoToner", "id", request.getIdTipoToner()));
        entrada.setTipoToner(tipoToner);

        entrada.setFechaEntrada(LocalDateTime.now());
        entrada.setBorrado(false);

        return mapper.toResponse(entradasTonerRepository.save(entrada));
    }

    public EntradasTonerResponse update(Integer id, EntradasTonerRequest request) {
        EntradasToner entrada = entradasTonerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntradasToner", "id", id));

        mapper.updateEntity(entrada, request);

        if (request.getIdTipoToner() != null) {
            TipoToner tipoToner = tipoTonerRepository.findById(request.getIdTipoToner())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoToner", "id", request.getIdTipoToner()));
            entrada.setTipoToner(tipoToner);
        }

        return mapper.toResponse(entradasTonerRepository.save(entrada));
    }

    public void delete(Integer id) {
        EntradasToner entrada = entradasTonerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntradasToner", "id", id));
        entrada.setBorrado(true);
        entradasTonerRepository.save(entrada);
    }

    public void restore(Integer id) {
        EntradasToner entrada = entradasTonerRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntradasToner eliminada", "id", id));
        entrada.setBorrado(false);
        entradasTonerRepository.save(entrada);
    }
}

