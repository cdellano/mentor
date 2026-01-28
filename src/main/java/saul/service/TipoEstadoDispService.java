package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.TipoEstadoDispRequest;
import saul.dto.response.TipoEstadoDispResponse;
import saul.entity.TipoEstadoDisp;
import saul.exception.ResourceNotFoundException;
import saul.repository.TipoEstadoDispRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoEstadoDispService {

    private final TipoEstadoDispRepository tipoEstadoDispRepository;
    private final EntityMapper mapper;

    public Page<TipoEstadoDispResponse> findAll(Pageable pageable) {
        return tipoEstadoDispRepository.findAll(pageable).map(mapper::toResponse);
    }

    public TipoEstadoDispResponse findById(Integer id) {
        TipoEstadoDisp tipo = tipoEstadoDispRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoEstadoDisp", "id", id));
        return mapper.toResponse(tipo);
    }

    public Page<TipoEstadoDispResponse> findByNombre(String nombre, Pageable pageable) {
        return tipoEstadoDispRepository.findByNombreEstadoContainingIgnoreCase(nombre, pageable)
                .map(mapper::toResponse);
    }

    public TipoEstadoDispResponse create(TipoEstadoDispRequest request) {
        TipoEstadoDisp tipo = mapper.toEntity(request);
        tipo.setBorrado(false);
        return mapper.toResponse(tipoEstadoDispRepository.save(tipo));
    }

    public TipoEstadoDispResponse update(Integer id, TipoEstadoDispRequest request) {
        TipoEstadoDisp tipo = tipoEstadoDispRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoEstadoDisp", "id", id));
        mapper.updateEntity(tipo, request);
        return mapper.toResponse(tipoEstadoDispRepository.save(tipo));
    }

    public void delete(Integer id) {
        TipoEstadoDisp tipo = tipoEstadoDispRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoEstadoDisp", "id", id));
        tipo.setBorrado(true);
        tipoEstadoDispRepository.save(tipo);
    }

    public void restore(Integer id) {
        TipoEstadoDisp tipo = tipoEstadoDispRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoEstadoDisp eliminado", "id", id));
        tipo.setBorrado(false);
        tipoEstadoDispRepository.save(tipo);
    }
}

