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
    

    @CircuitBreaker(name = "alertaCB", fallbackMethod = "fallbackSincronizarMapa")
    public List<ReporteGeoModel> sincronizarMapa(){
        log.info("Iniciando sincronización del mapa...");
        //Aqui se obtienen los datos del microservicio de alertas
        List<ReporteDTO> reportesDTO = alertaCliente.obtenerTodosLosReportes();

        //Convertir ReporteDTO a ReporteGeoModel
        List<ReporteGeoModel> reportesGeo = reportesDTO.stream().map(dto -> {
            ReporteGeoModel geo = new ReporteGeoModel();
            geo.setIdOriginal(dto.getId());
            geo.setTitulo(dto.getTitulo());
            geo.setLatitud(dto.getLatitud());
            geo.setLongitud(dto.getLongitud());
            return geo;
        }).collect(Collectors.toList());

        //Guardamos/Actualizamos en nuestra tabla "reportes_geo
        log.info("Guardando {} reportes en la base de datos de monitoreo.", reportesGeo.size());
        return mapaRepository.saveAll(reportesGeo);

        //Se ejecuta automaticamente si ms-alerta-incendios está apagado o falla
        
    }
    public List<ReporteGeoModel> fallbackSincronizarMapa(Exception e) {
        log.error("Circuito Abierto: ms-alerta no responde. Motivo: {}", e.getMessage());
        log.info("Activando Plan B: Devolviendo los últimos datos guardados localmente.");
        
        // Devolvemos lo que ya teníamos en nuestra base de datos para que el mapa de React no se caiga
        return mapaRepository.findAll(); 
    }
}
    