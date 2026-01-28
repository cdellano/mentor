package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.TipoDispositivoRequest;
import saul.dto.response.TipoDispositivoResponse;
import saul.entity.TipoDispositivo;
import saul.exception.ResourceNotFoundException;
import saul.repository.TipoDispositivoRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoDispositivoService {

    private final TipoDispositivoRepository tipoDispositivoRepository;
    private final EntityMapper mapper;

    public Page<TipoDispositivoResponse> findAll(Pageable pageable) {
        return tipoDispositivoRepository.findAll(pageable).map(mapper::toResponse);
    }

    public TipoDispositivoResponse findById(Integer id) {
        TipoDispositivo tipo = tipoDispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDispositivo", "id", id));
        return mapper.toResponse(tipo);
    }

    public Page<TipoDispositivoResponse> findByNombre(String nombre, Pageable pageable) {
        return tipoDispositivoRepository.findByNombreTipoContainingIgnoreCase(nombre, pageable)
                .map(mapper::toResponse);
    }

    public TipoDispositivoResponse create(TipoDispositivoRequest request) {
        TipoDispositivo tipo = mapper.toEntity(request);
        tipo.setBorrado(false);
        return mapper.toResponse(tipoDispositivoRepository.save(tipo));
    }

    public TipoDispositivoResponse update(Integer id, TipoDispositivoRequest request) {
        TipoDispositivo tipo = tipoDispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDispositivo", "id", id));
        mapper.updateEntity(tipo, request);
        return mapper.toResponse(tipoDispositivoRepository.save(tipo));
    }

    public void delete(Integer id) {
        TipoDispositivo tipo = tipoDispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDispositivo", "id", id));
        tipo.setBorrado(true);
        tipoDispositivoRepository.save(tipo);
    }

    public void restore(Integer id) {
        TipoDispositivo tipo = tipoDispositivoRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDispositivo eliminado", "id", id));
        tipo.setBorrado(false);
        tipoDispositivoRepository.save(tipo);
    }
}

