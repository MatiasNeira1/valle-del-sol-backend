package com.MunicipalidadDelValle.ms_monitoreo_geo.service;

import com.MunicipalidadDelValle.ms_monitoreo_geo.repository.MapaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import com.MunicipalidadDelValle.ms_monitoreo_geo.model.ReporteGeoModel;
import java.util.List;
import java.util.stream.Collectors;
import com.MunicipalidadDelValle.ms_monitoreo_geo.cliente.AlertaCliente;
import com.MunicipalidadDelValle.ms_monitoreo_geo.dto.ReporteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MapaService {
    private final MapaRepository mapaRepository;
    private final AlertaCliente alertaCliente;

    // --- ESTO ES LO DE MATÍAS (FLUJO DE SINCRONIZACIÓN) ---
    @CircuitBreaker(name = "alertaCB", fallbackMethod = "fallbackSincronizarMapa")
    public List<ReporteGeoModel> sincronizarMapa(){
        log.info("Iniciando sincronización del mapa...");
        List<ReporteDTO> reportesDTO = alertaCliente.obtenerTodosLosReportes();

        List<ReporteGeoModel> reportesGeo = reportesDTO.stream().map(dto -> {
            ReporteGeoModel geo = new ReporteGeoModel();
            geo.setIdOriginal(dto.getId());
            geo.setTitulo(dto.getTitulo());
            geo.setLatitud(dto.getLatitud());
            geo.setLongitud(dto.getLongitud());
            return geo;
        }).collect(Collectors.toList());

        log.info("Guardando {} reportes en la base de datos de monitoreo.", reportesGeo.size());
        return mapaRepository.saveAll(reportesGeo);
    }

    // --- ESTA ES LA PIEZA NUEVA PARA TU DASHBOARD DE REACT ---
    public ReporteGeoModel guardarReporte(ReporteGeoModel nuevoReporte) {
        log.info("Dashboard enviando nuevo reporte para guardar: {}", nuevoReporte.getTitulo());
        return mapaRepository.save(nuevoReporte);
    }

    // --- ESTO ES EL FALLBACK DE MATÍAS ---
    public List<ReporteGeoModel> fallbackSincronizarMapa(Exception e) {
        log.error("Circuito Abierto: ms-alerta no responde. Motivo: {}", e.getMessage());
        return mapaRepository.findAll(); 
    }
}