package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.DispositivoRequest;
import saul.dto.response.DispositivoResponse;
import saul.entity.Dispositivo;
import saul.entity.TipoDispositivo;
import saul.entity.TipoEstadoDisp;
import saul.exception.ResourceNotFoundException;
import saul.repository.DispositivoRepository;
import saul.repository.TipoDispositivoRepository;
import saul.repository.TipoEstadoDispRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DispositivoService {

    private final DispositivoRepository dispositivoRepository;
    private final TipoDispositivoRepository tipoDispositivoRepository;
    private final TipoEstadoDispRepository tipoEstadoDispRepository;
    private final EntityMapper mapper;

    public Page<DispositivoResponse> findAll(Pageable pageable) {
        return dispositivoRepository.findAll(pageable).map(mapper::toResponse);
    }

    public DispositivoResponse findById(Long id) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", "id", id));
        return mapper.toResponse(dispositivo);
    }

    public Page<DispositivoResponse> findByTipoEstado(Integer idTipoEstado, Pageable pageable) {
        return dispositivoRepository.findByTipoEstadoIdTipoEstado(idTipoEstado, pageable)
                .map(mapper::toResponse);
    }

    public DispositivoResponse findByNumeroSerie(String numeroSerie) {
        Dispositivo dispositivo = dispositivoRepository.findByNumeroSerie(numeroSerie)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", "numeroSerie", numeroSerie));
        return mapper.toResponse(dispositivo);
    }

    public Page<DispositivoResponse> findByTipoDispositivo(Integer idTipoDispositivo, Pageable pageable) {
        return dispositivoRepository.findByTipoDispositivoIdTipoDispositivo(idTipoDispositivo, pageable)
                .map(mapper::toResponse);
    }

    public Page<DispositivoResponse> findByModelo(String modelo, Pageable pageable) {
        return dispositivoRepository.findByModeloContainingIgnoreCase(modelo, pageable)
                .map(mapper::toResponse);
    }

    public Page<DispositivoResponse> findByCurrentLugar(Integer idLugar, Pageable pageable) {
        return dispositivoRepository.findByCurrentLugar(idLugar, pageable)
                .map(mapper::toResponse);
    }

    public Page<DispositivoResponse> findByCurrentDepartamento(Integer idDepartamento, Pageable pageable) {
        return dispositivoRepository.findByCurrentDepartamento(idDepartamento, pageable)
                .map(mapper::toResponse);
    }

    public Page<DispositivoResponse> findAllActiveWithCurrentLocation(Pageable pageable) {
        return dispositivoRepository.findAllActiveWithCurrentLocation(pageable)
                .map(mapper::toResponse);
    }

    /**
     * Busca un dispositivo aleatorio que no ha recibido mantenimiento en el número especificado de días,
     * filtrado por tipo de dispositivo.
     * @param diasMinimos número mínimo de días sin recibir mantenimiento
     * @param idTipoDispositivo ID del tipo de dispositivo a filtrar
     * @return Un dispositivo aleatorio que cumple con los criterios, o null si no existe ninguno
     * @throws ResourceNotFoundException si no se encuentra ningún dispositivo que cumpla los criterios
     */
    public DispositivoResponse findRandomEquipoRezagadoEnMantenimiento(Integer diasMinimos, Integer idTipoDispositivo) {
        Optional<Dispositivo> dispositivo = dispositivoRepository
                .findRandomEquipoRezagadoEnMantenimiento(diasMinimos, idTipoDispositivo);

        if (dispositivo.isEmpty()) {
            throw new ResourceNotFoundException("Dispositivo",
                    "criterio",
                    String.format("tipo=%d, días sin mantenimiento>%d", idTipoDispositivo, diasMinimos));
        }

        return mapper.toResponse(dispositivo.get());
    }

    public DispositivoResponse create(DispositivoRequest request) {
        Dispositivo dispositivo = mapper.toEntity(request);

        TipoDispositivo tipo = tipoDispositivoRepository.findById(request.getIdTipoDispositivo())
                .orElseThrow(() -> new ResourceNotFoundException("TipoDispositivo", "id", request.getIdTipoDispositivo()));
        dispositivo.setTipoDispositivo(tipo);

        TipoEstadoDisp estado = tipoEstadoDispRepository.findById(request.getIdTipoEstado())
                .orElseThrow(() -> new ResourceNotFoundException("TipoEstadoDisp", "id", request.getIdTipoEstado()));
        dispositivo.setTipoEstado(estado);

        dispositivo.setBorrado(false);
        return mapper.toResponse(dispositivoRepository.save(dispositivo));
    }

    public DispositivoResponse update(Long id, DispositivoRequest request) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", "id", id));

        mapper.updateEntity(dispositivo, request);

        if (request.getIdTipoDispositivo() != null) {
            TipoDispositivo tipo = tipoDispositivoRepository.findById(request.getIdTipoDispositivo())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoDispositivo", "id", request.getIdTipoDispositivo()));
            dispositivo.setTipoDispositivo(tipo);
        }

        if (request.getIdTipoEstado() != null) {
            TipoEstadoDisp estado = tipoEstadoDispRepository.findById(request.getIdTipoEstado())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoEstadoDisp", "id", request.getIdTipoEstado()));
            dispositivo.setTipoEstado(estado);
        }

        return mapper.toResponse(dispositivoRepository.save(dispositivo));
    }

    public void delete(Long id) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", "id", id));
        dispositivo.setBorrado(true);
        dispositivoRepository.save(dispositivo);
    }

    public void restore(Long id) {
        Dispositivo dispositivo = dispositivoRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo eliminado", "id", id));
        dispositivo.setBorrado(false);
        dispositivoRepository.save(dispositivo);
    }
}
