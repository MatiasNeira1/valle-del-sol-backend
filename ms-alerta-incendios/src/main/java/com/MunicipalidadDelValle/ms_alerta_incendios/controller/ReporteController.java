package com.MunicipalidadDelValle.ms_alerta_incendios.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.MunicipalidadDelValle.ms_alerta_incendios.model.ReporteModel;
import com.MunicipalidadDelValle.ms_alerta_incendios.service.ReporteService;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping("/crear")
    public ResponseEntity<ReporteModel> crearReporte(@RequestBody ReporteModel nuevoReporte) {
        ReporteModel reporteCreado = reporteService.crearNuevoReporte(nuevoReporte);
        return new ResponseEntity<>(reporteCreado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReporteModel>> obtenerTodosLosReportes() {
        List<ReporteModel> reportes = reporteService.obtenerTodos();
        return new ResponseEntity<>(reportes, HttpStatus.OK);
    }

    @GetMapping("/sincronizar")
    public ResponseEntity<List<ReporteModel>> sincronizarReportes() {
        List<ReporteModel> reportes = reporteService.obtenerTodos();
        return new ResponseEntity<>(reportes, HttpStatus.OK);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<ReporteModel> editarReporte(@PathVariable Long id,
            @RequestBody ReporteModel reporteDetalles) {
        ReporteModel reporteActualizado = reporteService.actualizarReporte(id, reporteDetalles);
        return new ResponseEntity<>(reporteActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
