package com.MunicipalidadDelValle.ms_monitoreo_geo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Cambié esto para incluir PostMapping y CrossOrigin
import java.util.List;
import com.MunicipalidadDelValle.ms_monitoreo_geo.service.MapaService;
import com.MunicipalidadDelValle.ms_monitoreo_geo.model.ReporteGeoModel;

@RestController
@RequestMapping("/api/mapa")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // 1. PERMISO PARA REACT
public class MapaController {

    private final MapaService mapaService;

    @GetMapping("/sincronizar")
    public ResponseEntity<List<ReporteGeoModel>> sincronizarYObtenerMapa() {
        List<ReporteGeoModel> puntosEnMapa = mapaService.sincronizarMapa();
        return ResponseEntity.ok(puntosEnMapa);
    }

    // 2. ESTE ES EL MÉTODO QUE TE FALTABA PARA EL DASHBOARD
    @PostMapping("/crear")
    public ResponseEntity<ReporteGeoModel> crearReporte(@RequestBody ReporteGeoModel nuevoReporte) {
        // Aquí llamas al service para guardar en la base de datos de Oracle
        ReporteGeoModel guardado = mapaService.guardarReporte(nuevoReporte); 
        return ResponseEntity.ok(guardado);
    }
}