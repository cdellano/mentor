package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.TipoPrioridadTicketRequest;
import saul.dto.response.TipoPrioridadTicketResponse;
import saul.entity.TipoPrioridadTicket;
import saul.exception.ResourceNotFoundException;
import saul.repository.TipoPrioridadTicketRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoPrioridadTicketService {

    private final TipoPrioridadTicketRepository tipoPrioridadTicketRepository;
    private final EntityMapper mapper;

    public Page<TipoPrioridadTicketResponse> findAll(Pageable pageable) {
        return tipoPrioridadTicketRepository.findAll(pageable).map(mapper::toResponse);
    }

    public TipoPrioridadTicketResponse findById(Integer id) {
        TipoPrioridadTicket tipo = tipoPrioridadTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoPrioridadTicket", "id", id));
        return mapper.toResponse(tipo);
    }

    public Page<TipoPrioridadTicketResponse> findByNombre(String nombre, Pageable pageable) {
        return tipoPrioridadTicketRepository.findByNombrePrioridadContainingIgnoreCase(nombre, pageable)
                .map(mapper::toResponse);
    }

    public TipoPrioridadTicketResponse create(TipoPrioridadTicketRequest request) {
        TipoPrioridadTicket tipo = mapper.toEntity(request);
        tipo.setBorrado(false);
        return mapper.toResponse(tipoPrioridadTicketRepository.save(tipo));
    }

    public TipoPrioridadTicketResponse update(Integer id, TipoPrioridadTicketRequest request) {
        TipoPrioridadTicket tipo = tipoPrioridadTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoPrioridadTicket", "id", id));
        mapper.updateEntity(tipo, request);
        return mapper.toResponse(tipoPrioridadTicketRepository.save(tipo));
    }

    public void delete(Integer id) {
        TipoPrioridadTicket tipo = tipoPrioridadTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoPrioridadTicket", "id", id));
        tipo.setBorrado(true);
        tipoPrioridadTicketRepository.save(tipo);
    }

    public void restore(Integer id) {
        TipoPrioridadTicket tipo = tipoPrioridadTicketRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoPrioridadTicket eliminado", "id", id));
        tipo.setBorrado(false);
        tipoPrioridadTicketRepository.save(tipo);
    }
}

