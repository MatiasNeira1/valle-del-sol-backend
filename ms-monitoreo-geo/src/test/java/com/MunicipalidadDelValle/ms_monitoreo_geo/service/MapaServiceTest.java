package com.MunicipalidadDelValle.ms_monitoreo_geo.service;

import com.MunicipalidadDelValle.ms_monitoreo_geo.cliente.AlertaCliente;
import com.MunicipalidadDelValle.ms_monitoreo_geo.dto.ReporteDTO;
import com.MunicipalidadDelValle.ms_monitoreo_geo.model.ReporteGeoModel;
import com.MunicipalidadDelValle.ms_monitoreo_geo.repository.MapaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para MapaService.
 * Se usa Mockito puro para simular el cliente Feign y el repositorio,
 * sin necesidad de levantar Spring Context ni conectarse a Oracle.
 */
@ExtendWith(MockitoExtension.class)
class MapaServiceTest {

    @Mock
    private MapaRepository mapaRepository;

    @Mock
    private AlertaCliente alertaCliente;

    @InjectMocks
    private MapaService mapaService;

    private ReporteDTO reporteDTO1;
    private ReporteDTO reporteDTO2;
    private ReporteGeoModel geoModel1;
    private ReporteGeoModel geoModel2;

    @BeforeEach
    void setUp() {
        // Preparar DTOs que vendrían de ms-alerta-incendios
        reporteDTO1 = new ReporteDTO();
        reporteDTO1.setId(1L);
        reporteDTO1.setTitulo("Incendio sector norte");
        reporteDTO1.setDescripcion("Gran columna de humo");
        reporteDTO1.setLatitud(-33.4489);
        reporteDTO1.setLongitud(-70.6693);

        reporteDTO2 = new ReporteDTO();
        reporteDTO2.setId(2L);
        reporteDTO2.setTitulo("Foco en cerro San Cristóbal");
        reporteDTO2.setDescripcion("Foco menor controlable");
        reporteDTO2.setLatitud(-33.4250);
        reporteDTO2.setLongitud(-70.6320);

        // Preparar modelos Geo esperados
        geoModel1 = new ReporteGeoModel();
        geoModel1.setIdOriginal(1L);
        geoModel1.setTitulo("Incendio sector norte");
        geoModel1.setLatitud(-33.4489);
        geoModel1.setLongitud(-70.6693);

        geoModel2 = new ReporteGeoModel();
        geoModel2.setIdOriginal(2L);
        geoModel2.setTitulo("Foco en cerro San Cristóbal");
        geoModel2.setLatitud(-33.4250);
        geoModel2.setLongitud(-70.6320);
    }

    // ═══════════════════════════════════════════════════════════════════
    //  TESTS PARA sincronizarMapa()
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("sincronizarMapa - Casos exitosos")
    class SincronizarMapaExitoso {

        @Test
        @DisplayName("Debe sincronizar y guardar reportes del microservicio de alertas")
        void sincronizarConReportes() {
            // Arrange
            List<ReporteDTO> reportesDTO = Arrays.asList(reporteDTO1, reporteDTO2);
            when(alertaCliente.obtenerTodosLosReportes()).thenReturn(reportesDTO);
            when(mapaRepository.saveAll(anyList())).thenReturn(Arrays.asList(geoModel1, geoModel2));

            // Act
            List<ReporteGeoModel> resultado = mapaService.sincronizarMapa();

            // Assert
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            verify(alertaCliente, times(1)).obtenerTodosLosReportes();
            verify(mapaRepository, times(1)).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe manejar lista vacía del microservicio de alertas")
        void sincronizarSinReportes() {
            // Arrange
            when(alertaCliente.obtenerTodosLosReportes()).thenReturn(Collections.emptyList());
            when(mapaRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

            // Act
            List<ReporteGeoModel> resultado = mapaService.sincronizarMapa();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(alertaCliente, times(1)).obtenerTodosLosReportes();
            verify(mapaRepository, times(1)).saveAll(anyList());
        }

        @Test
        @DisplayName("Debe convertir correctamente ReporteDTO a ReporteGeoModel")
        void verificarConversionDTOaGeoModel() {
            // Arrange
            List<ReporteDTO> reportesDTO = Collections.singletonList(reporteDTO1);
            // Capturamos lo que se guarda para verificar la conversión
            when(alertaCliente.obtenerTodosLosReportes()).thenReturn(reportesDTO);
            when(mapaRepository.saveAll(anyList())).thenAnswer(invocation -> {
                List<ReporteGeoModel> argList = invocation.getArgument(0);
                // Verificamos la conversión aquí
                ReporteGeoModel convertido = argList.get(0);
                assertEquals(1L, convertido.getIdOriginal());
                assertEquals("Incendio sector norte", convertido.getTitulo());
                assertEquals(-33.4489, convertido.getLatitud());
                assertEquals(-70.6693, convertido.getLongitud());
                return argList;
            });

            // Act
            List<ReporteGeoModel> resultado = mapaService.sincronizarMapa();

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe sincronizar con un solo reporte")
        void sincronizarConUnReporte() {
            // Arrange
            List<ReporteDTO> reportesDTO = Collections.singletonList(reporteDTO1);
            List<ReporteGeoModel> geoModels = Collections.singletonList(geoModel1);
            when(alertaCliente.obtenerTodosLosReportes()).thenReturn(reportesDTO);
            when(mapaRepository.saveAll(anyList())).thenReturn(geoModels);

            // Act
            List<ReporteGeoModel> resultado = mapaService.sincronizarMapa();

            // Assert
            assertEquals(1, resultado.size());
            assertEquals("Incendio sector norte", resultado.get(0).getTitulo());
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    //  TESTS PARA fallbackSincronizarMapa() - Circuit Breaker
    // ═══════════════════════════════════════════════════════════════════

    @Nested
    @DisplayName("fallbackSincronizarMapa - Circuit Breaker")
    class FallbackSincronizarMapa {

        @Test
        @DisplayName("Debe devolver datos locales cuando ms-alerta falla")
        void fallbackConDatosLocales() {
            // Arrange
            List<ReporteGeoModel> datosLocales = Arrays.asList(geoModel1, geoModel2);
            when(mapaRepository.findAll()).thenReturn(datosLocales);

            // Act
            List<ReporteGeoModel> resultado = mapaService.fallbackSincronizarMapa(
                    new RuntimeException("ms-alerta no responde"));

            // Assert
            assertNotNull(resultado);
            assertEquals(2, resultado.size());
            verify(mapaRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Debe devolver lista vacía si no hay datos locales en fallback")
        void fallbackSinDatosLocales() {
            // Arrange
            when(mapaRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<ReporteGeoModel> resultado = mapaService.fallbackSincronizarMapa(
                    new RuntimeException("Connection refused"));

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
            verify(mapaRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Debe funcionar con diferentes tipos de excepción")
        void fallbackConDiferentesExcepciones() {
            // Arrange
            List<ReporteGeoModel> datosLocales = Collections.singletonList(geoModel1);
            when(mapaRepository.findAll()).thenReturn(datosLocales);

            // Act - Probamos con distintos tipos de excepción
            List<ReporteGeoModel> resultado1 = mapaService.fallbackSincronizarMapa(
                    new RuntimeException("Timeout"));
            List<ReporteGeoModel> resultado2 = mapaService.fallbackSincronizarMapa(
                    new Exception("Connection reset"));

            // Assert
            assertEquals(1, resultado1.size());
            assertEquals(1, resultado2.size());
            verify(mapaRepository, times(2)).findAll();
        }

        @Test
        @DisplayName("No debe llamar al cliente de alertas en el fallback")
        void fallbackNoLlamaAlertaCliente() {
            // Arrange
            when(mapaRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            mapaService.fallbackSincronizarMapa(new RuntimeException("Error"));

            // Assert - El fallback NO debe intentar llamar al microservicio de alertas
            verify(alertaCliente, never()).obtenerTodosLosReportes();
        }
    }
}
