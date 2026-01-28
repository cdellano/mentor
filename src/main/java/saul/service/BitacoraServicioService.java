package saul.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.BitacoraServicioRequest;
import saul.dto.response.BitacoraServicioResponse;
import saul.entity.BitacoraServicio;
import saul.entity.EstadoBitacora;
import saul.entity.Servicio;
import saul.entity.TipoIncidente;
import saul.entity.Usuario;
import saul.exception.ResourceNotFoundException;
import saul.repository.BitacoraServicioRepository;
import saul.repository.ServicioRepository;
import saul.repository.TipoIncidenteRepository;
import saul.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class BitacoraServicioService {
    private final BitacoraServicioRepository bitacoraServicioRepository;
    private final ServicioRepository servicioRepository;
    private final TipoIncidenteRepository tipoIncidenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntityMapper mapper;
    public Page<BitacoraServicioResponse> findAll(Pageable pageable) {
        return bitacoraServicioRepository.findAll(pageable).map(mapper::toResponse);
    }
    public BitacoraServicioResponse findById(Long id) {
        BitacoraServicio bitacora = bitacoraServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BitacoraServicio", "id", id));
        return mapper.toResponse(bitacora);
    }
    public Page<BitacoraServicioResponse> findByServicio(Long servicioId, Pageable pageable) {
        return bitacoraServicioRepository.findByServicioId(servicioId, pageable)
                .map(mapper::toResponse);
    }
    public Page<BitacoraServicioResponse> findByEstado(String estado, Pageable pageable) {
        EstadoBitacora estadoBitacora = EstadoBitacora.valueOf(estado);
        return bitacoraServicioRepository.findByEstado(estadoBitacora, pageable)
                .map(mapper::toResponse);
    }
    public Page<BitacoraServicioResponse> findByFechaRango(LocalDateTime inicio, LocalDateTime fin, Pageable pageable) {
        return bitacoraServicioRepository.findByFechaInicioBetween(inicio, fin, pageable)
                .map(mapper::toResponse);
    }
    public Page<BitacoraServicioResponse> findByReportadoPor(Long idUsuario, Pageable pageable) {
        return bitacoraServicioRepository.findByReportadoPorIdUsuario(idUsuario, pageable)
                .map(mapper::toResponse);
    }
    public Page<BitacoraServicioResponse> findByTipoIncidente(Long tipoIncidenteId, Pageable pageable) {
        return bitacoraServicioRepository.findByTipoIncidenteId(tipoIncidenteId, pageable)
                .map(mapper::toResponse);
    }
    public BitacoraServicioResponse create(BitacoraServicioRequest request) {
        BitacoraServicio bitacora = mapper.toEntity(request);
        Servicio servicio = servicioRepository.findById(request.getServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", request.getServicioId()));
        bitacora.setServicio(servicio);
        TipoIncidente tipoIncidente = tipoIncidenteRepository.findById(request.getTipoIncidenteId())
                .orElseThrow(() -> new ResourceNotFoundException("TipoIncidente", "id", request.getTipoIncidenteId()));
        bitacora.setTipoIncidente(tipoIncidente);
        Usuario reportadoPor = usuarioRepository.findById(request.getReportadoPorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getReportadoPorId()));
        bitacora.setReportadoPor(reportadoPor);
        bitacora.setCreatedAt(LocalDateTime.now());
        bitacora.setBorrado(false);
        return mapper.toResponse(bitacoraServicioRepository.save(bitacora));
    }
    public BitacoraServicioResponse update(Long id, BitacoraServicioRequest request) {
        BitacoraServicio bitacora = bitacoraServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BitacoraServicio", "id", id));
        mapper.updateEntity(bitacora, request);
        if (request.getServicioId() != null) {
            Servicio servicio = servicioRepository.findById(request.getServicioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", request.getServicioId()));
            bitacora.setServicio(servicio);
        }
        if (request.getTipoIncidenteId() != null) {
            TipoIncidente tipoIncidente = tipoIncidenteRepository.findById(request.getTipoIncidenteId())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoIncidente", "id", request.getTipoIncidenteId()));
            bitacora.setTipoIncidente(tipoIncidente);
        }
        if (request.getReportadoPorId() != null) {
            Usuario reportadoPor = usuarioRepository.findById(request.getReportadoPorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getReportadoPorId()));
            bitacora.setReportadoPor(reportadoPor);
        }
        return mapper.toResponse(bitacoraServicioRepository.save(bitacora));
    }
    public void delete(Long id) {
        BitacoraServicio bitacora = bitacoraServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BitacoraServicio", "id", id));
        bitacora.setBorrado(true);
        bitacoraServicioRepository.save(bitacora);
    }
    public void restore(Long id) {
        bitacoraServicioRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("BitacoraServicio (eliminado)", "id", id));
        bitacoraServicioRepository.restore(id);
    }

    public List<BitacoraServicio> findForReport(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Long idUsuario,
            String comentario,
            EstadoBitacora estado,
            Long idTipoIncidente
    ) {
        return bitacoraServicioRepository.findForReport(
                fechaInicio,
                fechaFin,
                idUsuario,
                comentario,
                estado != null ? estado.name() : null,
                idTipoIncidente
        );
    }
}
