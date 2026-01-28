package saul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import saul.dto.response.StockTonerResponse;
import saul.service.StockTonerService;

import java.util.List;

/**
 * Controlador REST para gestionar el stock de tóners.
 * Permite consultar el inventario disponible de cartuchos de tinta/tóner.
 */
@RestController
@RequestMapping("/api/stock-toner")
@RequiredArgsConstructor
public class StockTonerController {

    private final StockTonerService stockTonerService;

    /**
     * Obtiene todo el inventario de tóners disponible en el sistema.
     * @return Lista completa con el stock de todos los tipos de tóner
     */
    @GetMapping
    public ResponseEntity<List<StockTonerResponse>> findAll() {
        return ResponseEntity.ok(stockTonerService.findAll());
    }

    /**
     * Consulta el stock disponible de un tipo específico de tóner.
     * @param idTipoToner Identificador único del tipo de tóner
     * @return Información del stock para el tipo de tóner especificado
     */
    @GetMapping("/tipo-toner/{idTipoToner}")
    public ResponseEntity<StockTonerResponse> findByTipoToner(@PathVariable Integer idTipoToner) {
        return ResponseEntity.ok(stockTonerService.findByTipoToner(idTipoToner));
    }

    /**
     * Obtiene la existencia de todos los tipos de tóner que hay en el sistema.
     * Calcula el stock actual de cada tipo de tóner basándose en las entradas y salidas registradas.
     * @return Lista completa con la existencia de todos los tipos de tóner
     */
    @GetMapping("/existencias")
    public ResponseEntity<List<StockTonerResponse>> findAllExistencias() {
        return ResponseEntity.ok(stockTonerService.findAllExistencias());
    }

}

