package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.HistorialUbicacionRequest;
import saul.dto.response.HistorialUbicacionResponse;
import saul.entity.Dispositivo;
import saul.entity.HistorialUbicacion;
import saul.entity.Lugar;
import saul.entity.Usuario;
import saul.exception.ResourceNotFoundException;
import saul.repository.DispositivoRepository;
import saul.repository.HistorialUbicacionRepository;
import saul.repository.LugarRepository;
import saul.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class HistorialUbicacionService {

    private final HistorialUbicacionRepository historialUbicacionRepository;
    private final DispositivoRepository dispositivoRepository;
    private final LugarRepository lugarRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntityMapper mapper;

    public Page<HistorialUbicacionResponse> findAll(Pageable pageable) {
        return historialUbicacionRepository.findAll(pageable).map(mapper::toResponse);
    }

    public HistorialUbicacionResponse findById(Long id) {
        HistorialUbicacion historial = historialUbicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistorialUbicacion", "id", id));
        return mapper.toResponse(historial);
    }

    public Page<HistorialUbicacionResponse> findByDispositivo(Long idDispositivo, Pageable pageable) {
        return historialUbicacionRepository.findByDispositivoIdDispositivo(idDispositivo, pageable)
                .map(mapper::toResponse);
    }

    public Page<HistorialUbicacionResponse> findByLugar(Integer idLugar, Pageable pageable) {
        return historialUbicacionRepository.findByLugarIdLugar(idLugar, pageable)
                .map(mapper::toResponse);
    }

    public Page<HistorialUbicacionResponse> findByFechaRango(LocalDateTime inicio, LocalDateTime fin, Pageable pageable) {
        return historialUbicacionRepository.findByFechaEntradaBetween(inicio, fin, pageable)
                .map(mapper::toResponse);
    }

    public HistorialUbicacionResponse findCurrentByDispositivo(Long idDispositivo) {
        HistorialUbicacion historial = historialUbicacionRepository.findCurrentByDispositivo(idDispositivo)
                .orElseThrow(() -> new ResourceNotFoundException("HistorialUbicacion actual", "idDispositivo", idDispositivo));
        return mapper.toResponse(historial);
    }

    public HistorialUbicacionResponse create(HistorialUbicacionRequest request) {
        HistorialUbicacion historial = mapper.toEntity(request);

        Dispositivo dispositivo = dispositivoRepository.findById(request.getIdDispositivo())
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", "id", request.getIdDispositivo()));
        historial.setDispositivo(dispositivo);

        Lugar lugar = lugarRepository.findById(request.getIdLugar())
                .orElseThrow(() -> new ResourceNotFoundException("Lugar", "id", request.getIdLugar()));
        historial.setLugar(lugar);

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuario()));
        historial.setUsuario(usuario);

        // Cerrar ubicación anterior si existe
        historialUbicacionRepository.findCurrentByDispositivo(request.getIdDispositivo())
                .ifPresent(current -> {
                    current.setFechaSalida(LocalDateTime.now());
                    historialUbicacionRepository.save(current);
                });

        historial.setFechaEntrada(request.getFechaEntrada() != null ? request.getFechaEntrada() : LocalDateTime.now());
        historial.setBorrado(false);

        return mapper.toResponse(historialUbicacionRepository.save(historial));
    }

    public HistorialUbicacionResponse update(Long id, HistorialUbicacionRequest request) {
        HistorialUbicacion historial = historialUbicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistorialUbicacion", "id", id));

        mapper.updateEntity(historial, request);

        if (request.getIdLugar() != null) {
            Lugar lugar = lugarRepository.findById(request.getIdLugar())
                    .orElseThrow(() -> new ResourceNotFoundException("Lugar", "id", request.getIdLugar()));
            historial.setLugar(lugar);
        }

        return mapper.toResponse(historialUbicacionRepository.save(historial));
    }

    public void delete(Long id) {
        HistorialUbicacion historial = historialUbicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistorialUbicacion", "id", id));
        historial.setBorrado(true);
        historialUbicacionRepository.save(historial);
    }

    public void restore(Long id) {
        HistorialUbicacion historial = historialUbicacionRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistorialUbicacion eliminado", "id", id));
        historial.setBorrado(false);
        historialUbicacionRepository.save(historial);
    }
}

