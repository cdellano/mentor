package saul.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import saul.dto.request.AnotacionRequest;
import saul.dto.response.AnotacionResponse;
import saul.dto.response.UsuarioResponse;
import saul.exception.ResourceNotFoundException;
import saul.service.AnotacionService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnotacionController.class)
class AnotacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private AnotacionService anotacionService;

    private AnotacionResponse anotacionResponse;
    private AnotacionRequest anotacionRequest;
    private UsuarioResponse usuarioResponse;
    private Page<AnotacionResponse> anotacionPage;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        usuarioResponse = UsuarioResponse.builder()
                .idUsuario(1L)
                .nombre("Usuario Test")
                .email("test@example.com")
                .build();

        anotacionResponse = AnotacionResponse.builder()
                .idAnotacion(1L)
                .usuario(usuarioResponse)
                .titulo("Título de prueba")
                .contenido("Contenido de prueba")
                .pagina(1)
                .fechaAnotacion(LocalDateTime.now())
                .etiquetas("etiqueta1,etiqueta2")
                .importante(true)
                .build();

        anotacionRequest = AnotacionRequest.builder()
                .idUsuario(1L)
                .titulo("Título de prueba")
                .contenido("Contenido de prueba")
                .pagina(1)
                .etiquetas("etiqueta1,etiqueta2")
                .importante(true)
                .build();

        anotacionPage = new PageImpl<>(List.of(anotacionResponse), PageRequest.of(0, 20), 1);
    }

    @Nested
    @DisplayName("GET /api/anotaciones")
    class FindAllTests {

        @Test
        @DisplayName("Debe retornar página de anotaciones exitosamente")
        void findAll_ShouldReturnPageOfAnotaciones() throws Exception {
            when(anotacionService.findAll(any(Pageable.class))).thenReturn(anotacionPage);

            mockMvc.perform(get("/api/anotaciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].idAnotacion").value(1L))
                    .andExpect(jsonPath("$.content[0].titulo").value("Título de prueba"))
                    .andExpect(jsonPath("$.totalElements").value(1));

            verify(anotacionService).findAll(any(Pageable.class));
        }

        @Test
        @DisplayName("Debe retornar página vacía cuando no hay anotaciones")
        void findAll_ShouldReturnEmptyPage_WhenNoAnotaciones() throws Exception {
            Page<AnotacionResponse> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 20), 0);
            when(anotacionService.findAll(any(Pageable.class))).thenReturn(emptyPage);

            mockMvc.perform(get("/api/anotaciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.totalElements").value(0));
        }

        @Test
        @DisplayName("Debe respetar parámetros de paginación")
        void findAll_ShouldRespectPaginationParams() throws Exception {
            when(anotacionService.findAll(any(Pageable.class))).thenReturn(anotacionPage);

            mockMvc.perform(get("/api/anotaciones")
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "titulo,asc"))
                    .andExpect(status().isOk());

            verify(anotacionService).findAll(any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("GET /api/anotaciones/{id}")
    class FindByIdTests {

        @Test
        @DisplayName("Debe retornar anotación cuando existe")
        void findById_ShouldReturnAnotacion_WhenExists() throws Exception {
            when(anotacionService.findById(1L)).thenReturn(anotacionResponse);

            mockMvc.perform(get("/api/anotaciones/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idAnotacion").value(1L))
                    .andExpect(jsonPath("$.titulo").value("Título de prueba"))
                    .andExpect(jsonPath("$.contenido").value("Contenido de prueba"))
                    .andExpect(jsonPath("$.pagina").value(1))
                    .andExpect(jsonPath("$.importante").value(true));

            verify(anotacionService).findById(1L);
        }

        @Test
        @DisplayName("Debe retornar 404 cuando no existe")
        void findById_ShouldReturn404_WhenNotExists() throws Exception {
            when(anotacionService.findById(999L)).thenThrow(new ResourceNotFoundException("Anotacion", "id", 999L));

            mockMvc.perform(get("/api/anotaciones/999"))
                    .andExpect(status().isNotFound());

            verify(anotacionService).findById(999L);
        }
    }

    @Nested
    @DisplayName("GET /api/anotaciones/usuario/{idUsuario}")
    class FindByUsuarioTests {

        @Test
        @DisplayName("Debe retornar anotaciones por usuario")
        void findByUsuario_ShouldReturnAnotaciones() throws Exception {
            when(anotacionService.findByUsuario(eq(1L), any(Pageable.class))).thenReturn(anotacionPage);

            mockMvc.perform(get("/api/anotaciones/usuario/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].usuario.idUsuario").value(1L));

            verify(anotacionService).findByUsuario(eq(1L), any(Pageable.class));
        }

        @Test
        @DisplayName("Debe retornar página vacía cuando usuario no tiene anotaciones")
        void findByUsuario_ShouldReturnEmptyPage_WhenNoAnotaciones() throws Exception {
            Page<AnotacionResponse> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 20), 0);
            when(anotacionService.findByUsuario(eq(99L), any(Pageable.class))).thenReturn(emptyPage);

            mockMvc.perform(get("/api/anotaciones/usuario/99"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty());
        }
    }

    @Nested
    @DisplayName("GET /api/anotaciones/pagina/{pagina}")
    class FindByPaginaTests {

        @Test
        @DisplayName("Debe retornar anotaciones por número de página")
        void findByPagina_ShouldReturnAnotaciones() throws Exception {
            when(anotacionService.findByPagina(eq(1), any(Pageable.class))).thenReturn(anotacionPage);

            mockMvc.perform(get("/api/anotaciones/pagina/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].pagina").value(1));

            verify(anotacionService).findByPagina(eq(1), any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("GET /api/anotaciones/etiqueta")
    class FindByEtiquetaTests {

        @Test
        @DisplayName("Debe retornar anotaciones por etiqueta")
        void findByEtiqueta_ShouldReturnAnotaciones() throws Exception {
            when(anotacionService.findByEtiqueta(eq("etiqueta1"), any(Pageable.class))).thenReturn(anotacionPage);

            mockMvc.perform(get("/api/anotaciones/etiqueta")
                            .param("etiqueta", "etiqueta1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].etiquetas").value("etiqueta1,etiqueta2"));

            verify(anotacionService).findByEtiqueta(eq("etiqueta1"), any(Pageable.class));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando falta el parámetro etiqueta")
        void findByEtiqueta_ShouldReturn400_WhenMissingParam() throws Exception {
            mockMvc.perform(get("/api/anotaciones/etiqueta"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/anotaciones/contenido")
    class FindByContenidoTests {

        @Test
        @DisplayName("Debe retornar anotaciones por contenido")
        void findByContenido_ShouldReturnAnotaciones() throws Exception {
            when(anotacionService.findByContenido(eq("prueba"), any(Pageable.class))).thenReturn(anotacionPage);

            mockMvc.perform(get("/api/anotaciones/contenido")
                            .param("contenido", "prueba"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].contenido").value("Contenido de prueba"));

            verify(anotacionService).findByContenido(eq("prueba"), any(Pageable.class));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando falta el parámetro contenido")
        void findByContenido_ShouldReturn400_WhenMissingParam() throws Exception {
            mockMvc.perform(get("/api/anotaciones/contenido"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/anotaciones/fecha")
    class FindByFechaRangoTests {

        @Test
        @DisplayName("Debe retornar anotaciones por rango de fecha")
        void findByFechaRango_ShouldReturnAnotaciones() throws Exception {
            LocalDateTime inicio = LocalDateTime.of(2026, 1, 1, 0, 0);
            LocalDateTime fin = LocalDateTime.of(2026, 12, 31, 23, 59);

            when(anotacionService.findByFechaRango(any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                    .thenReturn(anotacionPage);

            mockMvc.perform(get("/api/anotaciones/fecha")
                            .param("inicio", inicio.format(DateTimeFormatter.ISO_DATE_TIME))
                            .param("fin", fin.format(DateTimeFormatter.ISO_DATE_TIME)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray());

            verify(anotacionService).findByFechaRango(any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando faltan parámetros de fecha")
        void findByFechaRango_ShouldReturn400_WhenMissingParams() throws Exception {
            mockMvc.perform(get("/api/anotaciones/fecha"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/anotaciones")
    class CreateTests {

        @Test
        @DisplayName("Debe crear anotación exitosamente")
        void create_ShouldReturnCreatedAnotacion() throws Exception {
            when(anotacionService.create(any(AnotacionRequest.class))).thenReturn(anotacionResponse);

            mockMvc.perform(post("/api/anotaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(anotacionRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idAnotacion").value(1L))
                    .andExpect(jsonPath("$.titulo").value("Título de prueba"));

            verify(anotacionService).create(any(AnotacionRequest.class));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando titulo está vacío")
        void create_ShouldReturn400_WhenTituloIsBlank() throws Exception {
            AnotacionRequest invalidRequest = AnotacionRequest.builder()
                    .idUsuario(1L)
                    .titulo("")
                    .contenido("Contenido")
                    .pagina(1)
                    .build();

            mockMvc.perform(post("/api/anotaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando idUsuario es null")
        void create_ShouldReturn400_WhenIdUsuarioIsNull() throws Exception {
            AnotacionRequest invalidRequest = AnotacionRequest.builder()
                    .titulo("Título")
                    .contenido("Contenido")
                    .pagina(1)
                    .build();

            mockMvc.perform(post("/api/anotaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando contenido está vacío")
        void create_ShouldReturn400_WhenContenidoIsBlank() throws Exception {
            AnotacionRequest invalidRequest = AnotacionRequest.builder()
                    .idUsuario(1L)
                    .titulo("Título")
                    .contenido("")
                    .pagina(1)
                    .build();

            mockMvc.perform(post("/api/anotaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando pagina es null")
        void create_ShouldReturn400_WhenPaginaIsNull() throws Exception {
            AnotacionRequest invalidRequest = AnotacionRequest.builder()
                    .idUsuario(1L)
                    .titulo("Título")
                    .contenido("Contenido")
                    .build();

            mockMvc.perform(post("/api/anotaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando titulo excede 120 caracteres")
        void create_ShouldReturn400_WhenTituloExceedsMaxLength() throws Exception {
            String tituloLargo = "a".repeat(121);
            AnotacionRequest invalidRequest = AnotacionRequest.builder()
                    .idUsuario(1L)
                    .titulo(tituloLargo)
                    .contenido("Contenido")
                    .pagina(1)
                    .build();

            mockMvc.perform(post("/api/anotaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando etiquetas excede 200 caracteres")
        void create_ShouldReturn400_WhenEtiquetasExceedsMaxLength() throws Exception {
            String etiquetasLargas = "a".repeat(201);
            AnotacionRequest invalidRequest = AnotacionRequest.builder()
                    .idUsuario(1L)
                    .titulo("Título")
                    .contenido("Contenido")
                    .pagina(1)
                    .etiquetas(etiquetasLargas)
                    .build();

            mockMvc.perform(post("/api/anotaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /api/anotaciones/{id}")
    class UpdateTests {

        @Test
        @DisplayName("Debe actualizar anotación exitosamente")
        void update_ShouldReturnUpdatedAnotacion() throws Exception {
            AnotacionResponse updatedResponse = AnotacionResponse.builder()
                    .idAnotacion(1L)
                    .usuario(usuarioResponse)
                    .titulo("Título actualizado")
                    .contenido("Contenido actualizado")
                    .pagina(2)
                    .fechaAnotacion(LocalDateTime.now())
                    .etiquetas("nuevaEtiqueta")
                    .importante(false)
                    .build();

            when(anotacionService.update(eq(1L), any(AnotacionRequest.class))).thenReturn(updatedResponse);

            AnotacionRequest updateRequest = AnotacionRequest.builder()
                    .idUsuario(1L)
                    .titulo("Título actualizado")
                    .contenido("Contenido actualizado")
                    .pagina(2)
                    .etiquetas("nuevaEtiqueta")
                    .importante(false)
                    .build();

            mockMvc.perform(put("/api/anotaciones/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.titulo").value("Título actualizado"))
                    .andExpect(jsonPath("$.contenido").value("Contenido actualizado"));

            verify(anotacionService).update(eq(1L), any(AnotacionRequest.class));
        }

        @Test
        @DisplayName("Debe retornar 404 cuando anotación no existe")
        void update_ShouldReturn404_WhenNotExists() throws Exception {
            when(anotacionService.update(eq(999L), any(AnotacionRequest.class)))
                    .thenThrow(new ResourceNotFoundException("Anotacion", "id", 999L));

            mockMvc.perform(put("/api/anotaciones/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(anotacionRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando request es inválido")
        void update_ShouldReturn400_WhenInvalidRequest() throws Exception {
            AnotacionRequest invalidRequest = AnotacionRequest.builder()
                    .idUsuario(1L)
                    .titulo("")
                    .contenido("Contenido")
                    .pagina(1)
                    .build();

            mockMvc.perform(put("/api/anotaciones/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /api/anotaciones/{id}")
    class DeleteTests {

        @Test
        @DisplayName("Debe eliminar anotación exitosamente")
        void delete_ShouldReturnNoContent() throws Exception {
            doNothing().when(anotacionService).delete(1L);

            mockMvc.perform(delete("/api/anotaciones/1"))
                    .andExpect(status().isNoContent());

            verify(anotacionService).delete(1L);
        }

        @Test
        @DisplayName("Debe retornar 404 cuando anotación no existe")
        void delete_ShouldReturn404_WhenNotExists() throws Exception {
            doThrow(new ResourceNotFoundException("Anotacion", "id", 999L))
                    .when(anotacionService).delete(999L);

            mockMvc.perform(delete("/api/anotaciones/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /api/anotaciones/{id}/restore")
    class RestoreTests {

        @Test
        @DisplayName("Debe restaurar anotación exitosamente")
        void restore_ShouldReturnOk() throws Exception {
            doNothing().when(anotacionService).restore(1L);

            mockMvc.perform(put("/api/anotaciones/1/restore"))
                    .andExpect(status().isOk());

            verify(anotacionService).restore(1L);
        }

        @Test
        @DisplayName("Debe retornar 404 cuando anotación no existe")
        void restore_ShouldReturn404_WhenNotExists() throws Exception {
            doThrow(new ResourceNotFoundException("Anotacion", "id", 999L))
                    .when(anotacionService).restore(999L);

            mockMvc.perform(put("/api/anotaciones/999/restore"))
                    .andExpect(status().isNotFound());
        }
    }
}

