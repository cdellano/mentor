package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.EstadoTicketRequest;
import saul.dto.response.EstadoTicketResponse;
import saul.entity.EstadoTicket;
import saul.exception.ResourceNotFoundException;
import saul.repository.EstadoTicketRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class EstadoTicketService {

    private final EstadoTicketRepository estadoTicketRepository;
    private final EntityMapper mapper;

    public Page<EstadoTicketResponse> findAll(Pageable pageable) {
        return estadoTicketRepository.findAll(pageable).map(mapper::toResponse);
    }

    public EstadoTicketResponse findById(Integer id) {
        EstadoTicket estado = estadoTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoTicket", "id", id));
        return mapper.toResponse(estado);
    }

    public Page<EstadoTicketResponse> findByNombre(String nombre, Pageable pageable) {
        return estadoTicketRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                .map(mapper::toResponse);
    }

    public EstadoTicketResponse create(EstadoTicketRequest request) {
        EstadoTicket estado = mapper.toEntity(request);
        estado.setBorrado(false);
        return mapper.toResponse(estadoTicketRepository.save(estado));
    }

    public EstadoTicketResponse update(Integer id, EstadoTicketRequest request) {
        EstadoTicket estado = estadoTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoTicket", "id", id));
        mapper.updateEntity(estado, request);
        return mapper.toResponse(estadoTicketRepository.save(estado));
    }

    public void delete(Integer id) {
        EstadoTicket estado = estadoTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoTicket", "id", id));
        estado.setBorrado(true);
        estadoTicketRepository.save(estado);
    }

    public void restore(Integer id) {
        EstadoTicket estado = estadoTicketRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoTicket eliminado", "id", id));
        estado.setBorrado(false);
        estadoTicketRepository.save(estado);
    }
}

