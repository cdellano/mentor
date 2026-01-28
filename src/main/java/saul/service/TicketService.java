package saul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.request.TicketRequest;
import saul.dto.response.TicketResponse;
import saul.entity.Departamento;
import saul.entity.EstadoTicket;
import saul.entity.Ticket;
import saul.entity.TipoPrioridadTicket;
import saul.entity.Usuario;
import saul.exception.ResourceNotFoundException;
import saul.repository.DepartamentoRepository;
import saul.repository.EstadoTicketRepository;
import saul.repository.TicketRepository;
import saul.repository.TipoPrioridadTicketRepository;
import saul.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstadoTicketRepository estadoTicketRepository;
    private final TipoPrioridadTicketRepository tipoPrioridadTicketRepository;
    private final DepartamentoRepository departamentoRepository;
    private final EntityMapper mapper;

    public Page<TicketResponse> findAll(Pageable pageable) {
        return ticketRepository.findAll(pageable).map(mapper::toResponse);
    }

    public TicketResponse findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));
        return mapper.toResponse(ticket);
    }

    public Page<TicketResponse> findByEstado(Integer idEstado, Pageable pageable) {
        return ticketRepository.findByEstadoTicketIdEstado(idEstado, pageable)
                .map(mapper::toResponse);
    }

    public Page<TicketResponse> findByPrioridad(Integer idPrioridad, Pageable pageable) {
        return ticketRepository.findByPrioridadId(idPrioridad, pageable)
                .map(mapper::toResponse);
    }

    public Page<TicketResponse> findByFechaRango(LocalDateTime inicio, LocalDateTime fin, Pageable pageable) {
        return ticketRepository.findByFechaCreacionBetween(inicio, fin, pageable)
                .map(mapper::toResponse);
    }

    public TicketResponse create(TicketRequest request) {
        Ticket ticket = mapper.toEntity(request);

        Usuario creador = usuarioRepository.findById(request.getIdUsuarioCreador())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuarioCreador()));
        ticket.setUsuarioCreador(creador);

        if (request.getIdUsuarioAsignado() != null) {
            Usuario asignado = usuarioRepository.findById(request.getIdUsuarioAsignado())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuarioAsignado()));
            ticket.setUsuarioAsignado(asignado);
        }

        Departamento departamento = departamentoRepository.findById(request.getIdDepartamento())
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", request.getIdDepartamento()));
        ticket.setDepartamento(departamento);

        EstadoTicket estado = estadoTicketRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new ResourceNotFoundException("EstadoTicket", "id", request.getIdEstado()));
        ticket.setEstadoTicket(estado);

        TipoPrioridadTicket prioridad = tipoPrioridadTicketRepository.findById(request.getIdPrioridad())
                .orElseThrow(() -> new ResourceNotFoundException("TipoPrioridadTicket", "id", request.getIdPrioridad()));
        ticket.setPrioridad(prioridad);

        ticket.setFechaCreacion(LocalDateTime.now());
        ticket.setBorrado(false);

        return mapper.toResponse(ticketRepository.save(ticket));
    }

    public TicketResponse update(Long id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));

        mapper.updateEntity(ticket, request);

        if (request.getIdUsuarioAsignado() != null) {
            Usuario asignado = usuarioRepository.findById(request.getIdUsuarioAsignado())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", request.getIdUsuarioAsignado()));
            ticket.setUsuarioAsignado(asignado);
        }

        if (request.getIdDepartamento() != null) {
            Departamento departamento = departamentoRepository.findById(request.getIdDepartamento())
                    .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", request.getIdDepartamento()));
            ticket.setDepartamento(departamento);
        }

        if (request.getIdEstado() != null) {
            EstadoTicket estado = estadoTicketRepository.findById(request.getIdEstado())
                    .orElseThrow(() -> new ResourceNotFoundException("EstadoTicket", "id", request.getIdEstado()));
            ticket.setEstadoTicket(estado);
        }

        if (request.getIdPrioridad() != null) {
            TipoPrioridadTicket prioridad = tipoPrioridadTicketRepository.findById(request.getIdPrioridad())
                    .orElseThrow(() -> new ResourceNotFoundException("TipoPrioridadTicket", "id", request.getIdPrioridad()));
            ticket.setPrioridad(prioridad);
        }

        return mapper.toResponse(ticketRepository.save(ticket));
    }

    public Page<TicketResponse> findByDepartamento(Integer idDepartamento, Pageable pageable) {
        return ticketRepository.findByDepartamentoIdDepartamento(idDepartamento, pageable)
                .map(ticket -> mapper.toResponse(ticket));
    }

    public void delete(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));
        ticket.setBorrado(true);
        ticketRepository.save(ticket);
    }

    public void restore(Long id) {
        Ticket ticket = ticketRepository.findByIdDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket eliminado", "id", id));
        ticket.setBorrado(false);
        ticketRepository.save(ticket);
    }
}

