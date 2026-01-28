package saul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import saul.dto.EntityMapper;
import saul.dto.response.StockTonerResponse;
import saul.entity.StockToner;
import saul.entity.TipoToner;
import saul.exception.ResourceNotFoundException;
import saul.repository.EntradasTonerRepository;
import saul.repository.SalidasTonerRepository;
import saul.repository.TipoTonerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockTonerService {

    private final TipoTonerRepository tipoTonerRepository;
    private final EntradasTonerRepository entradasTonerRepository;
    private final SalidasTonerRepository salidasTonerRepository;
    private final EntityMapper mapper;

    public List<StockTonerResponse> findAll() {
        return tipoTonerRepository.findAll().stream()
                .map(this::calculateStock)
                .collect(Collectors.toList());
    }

    public StockTonerResponse findByTipoToner(Integer idTipoToner) {
        TipoToner tipoToner = tipoTonerRepository.findById(idTipoToner)
                .orElseThrow(() -> new ResourceNotFoundException("TipoToner", "id", idTipoToner));
        return calculateStock(tipoToner);
    }

    public List<StockTonerResponse> findAllExistencias() {
        return tipoTonerRepository.findAll().stream()
                .map(this::calculateStock)
                .collect(Collectors.toList());
    }

    private StockTonerResponse calculateStock(TipoToner tipoToner) {
        Integer totalEntradas = entradasTonerRepository.sumCantidadByTipoToner(tipoToner.getId());
        Integer totalSalidas = salidasTonerRepository.sumCantidadByTipoToner(tipoToner.getId());

        StockToner stock = new StockToner();
        stock.setIdTipoToner(tipoToner.getId());
        stock.setNombreTipoToner(tipoToner.getNombreTipoToner());
        stock.setTotalEntradas(totalEntradas != null ? totalEntradas : 0);
        stock.setTotalSalidas(totalSalidas != null ? totalSalidas : 0);
        stock.setStockActual(stock.getTotalEntradas() - stock.getTotalSalidas());

        return mapper.toResponse(stock);
    }
}

