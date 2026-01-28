package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.SalidasTonerRequest;
import saul.dto.response.SalidasTonerResponse;
import saul.entity.Lugar;
import saul.entity.SalidasToner;
import saul.entity.TipoToner;
import saul.entity.Usuario;
import saul.exception.ResourceNotFoundException;
import saul.repository.LugarRepository;
import saul.repository.SalidasTonerRepository;
import saul.repository.TipoTonerRepository;
import saul.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class SalidasTonerService {

    private final SalidasTonerRepository salidasTonerRepository;
    private final TipoTonerRepository tipoTonerRepository;
    private final UsuarioRepository usuarioRepository;
    private final LugarRepository lugarRepository;
    private final EntityMapper mapper;

    public Page<SalidasTonerResponse> findAll(Pageable pageable) {
        return salidasTonerRepository.findAll(pageable).map(mapper::toResponse);
    }

    public SalidasTonerResponse findById(Integer id) {
        SalidasToner salida = salidasTonerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalidasToner", "id", id));
        return mapper.toResponse(salida);
    }

    public Page<SalidasTonerResponse> findByTipoToner(Integer idTipoToner, Pageable pageable) {
        return salidasTonerRepository.findByTipoTonerId(idTipoToner, pageable)
                .map(mapper::toResponse);
    }

    public Page<SalidasTonerResponse> findByUsuario(Long idUsuario, Pageable pageable) {
        return salidasTonerRepository.findByUsuarioInstalaIdUsuario(idUsuario, pageable)
                .map(mapper::toResponse);
    }

    public Page<SalidasTonerResponse> findByFechaRango(LocalDateTime inicio, LocalDateTime fin, Pageable pageable) {
        return salidasTonerRepository.findByFechaSalidaBetween(inicio, fin, pageable)
                .map(mapper::toResponse);
    }

    public SalidasTonerResponse create(SalidasTonerRequest request) {
        SalidasToner salida = mapper.toEntity(request);

        Usuario instala = usuarioRepository.findById(request.getIdUsuarioInstala())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuarioInstala()));
        salida.setUsuarioInstala(instala);

        Usuario recibe = usuarioRepository.findById(request.getIdUsuarioRecibe())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuarioRecibe()));
        salida.setUsuarioRecibe(recibe);

        TipoToner tipoToner = tipoTonerRepository.findById(request.getIdTipoToner())
                .orElseThrow(() -> new ResourceNotFoundException("TipoToner", "id", request.getIdTipoToner()));
        salida.setTipoToner(tipoToner);

        Lugar lugar = lugarRepository.findById(request.getIdDepartamento())
                .orElseThrow(() -> new ResourceNotFoundException("Lugar", "id", request.getIdDepartamento()));
        salida.setDepartamento(lugar);

        salida.setFechaSalida(LocalDateTime.now());
        salida.setBorrado(false);

        return mapper.toResponse(salidasTonerRepository.save(salida));
    }

    public SalidasTonerResponse update(Integer id, SalidasTonerRequest request) {
        SalidasToner salida = salidasTonerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalidasToner", "id", id));

        mapper.updateEntity(salida, request);

        if (request.getIdTipoToner() != null) {
            TipoToner tipoToner = tipoTonerRepository.findById(request.getIdTipoToner())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoToner", "id", request.getIdTipoToner()));
            salida.setTipoToner(tipoToner);
        }

        if (request.getIdDepartamento() != null) {
            Lugar lugar = lugarRepository.findById(request.getIdDepartamento())
                    .orElseThrow(() -> new ResourceNotFoundException("Lugar", "id", request.getIdDepartamento()));
            salida.setDepartamento(lugar);
        }

        return mapper.toResponse(salidasTonerRepository.save(salida));
    }

    public void delete(Integer id) {
        SalidasToner salida = salidasTonerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalidasToner", "id", id));
        salida.setBorrado(true);
        salidasTonerRepository.save(salida);
    }

    public void restore(Integer id) {
        SalidasToner salida = salidasTonerRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalidasToner eliminada", "id", id));
        salida.setBorrado(false);
        salidasTonerRepository.save(salida);
    }
}

