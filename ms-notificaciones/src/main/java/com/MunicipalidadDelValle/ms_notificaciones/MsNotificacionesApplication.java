package com.MunicipalidadDelValle.ms_notificaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class MsNotificacionesApplication {

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

		SpringApplication.run(MsNotificacionesApplication.class, args);
	}
}
