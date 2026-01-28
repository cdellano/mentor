package saul.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.ServicioRequest;
import saul.dto.response.ServicioResponse;
import saul.entity.Servicio;
import saul.exception.ResourceNotFoundException;
import saul.repository.ServicioRepository;
import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
@Transactional
public class ServicioService {
    private final ServicioRepository servicioRepository;
    private final EntityMapper mapper;
    public Page<ServicioResponse> findAll(Pageable pageable) {
        return servicioRepository.findAll(pageable).map(mapper::toResponse);
    }
    public ServicioResponse findById(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", id));
        return mapper.toResponse(servicio);
    }
    public Page<ServicioResponse> findByNombre(String nombre, Pageable pageable) {
        return servicioRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                .map(mapper::toResponse);
    }
    public Page<ServicioResponse> findByActivo(Boolean activo, Pageable pageable) {
        return servicioRepository.findByActivo(activo, pageable)
                .map(mapper::toResponse);
    }
    public ServicioResponse create(ServicioRequest request) {
        Servicio servicio = mapper.toEntity(request);
        servicio.setCreatedAt(LocalDateTime.now());
        servicio.setBorrado(false);
        return mapper.toResponse(servicioRepository.save(servicio));
    }
    public ServicioResponse update(Long id, ServicioRequest request) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", id));
        mapper.updateEntity(servicio, request);
        return mapper.toResponse(servicioRepository.save(servicio));
    }
    public void delete(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", id));
        servicio.setBorrado(true);
        servicioRepository.save(servicio);
    }
    public void restore(Long id) {
        servicioRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio (eliminado)", "id", id));
        servicioRepository.restore(id);
    }
}
