package com.MunicipalidadDelValle.ms_alerta_incendios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.File;

//Con el exclude se desactiva la seguridad para que no pida autenticación al acceder a los endpoints, esto es solo para desarrollo, en producción se debería configurar correctamente la seguridad
@SpringBootApplication
public class MsAlertaIncendiosApplication {

	public static void main(String[] args) {
		String tnsAdmin = System.getenv("TNS_ADMIN_DIR");
		if (tnsAdmin == null) {
			tnsAdmin = System.getProperty("TNS_ADMIN_DIR");
		}
		if (tnsAdmin == null || tnsAdmin.trim().isEmpty() || !new File(tnsAdmin).exists()) {
			tnsAdmin = "C:/Fullstack/valle-del-sol-backend/Wallet_ms-alerta-incendio";
			if (!new File(tnsAdmin).exists()) {
				tnsAdmin = "C:/Fullstack/Wallet_FXVGR0RD9MQXJ20W";
			}
		}
		tnsAdmin = tnsAdmin.replace("\\", "/");
		System.setProperty("oracle.net.tns_admin", tnsAdmin);
		System.out.println(">>> Oracle TNS_ADMIN set to: " + tnsAdmin);

		SpringApplication.run(MsAlertaIncendiosApplication.class, args);
	}

}
