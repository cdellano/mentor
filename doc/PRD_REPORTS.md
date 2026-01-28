## PRD general para crear reportes.

Crea un nuevo endpoint que genere un reporte en tamaño carta, en orientación Horizontal y con paginación en formato (p. x de total), usa el encabezado del endpoint "/mantenimientos". Aplica la función de autoajuste en todas las celdas.

## Especificaciones puntuales.

El título obtenlo de reports.properties de la clave report.title.regDisp

El informe dará un reporte de todos los dispositivos registrados en el sistema.

El endpoint recibirá 2 parámetros fecha de inicio y fecha de fin (me refiero al campo fechaRegistro), y de forma opcional los parámetro marca, también por modelo, también por numeroserie, tambien por inventario, y por alguna palabra clave dentro del campo notas.

Usa una query nativa y toma en cuenta el soft delete.
