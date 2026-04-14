package com.MunicipalidadDelValle.ms_alerta_incendios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//Con el exclude se desactiva la seguridad para que no pida autenticación al acceder a los endpoints, esto es solo para desarrollo, en producción se debería configurar correctamente la seguridad
@SpringBootApplication
public class MsAlertaIncendiosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAlertaIncendiosApplication.class, args);
	}

}
