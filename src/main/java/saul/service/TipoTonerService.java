package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.TipoTonerRequest;
import saul.dto.response.TipoTonerResponse;
import saul.entity.TipoToner;
import saul.exception.ResourceNotFoundException;
import saul.repository.TipoTonerRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoTonerService {

    private final TipoTonerRepository tipoTonerRepository;
    private final EntityMapper mapper;

    public Page<TipoTonerResponse> findAll(Pageable pageable) {
        return tipoTonerRepository.findAll(pageable).map(mapper::toResponse);
    }

    public TipoTonerResponse findById(Integer id) {
        TipoToner tipo = tipoTonerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoToner", "id", id));
        return mapper.toResponse(tipo);
    }

    public Page<TipoTonerResponse> findByNombre(String nombre, Pageable pageable) {
        return tipoTonerRepository.findByNombreTipoTonerContaining(nombre, pageable)
                .map(mapper::toResponse);
    }

    public TipoTonerResponse create(TipoTonerRequest request) {
        TipoToner tipo = mapper.toEntity(request);
        tipo.setBorrado(false);
        return mapper.toResponse(tipoTonerRepository.save(tipo));
    }

    public TipoTonerResponse update(Integer id, TipoTonerRequest request) {
        TipoToner tipo = tipoTonerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoToner", "id", id));
        mapper.updateEntity(tipo, request);
        return mapper.toResponse(tipoTonerRepository.save(tipo));
    }

    public void delete(Integer id) {
        TipoToner tipo = tipoTonerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoToner", "id", id));
        tipo.setBorrado(true);
        tipoTonerRepository.save(tipo);
    }

    public void restore(Integer id) {
        TipoToner tipo = tipoTonerRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoToner eliminado", "id", id));
        tipo.setBorrado(false);
        tipoTonerRepository.save(tipo);
    }
}

