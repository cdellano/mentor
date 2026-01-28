package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.TipoMantenimientoRequest;
import saul.dto.response.TipoMantenimientoResponse;
import saul.entity.TipoMantenimiento;
import saul.exception.ResourceNotFoundException;
import saul.repository.TipoMantenimientoRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoMantenimientoService {

    private final TipoMantenimientoRepository tipoMantenimientoRepository;
    private final EntityMapper mapper;

    public Page<TipoMantenimientoResponse> findAll(Pageable pageable) {
        return tipoMantenimientoRepository.findAll(pageable).map(mapper::toResponse);
    }

    public TipoMantenimientoResponse findById(Integer id) {
        TipoMantenimiento tipo = tipoMantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoMantenimiento", "id", id));
        return mapper.toResponse(tipo);
    }

    public Page<TipoMantenimientoResponse> findByNombre(String nombre, Pageable pageable) {
        return tipoMantenimientoRepository.findByNombreTipoMantenimientoContainingIgnoreCase(nombre, pageable)
                .map(mapper::toResponse);
    }

    public TipoMantenimientoResponse create(TipoMantenimientoRequest request) {
        TipoMantenimiento tipo = mapper.toEntity(request);
        tipo.setBorrado(false);
        return mapper.toResponse(tipoMantenimientoRepository.save(tipo));
    }

    public TipoMantenimientoResponse update(Integer id, TipoMantenimientoRequest request) {
        TipoMantenimiento tipo = tipoMantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoMantenimiento", "id", id));
        mapper.updateEntity(tipo, request);
        return mapper.toResponse(tipoMantenimientoRepository.save(tipo));
    }

    public void delete(Integer id) {
        TipoMantenimiento tipo = tipoMantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoMantenimiento", "id", id));
        tipo.setBorrado(true);
        tipoMantenimientoRepository.save(tipo);
    }

    public void restore(Integer id) {
        TipoMantenimiento tipo = tipoMantenimientoRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoMantenimiento eliminado", "id", id));
        tipo.setBorrado(false);
        tipoMantenimientoRepository.save(tipo);
    }
}

