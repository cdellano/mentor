package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.LugarRequest;
import saul.dto.response.LugarResponse;
import saul.entity.Departamento;
import saul.entity.Lugar;
import saul.exception.ResourceNotFoundException;
import saul.repository.DepartamentoRepository;
import saul.repository.LugarRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LugarService {

    private final LugarRepository lugarRepository;
    private final DepartamentoRepository departamentoRepository;
    private final EntityMapper mapper;

    public Page<LugarResponse> findAll(Pageable pageable) {
        return lugarRepository.findAll(pageable).map(mapper::toResponse);
    }

    public LugarResponse findById(Integer id) {
        Lugar lugar = lugarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lugar", "id", id));
        return mapper.toResponse(lugar);
    }

    public Page<LugarResponse> findByNombre(String nombre, Pageable pageable) {
        return lugarRepository.findByNombreLugarContainingIgnoreCase(nombre, pageable)
                .map(mapper::toResponse);
    }

    public Page<LugarResponse> findByDepartamento(Integer idDepartamento, Pageable pageable) {
        return lugarRepository.findByDepartamentoIdDepartamento(idDepartamento, pageable)
                .map(mapper::toResponse);
    }

    public LugarResponse create(LugarRequest request) {
        Lugar lugar = mapper.toEntity(request);

        if (request.getIdDepartamento() != null) {
            Departamento departamento = departamentoRepository.findById(request.getIdDepartamento())
                    .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", request.getIdDepartamento()));
            lugar.setDepartamento(departamento);
        }

        lugar.setBorrado(false);
        return mapper.toResponse(lugarRepository.save(lugar));
    }

    public LugarResponse update(Integer id, LugarRequest request) {
        Lugar lugar = lugarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lugar", "id", id));

        mapper.updateEntity(lugar, request);

        if (request.getIdDepartamento() != null) {
            Departamento departamento = departamentoRepository.findById(request.getIdDepartamento())
                    .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", request.getIdDepartamento()));
            lugar.setDepartamento(departamento);
        }

        return mapper.toResponse(lugarRepository.save(lugar));
    }

    public void delete(Integer id) {
        Lugar lugar = lugarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lugar", "id", id));
        lugar.setBorrado(true);
        lugarRepository.save(lugar);
    }

    public void restore(Integer id) {
        Lugar lugar = lugarRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lugar eliminado", "id", id));
        lugar.setBorrado(false);
        lugarRepository.save(lugar);
    }
}

