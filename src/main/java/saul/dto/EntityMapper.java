package saul.dto;

import org.springframework.stereotype.Component;
import saul.dto.request.*;
import saul.dto.response.*;
import saul.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades y DTOs.
 * Utiliza métodos estáticos de los builders generados por Lombok.
 */
@Component
public class EntityMapper {

    // ==================== DEPARTAMENTO ====================

    public DepartamentoResponse toResponse(Departamento entity) {
        if (entity == null) return null;
        DepartamentoResponse response = new DepartamentoResponse();
        response.setIdDepartamento(entity.getIdDepartamento());
        response.setNombreDepartamento(entity.getNombreDepartamento());
        response.setCodigo(entity.getCodigo());
        response.setDescripcion(entity.getDescripcion());
        response.setActivo(entity.getActivo());
        response.setFechaRegistro(entity.getFechaRegistro());
        return response;
    }

    public Departamento toEntity(DepartamentoRequest request) {
        if (request == null) return null;
        Departamento entity = new Departamento();
        entity.setNombreDepartamento(request.getNombreDepartamento());
        entity.setCodigo(request.getCodigo());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo() != null ? request.getActivo() : true);
        return entity;
    }

    public void updateEntity(Departamento entity, DepartamentoRequest request) {
        if (entity == null || request == null) return;
        entity.setNombreDepartamento(request.getNombreDepartamento());
        entity.setCodigo(request.getCodigo());
        entity.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) {
            entity.setActivo(request.getActivo());
        }
    }

    // ==================== ESTADO TICKET ====================

    public EstadoTicketResponse toResponse(EstadoTicket entity) {
        if (entity == null) return null;
        EstadoTicketResponse response = new EstadoTicketResponse();
        response.setIdEstado(entity.getIdEstado());
        response.setNombre(entity.getNombre());
        response.setDescripcion(entity.getDescripcion());
        response.setColor(entity.getColor());
        response.setActivo(entity.getActivo());
        response.setOrden(entity.getOrden());
        return response;
    }

    public EstadoTicket toEntity(EstadoTicketRequest request) {
        if (request == null) return null;
        EstadoTicket entity = new EstadoTicket();
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setColor(request.getColor());
        entity.setActivo(request.getActivo() != null ? request.getActivo() : true);
        entity.setOrden(request.getOrden());
        return entity;
    }

    public void updateEntity(EstadoTicket entity, EstadoTicketRequest request) {
        if (entity == null || request == null) return;
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setColor(request.getColor());
        if (request.getActivo() != null) {
            entity.setActivo(request.getActivo());
        }
        entity.setOrden(request.getOrden());
    }

    // ==================== LUGAR ====================

    public LugarResponse toResponse(Lugar entity) {
        if (entity == null) return null;
        LugarResponse response = new LugarResponse();
        response.setIdLugar(entity.getIdLugar());
        response.setNombreLugar(entity.getNombreLugar());
        response.setDepartamento(toResponse(entity.getDepartamento()));
        response.setPiso(entity.getPiso());
        response.setEdificio(entity.getEdificio());
        response.setDescripcion(entity.getDescripcion());
        response.setFechaRegistro(entity.getFechaRegistro());
        return response;
    }

    public Lugar toEntity(LugarRequest request) {
        if (request == null) return null;
        Lugar entity = new Lugar();
        entity.setNombreLugar(request.getNombreLugar());
        entity.setPiso(request.getPiso());
        entity.setEdificio(request.getEdificio());
        entity.setDescripcion(request.getDescripcion());
        return entity;
    }

    public void updateEntity(Lugar entity, LugarRequest request) {
        if (entity == null || request == null) return;
        entity.setNombreLugar(request.getNombreLugar());
        entity.setPiso(request.getPiso());
        entity.setEdificio(request.getEdificio());
        entity.setDescripcion(request.getDescripcion());
    }

    // ==================== TIPO DISPOSITIVO ====================

    public TipoDispositivoResponse toResponse(TipoDispositivo entity) {
        if (entity == null) return null;
        TipoDispositivoResponse response = new TipoDispositivoResponse();
        response.setIdTipoDispositivo(entity.getIdTipoDispositivo());
        response.setNombreTipo(entity.getNombreTipo());
        return response;
    }

    public TipoDispositivo toEntity(TipoDispositivoRequest request) {
        if (request == null) return null;
        TipoDispositivo entity = new TipoDispositivo();
        entity.setNombreTipo(request.getNombreTipo());
        return entity;
    }

    public void updateEntity(TipoDispositivo entity, TipoDispositivoRequest request) {
        if (entity == null || request == null) return;
        entity.setNombreTipo(request.getNombreTipo());
    }

    // ==================== TIPO ESTADO DISPOSITIVO ====================

    public TipoEstadoDispResponse toResponse(TipoEstadoDisp entity) {
        if (entity == null) return null;
        TipoEstadoDispResponse response = new TipoEstadoDispResponse();
        response.setIdTipoEstado(entity.getIdTipoEstado());
        response.setNombreEstado(entity.getNombreEstado());
        response.setDescripcion(entity.getDescripcion());
        response.setActivo(entity.getActivo());
        return response;
    }

    public TipoEstadoDisp toEntity(TipoEstadoDispRequest request) {
        if (request == null) return null;
        TipoEstadoDisp entity = new TipoEstadoDisp();
        entity.setNombreEstado(request.getNombreEstado());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo() != null ? request.getActivo() : true);
        return entity;
    }

    public void updateEntity(TipoEstadoDisp entity, TipoEstadoDispRequest request) {
        if (entity == null || request == null) return;
        entity.setNombreEstado(request.getNombreEstado());
        entity.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) {
            entity.setActivo(request.getActivo());
        }
    }

    // ==================== TIPO MANTENIMIENTO ====================

    public TipoMantenimientoResponse toResponse(TipoMantenimiento entity) {
        if (entity == null) return null;
        TipoMantenimientoResponse response = new TipoMantenimientoResponse();
        response.setId(entity.getId());
        response.setNombreTipoMantenimiento(entity.getNombreTipoMantenimiento());
        return response;
    }

    public TipoMantenimiento toEntity(TipoMantenimientoRequest request) {
        if (request == null) return null;
        TipoMantenimiento entity = new TipoMantenimiento();
        entity.setNombreTipoMantenimiento(request.getNombreTipoMantenimiento());
        return entity;
    }

    public void updateEntity(TipoMantenimiento entity, TipoMantenimientoRequest request) {
        if (entity == null || request == null) return;
        entity.setNombreTipoMantenimiento(request.getNombreTipoMantenimiento());
    }

    // ==================== TIPO PRIORIDAD TICKET ====================

    public TipoPrioridadTicketResponse toResponse(TipoPrioridadTicket entity) {
        if (entity == null) return null;
        TipoPrioridadTicketResponse response = new TipoPrioridadTicketResponse();
        response.setId(entity.getId());
        response.setNombrePrioridad(entity.getNombrePrioridad());
        response.setDescripcion(entity.getDescripcion());
        return response;
    }

    public TipoPrioridadTicket toEntity(TipoPrioridadTicketRequest request) {
        if (request == null) return null;
        TipoPrioridadTicket entity = new TipoPrioridadTicket();
        entity.setNombrePrioridad(request.getNombrePrioridad());
        entity.setDescripcion(request.getDescripcion());
        return entity;
    }

    public void updateEntity(TipoPrioridadTicket entity, TipoPrioridadTicketRequest request) {
        if (entity == null || request == null) return;
        entity.setNombrePrioridad(request.getNombrePrioridad());
        entity.setDescripcion(request.getDescripcion());
    }

    // ==================== TIPO TONER ====================

    public TipoTonerResponse toResponse(TipoToner entity) {
        if (entity == null) return null;
        TipoTonerResponse response = new TipoTonerResponse();
        response.setId(entity.getId());
        response.setNombreTipoToner(entity.getNombreTipoToner());
        return response;
    }

    public TipoToner toEntity(TipoTonerRequest request) {
        if (request == null) return null;
        TipoToner entity = new TipoToner();
        entity.setNombreTipoToner(request.getNombreTipoToner());
        return entity;
    }

    public void updateEntity(TipoToner entity, TipoTonerRequest request) {
        if (entity == null || request == null) return;
        entity.setNombreTipoToner(request.getNombreTipoToner());
    }

    // ==================== USUARIO ====================

    public UsuarioResponse toResponse(Usuario entity) {
        if (entity == null) return null;
        UsuarioResponse response = new UsuarioResponse();
        response.setIdUsuario(entity.getIdUsuario());
        response.setNombre(entity.getNombre());
        response.setApellido(entity.getApellido());
        response.setUsuarioLogin(entity.getUsuarioLogin());
        response.setEmail(entity.getEmail());
        response.setEstado(entity.getEstado());
        response.setFechaRegistro(entity.getFechaRegistro());
        response.setRol(entity.getRol());
        return response;
    }

    public Usuario toEntity(UsuarioRequest request) {
        if (request == null) return null;
        Usuario entity = new Usuario();
        entity.setNombre(request.getNombre());
        entity.setApellido(request.getApellido());
        entity.setUsuarioLogin(request.getUsuarioLogin());
        entity.setEmail(request.getEmail());
        entity.setEstado(request.getEstado() != null ? request.getEstado() : "ACTIVO");
        entity.setRol(request.getRol());
        return entity;
    }

    public void updateEntity(Usuario entity, UsuarioRequest request) {
        if (entity == null || request == null) return;
        entity.setNombre(request.getNombre());
        entity.setApellido(request.getApellido());
        entity.setUsuarioLogin(request.getUsuarioLogin());
        entity.setEmail(request.getEmail());
        if (request.getEstado() != null) {
            entity.setEstado(request.getEstado());
        }
        entity.setRol(request.getRol());
    }

    // ==================== HISTORIAL UBICACION ====================

    public HistorialUbicacionResponse toResponse(HistorialUbicacion entity) {
        if (entity == null) return null;
        HistorialUbicacionResponse response = new HistorialUbicacionResponse();
        response.setIdHistorial(entity.getIdHistorial());
        response.setIdDispositivo(entity.getDispositivo() != null ? entity.getDispositivo().getIdDispositivo() : null);
        response.setLugar(toResponse(entity.getLugar()));
        response.setUsuario(toResponse(entity.getUsuario()));
        response.setFechaEntrada(entity.getFechaEntrada());
        response.setFechaSalida(entity.getFechaSalida());
        return response;
    }

    public HistorialUbicacion toEntity(HistorialUbicacionRequest request) {
        if (request == null) return null;
        HistorialUbicacion entity = new HistorialUbicacion();
        entity.setFechaEntrada(request.getFechaEntrada());
        entity.setFechaSalida(request.getFechaSalida());
        return entity;
    }

    public void updateEntity(HistorialUbicacion entity, HistorialUbicacionRequest request) {
        if (entity == null || request == null) return;
        entity.setFechaEntrada(request.getFechaEntrada());
        entity.setFechaSalida(request.getFechaSalida());
    }

    public List<HistorialUbicacionResponse> toHistorialUbicacionResponseList(List<HistorialUbicacion> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ==================== STOCK TONER ====================

    public StockTonerResponse toResponse(StockToner entity) {
        if (entity == null) return null;
        StockTonerResponse response = new StockTonerResponse();
        response.setIdTipoToner(entity.getIdTipoToner());
        response.setNombreTipoToner(entity.getNombreTipoToner());
        response.setTotalEntradas(entity.getTotalEntradas());
        response.setTotalSalidas(entity.getTotalSalidas());
        response.setStockActual(entity.getStockActual());
        return response;
    }

    // ==================== DISPOSITIVO ====================

    public DispositivoResponse toResponse(Dispositivo entity) {
        if (entity == null) return null;
        DispositivoResponse response = new DispositivoResponse();
        response.setIdDispositivo(entity.getIdDispositivo());
        response.setTipoDispositivo(toResponse(entity.getTipoDispositivo()));
        response.setMarca(entity.getMarca());
        response.setModelo(entity.getModelo());
        response.setNumeroSerie(entity.getNumeroSerie());
        response.setInventario(entity.getInventario());
        response.setTipoEstado(toResponse(entity.getTipoEstado()));
        response.setFechaCompra(entity.getFechaCompra());
        response.setCosto(entity.getCosto());
        response.setNotas(entity.getNotas());
        response.setFechaRegistro(entity.getFechaRegistro());
        response.setFechaBaja(entity.getFechaBaja());
        response.setHistorialUbicaciones(toHistorialUbicacionResponseList(entity.getHistorialUbicaciones()));
        return response;
    }

    public Dispositivo toEntity(DispositivoRequest request) {
        if (request == null) return null;
        Dispositivo entity = new Dispositivo();
        entity.setMarca(request.getMarca());
        entity.setModelo(request.getModelo());
        entity.setNumeroSerie(request.getNumeroSerie());
        entity.setInventario(request.getInventario());
        entity.setFechaCompra(request.getFechaCompra());
        entity.setCosto(request.getCosto());
        entity.setNotas(request.getNotas());
        entity.setFechaBaja(request.getFechaBaja());
        return entity;
    }

    public void updateEntity(Dispositivo entity, DispositivoRequest request) {
        if (entity == null || request == null) return;
        entity.setMarca(request.getMarca());
        entity.setModelo(request.getModelo());
        entity.setNumeroSerie(request.getNumeroSerie());
        entity.setInventario(request.getInventario());
        entity.setFechaCompra(request.getFechaCompra());
        entity.setCosto(request.getCosto());
        entity.setNotas(request.getNotas());
        entity.setFechaBaja(request.getFechaBaja());
    }

    // ==================== TICKET ====================

    public TicketResponse toResponse(Ticket entity) {
        if (entity == null) return null;
        TicketResponse response = new TicketResponse();
        response.setIdTicket(entity.getIdTicket());
        response.setUsuarioCreador(toResponse(entity.getUsuarioCreador()));
        response.setUsuarioAsignado(toResponse(entity.getUsuarioAsignado()));
        response.setDepartamento(toResponse(entity.getDepartamento()));
        response.setAsunto(entity.getAsunto());
        response.setDescripcion(entity.getDescripcion());
        response.setEstadoTicket(toResponse(entity.getEstadoTicket()));
        response.setPrioridad(toResponse(entity.getPrioridad()));
        response.setFechaCreacion(entity.getFechaCreacion());
        response.setFechaCierre(entity.getFechaCierre());
        return response;
    }

    public Ticket toEntity(TicketRequest request) {
        if (request == null) return null;
        Ticket entity = new Ticket();
        entity.setAsunto(request.getAsunto());
        entity.setDescripcion(request.getDescripcion());
        return entity;
    }

    public void updateEntity(Ticket entity, TicketRequest request) {
        if (entity == null || request == null) return;
        entity.setAsunto(request.getAsunto());
        entity.setDescripcion(request.getDescripcion());
    }

    // ==================== MANTENIMIENTO ====================

    public MantenimientoResponse toResponse(Mantenimiento entity) {
        if (entity == null) return null;
        MantenimientoResponse response = new MantenimientoResponse();
        response.setIdMantenimiento(entity.getIdMantenimiento());
        response.setDispositivo(toResponse(entity.getDispositivo()));
        response.setUsuarioSolicita(toResponse(entity.getUsuarioSolicita()));
        response.setUsuarioAtiende(toResponse(entity.getUsuarioAtiende()));
        response.setTipoMantenimiento(toResponse(entity.getTipoMantenimiento()));
        response.setDescripcion(entity.getDescripcion());
        response.setFechaProgramada(entity.getFechaProgramada());
        response.setFechaRealizado(entity.getFechaRealizado());
        response.setEstado(entity.getEstado());
        response.setCosto(entity.getCosto());
        response.setNotas(entity.getNotas());
        response.setFechaRegistro(entity.getFechaRegistro());
        response.setPilaCmos(entity.getPilaCmos());
        response.setPastaCpu(entity.getPastaCpu());
        response.setLimpieza(entity.getLimpieza());
        return response;
    }

    public Mantenimiento toEntity(MantenimientoRequest request) {
        if (request == null) return null;
        Mantenimiento entity = new Mantenimiento();
        entity.setDescripcion(request.getDescripcion());
        entity.setFechaProgramada(request.getFechaProgramada());
        entity.setFechaRealizado(request.getFechaRealizado());
        entity.setEstado(request.getEstado() != null ? request.getEstado() : "PROGRAMADO");
        entity.setCosto(request.getCosto());
        entity.setNotas(request.getNotas());
        entity.setPilaCmos(request.getPilaCmos());
        entity.setPastaCpu(request.getPastaCpu());
        entity.setLimpieza(request.getLimpieza());
        return entity;
    }

    public void updateEntity(Mantenimiento entity, MantenimientoRequest request) {
        if (entity == null || request == null) return;
        entity.setDescripcion(request.getDescripcion());
        entity.setFechaProgramada(request.getFechaProgramada());
        entity.setFechaRealizado(request.getFechaRealizado());
        if (request.getEstado() != null) {
            entity.setEstado(request.getEstado());
        }
        entity.setCosto(request.getCosto());
        entity.setNotas(request.getNotas());
        entity.setPilaCmos(request.getPilaCmos());
        entity.setPastaCpu(request.getPastaCpu());
        entity.setLimpieza(request.getLimpieza());
    }

    // ==================== ENTRADAS TONER ====================

    public EntradasTonerResponse toResponse(EntradasToner entity) {
        if (entity == null) return null;
        EntradasTonerResponse response = new EntradasTonerResponse();
        response.setId(entity.getId());
        response.setUsuarioEntrada(toResponse(entity.getUsuarioEntrada()));
        response.setFechaEntrada(entity.getFechaEntrada());
        response.setTipoToner(toResponse(entity.getTipoToner()));
        response.setCantidad(entity.getCantidad());
        response.setObservaciones(entity.getObservaciones());
        return response;
    }

    public EntradasToner toEntity(EntradasTonerRequest request) {
        if (request == null) return null;
        EntradasToner entity = new EntradasToner();
        entity.setCantidad(request.getCantidad());
        entity.setObservaciones(request.getObservaciones());
        return entity;
    }

    public void updateEntity(EntradasToner entity, EntradasTonerRequest request) {
        if (entity == null || request == null) return;
        entity.setCantidad(request.getCantidad());
        entity.setObservaciones(request.getObservaciones());
    }

    // ==================== SALIDAS TONER ====================

    public SalidasTonerResponse toResponse(SalidasToner entity) {
        if (entity == null) return null;
        SalidasTonerResponse response = new SalidasTonerResponse();
        response.setId(entity.getId());
        response.setUsuarioInstala(toResponse(entity.getUsuarioInstala()));
        response.setUsuarioRecibe(toResponse(entity.getUsuarioRecibe()));
        response.setTipoToner(toResponse(entity.getTipoToner()));
        response.setFechaSalida(entity.getFechaSalida());
        response.setCantidad(entity.getCantidad());
        response.setDepartamento(toResponse(entity.getDepartamento()));
        response.setObservaciones(entity.getObservaciones());
        return response;
    }

    public SalidasToner toEntity(SalidasTonerRequest request) {
        if (request == null) return null;
        SalidasToner entity = new SalidasToner();
        entity.setCantidad(request.getCantidad());
        entity.setObservaciones(request.getObservaciones());
        return entity;
    }

    public void updateEntity(SalidasToner entity, SalidasTonerRequest request) {
        if (entity == null || request == null) return;
        entity.setCantidad(request.getCantidad());
        entity.setObservaciones(request.getObservaciones());
    }

    // ==================== ANOTACION ====================

    public AnotacionResponse toResponse(Anotacion entity) {
        if (entity == null) return null;
        AnotacionResponse response = new AnotacionResponse();
        response.setIdAnotacion(entity.getIdAnotacion());
        response.setUsuario(toResponse(entity.getUsuario()));
        response.setTitulo(entity.getTitulo());
        response.setContenido(entity.getContenido());
        response.setPagina(entity.getPagina());
        response.setFechaAnotacion(entity.getFechaAnotacion());
        response.setEtiquetas(entity.getEtiquetas());
        response.setImportante(entity.getImportante());
        return response;
    }

    // ==================== SERVICIO ====================

    public ServicioResponse toResponse(Servicio entity) {
        if (entity == null) return null;
        ServicioResponse response = new ServicioResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        response.setDescripcion(entity.getDescripcion());
        response.setActivo(entity.getActivo());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public Servicio toEntity(ServicioRequest request) {
        if (request == null) return null;
        Servicio entity = new Servicio();
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo() != null ? request.getActivo() : true);
        return entity;
    }

    public void updateEntity(Servicio entity, ServicioRequest request) {
        if (entity == null || request == null) return;
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        if (request.getActivo() != null) {
            entity.setActivo(request.getActivo());
        }
    }

    // ==================== TIPO INCIDENTE ====================

    public TipoIncidenteResponse toResponse(TipoIncidente entity) {
        if (entity == null) return null;
        TipoIncidenteResponse response = new TipoIncidenteResponse();
        response.setId(entity.getId());
        response.setNombre(entity.getNombre());
        response.setDescripcion(entity.getDescripcion());
        return response;
    }

    public TipoIncidente toEntity(TipoIncidenteRequest request) {
        if (request == null) return null;
        TipoIncidente entity = new TipoIncidente();
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        return entity;
    }

    public void updateEntity(TipoIncidente entity, TipoIncidenteRequest request) {
        if (entity == null || request == null) return;
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
    }

    // ==================== BITACORA SERVICIO ====================

    public BitacoraServicioResponse toResponse(BitacoraServicio entity) {
        if (entity == null) return null;
        BitacoraServicioResponse response = new BitacoraServicioResponse();
        response.setId(entity.getId());
        response.setServicio(toResponse(entity.getServicio()));
        response.setTipoIncidente(toResponse(entity.getTipoIncidente()));
        response.setEstado(entity.getEstado() != null ? entity.getEstado().name() : null);
        response.setComentario(entity.getComentario());
        response.setFechaInicio(entity.getFechaInicio());
        response.setFechaFin(entity.getFechaFin());
        response.setReportadoPor(toResponse(entity.getReportadoPor()));
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }

    public BitacoraServicio toEntity(BitacoraServicioRequest request) {
        if (request == null) return null;
        BitacoraServicio entity = new BitacoraServicio();
        entity.setEstado(EstadoBitacora.valueOf(request.getEstado()));
        entity.setComentario(request.getComentario());
        entity.setFechaInicio(request.getFechaInicio());
        entity.setFechaFin(request.getFechaFin());
        return entity;
    }

    public void updateEntity(BitacoraServicio entity, BitacoraServicioRequest request) {
        if (entity == null || request == null) return;
        if (request.getEstado() != null) {
            entity.setEstado(EstadoBitacora.valueOf(request.getEstado()));
        }
        entity.setComentario(request.getComentario());
        entity.setFechaInicio(request.getFechaInicio());
        entity.setFechaFin(request.getFechaFin());
    }

    // ==================== LIST CONVERSIONS ====================

    public <T, R> List<R> toResponseList(List<T> entities, java.util.function.Function<T, R> mapper) {
        if (entities == null) return Collections.emptyList();
        return entities.stream().map(mapper).collect(Collectors.toList());
    }
}

