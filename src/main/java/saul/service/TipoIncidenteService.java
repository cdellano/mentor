package saul.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.TipoIncidenteRequest;
import saul.dto.response.TipoIncidenteResponse;
import saul.entity.TipoIncidente;
import saul.exception.ResourceNotFoundException;
import saul.repository.TipoIncidenteRepository;
@Service
@RequiredArgsConstructor
@Transactional
public class TipoIncidenteService {
    private final TipoIncidenteRepository tipoIncidenteRepository;
    private final EntityMapper mapper;
    public Page<TipoIncidenteResponse> findAll(Pageable pageable) {
        return tipoIncidenteRepository.findAll(pageable).map(mapper::toResponse);
    }
    public TipoIncidenteResponse findById(Long id) {
        TipoIncidente tipoIncidente = tipoIncidenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoIncidente", "id", id));
        return mapper.toResponse(tipoIncidente);
    }
    public TipoIncidenteResponse create(TipoIncidenteRequest request) {
        TipoIncidente tipoIncidente = mapper.toEntity(request);
        tipoIncidente.setBorrado(false);
        return mapper.toResponse(tipoIncidenteRepository.save(tipoIncidente));
    }
    public TipoIncidenteResponse update(Long id, TipoIncidenteRequest request) {
        TipoIncidente tipoIncidente = tipoIncidenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoIncidente", "id", id));
        mapper.updateEntity(tipoIncidente, request);
        return mapper.toResponse(tipoIncidenteRepository.save(tipoIncidente));
    }
    public void delete(Long id) {
        TipoIncidente tipoIncidente = tipoIncidenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoIncidente", "id", id));
        tipoIncidente.setBorrado(true);
        tipoIncidenteRepository.save(tipoIncidente);
    }
    public void restore(Long id) {
        tipoIncidenteRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoIncidente (eliminado)", "id", id));
        tipoIncidenteRepository.restore(id);
    }
}
