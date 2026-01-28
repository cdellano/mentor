package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.MantenimientoRequest;
import saul.dto.response.MantenimientoResponse;
import saul.entity.Dispositivo;
import saul.entity.Mantenimiento;
import saul.entity.TipoMantenimiento;
import saul.entity.Usuario;
import saul.exception.ResourceNotFoundException;
import saul.repository.DispositivoRepository;
import saul.repository.MantenimientoRepository;
import saul.repository.TipoMantenimientoRepository;
import saul.repository.UsuarioRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final DispositivoRepository dispositivoRepository;
    private final TipoMantenimientoRepository tipoMantenimientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntityMapper mapper;

    public Page<MantenimientoResponse> findAll(Pageable pageable) {
        return mantenimientoRepository.findAll(pageable).map(mapper::toResponse);
    }

    public MantenimientoResponse findById(Long id) {
        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento", "id", id));
        return mapper.toResponse(mantenimiento);
    }

    public Page<MantenimientoResponse> findByDispositivo(Long idDispositivo, Pageable pageable) {
        return mantenimientoRepository.findByDispositivoIdDispositivo(idDispositivo, pageable)
                .map(mapper::toResponse);
    }

    public Page<MantenimientoResponse> findByTipoMantenimiento(Integer idTipoMantenimiento, Pageable pageable) {
        return mantenimientoRepository.findByTipoMantenimientoId(idTipoMantenimiento, pageable)
                .map(mapper::toResponse);
    }

    public Page<MantenimientoResponse> findByFechaRango(LocalDate inicio, LocalDate fin, Pageable pageable) {
        return mantenimientoRepository.findByFechaProgramadaBetween(inicio, fin, pageable)
                .map(mapper::toResponse);
    }

    public MantenimientoResponse create(MantenimientoRequest request) {
        Mantenimiento mantenimiento = mapper.toEntity(request);

        Dispositivo dispositivo = dispositivoRepository.findById(request.getIdDispositivo())
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", "id", request.getIdDispositivo()));
        mantenimiento.setDispositivo(dispositivo);

        TipoMantenimiento tipo = tipoMantenimientoRepository.findById(request.getIdTipoMantenimiento())
                .orElseThrow(() -> new ResourceNotFoundException("TipoMantenimiento", "id", request.getIdTipoMantenimiento()));
        mantenimiento.setTipoMantenimiento(tipo);

        if (request.getIdUsuarioSolicita() != null) {
            Usuario solicita = usuarioRepository.findById(request.getIdUsuarioSolicita())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuarioSolicita()));
            mantenimiento.setUsuarioSolicita(solicita);
        }

        if (request.getIdUsuarioAtiende() != null) {
            Usuario atiende = usuarioRepository.findById(request.getIdUsuarioAtiende())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuarioAtiende()));
            mantenimiento.setUsuarioAtiende(atiende);
        }

        mantenimiento.setBorrado(false);
        return mapper.toResponse(mantenimientoRepository.save(mantenimiento));
    }

    public MantenimientoResponse update(Long id, MantenimientoRequest request) {
        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento", "id", id));

        mapper.updateEntity(mantenimiento, request);

        if (request.getIdTipoMantenimiento() != null) {
            TipoMantenimiento tipo = tipoMantenimientoRepository.findById(request.getIdTipoMantenimiento())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoMantenimiento", "id", request.getIdTipoMantenimiento()));
            mantenimiento.setTipoMantenimiento(tipo);
        }

        if (request.getIdUsuarioAtiende() != null) {
            Usuario atiende = usuarioRepository.findById(request.getIdUsuarioAtiende())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuarioAtiende()));
            mantenimiento.setUsuarioAtiende(atiende);
        }

        return mapper.toResponse(mantenimientoRepository.save(mantenimiento));
    }

    public void delete(Long id) {
        Mantenimiento mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento", "id", id));
        mantenimiento.setBorrado(true);
        mantenimientoRepository.save(mantenimiento);
    }

    public void restore(Long id) {
        Mantenimiento mantenimiento = mantenimientoRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mantenimiento eliminado", "id", id));
        mantenimiento.setBorrado(false);
        mantenimientoRepository.save(mantenimiento);
    }
}

