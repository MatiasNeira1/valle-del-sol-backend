package com.MunicipalidadDelValle.ms_alerta_incendios.service;
import org.springframework.stereotype.Service;
import com.MunicipalidadDelValle.ms_alerta_incendios.model.ReporteModel;
import com.MunicipalidadDelValle.ms_alerta_incendios.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.MunicipalidadDelValle.ms_alerta_incendios.Excepcion.*;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteService {
    @Autowired
    private ReporteRepository reporteRepository;

    @Transactional
    public ReporteModel crearNuevoReporte(ReporteModel reporte) {
        
        // 1. Validaciones de Negocio
        if (reporte.getTitulo() == null || reporte.getTitulo().trim().isEmpty()) {
            throw new ReporteInvalidoExcepcion("El reporte debe tener un título descriptivo.");
        }
        
        if (reporte.getLatitud() == null || reporte.getLongitud() == null) {
            throw new ReporteInvalidoExcepcion("Las coordenadas (latitud y longitud) son obligatorias para el mapa.");
        }

        // Validar que las coordenadas tengan sentido geográfico
        if (reporte.getLatitud() < -90 || reporte.getLatitud() > 90 || 
            reporte.getLongitud() < -180 || reporte.getLongitud() > 180) {
            throw new ReporteInvalidoExcepcion("Las coordenadas geográficas ingresadas no son válidas.");
        }

        // 2. Guardar en la base de datis 
        ReporteModel reporteGuardado = reporteRepository.save(reporte);

        // 3. TODO: Comunicación con ms-monitoreo-geo
        // Aquí es donde, en el futuro, usaremos un RestTemplate o FeignClient 
        // para avisarle al mapa que debe pintar el pin con reporteGuardado.getId()

        return reporteGuardado;
}
}
