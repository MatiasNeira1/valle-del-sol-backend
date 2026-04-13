package com.MunicipalidadDelValle.ms_alerta_incendios.repository;

import com.MunicipalidadDelValle.ms_alerta_incendios.model.ReporteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteModel, Long> {

}
