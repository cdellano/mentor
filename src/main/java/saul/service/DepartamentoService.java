package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.DepartamentoRequest;
import saul.dto.response.DepartamentoResponse;
import saul.entity.Departamento;
import saul.exception.ResourceNotFoundException;
import saul.repository.DepartamentoRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final EntityMapper mapper;

    public Page<DepartamentoResponse> findAll(Pageable pageable) {
        return departamentoRepository.findAll(pageable).map(mapper::toResponse);
    }

    public DepartamentoResponse findById(Integer id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", id));
        return mapper.toResponse(departamento);
    }

    public Page<DepartamentoResponse> findByNombre(String nombre, Pageable pageable) {
        return departamentoRepository.findByNombreDepartamentoContainingIgnoreCase(nombre, pageable)
                .map(mapper::toResponse);
    }

    public DepartamentoResponse create(DepartamentoRequest request) {
        Departamento departamento = mapper.toEntity(request);
        departamento.setBorrado(false);
        return mapper.toResponse(departamentoRepository.save(departamento));
    }

    public DepartamentoResponse update(Integer id, DepartamentoRequest request) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", id));
        mapper.updateEntity(departamento, request);
        return mapper.toResponse(departamentoRepository.save(departamento));
    }

    public void delete(Integer id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", id));
        departamento.setBorrado(true);
        departamentoRepository.save(departamento);
    }

    public void restore(Integer id) {
        Departamento departamento = departamentoRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento eliminado", "id", id));
        departamento.setBorrado(false);
        departamentoRepository.save(departamento);
    }
}

