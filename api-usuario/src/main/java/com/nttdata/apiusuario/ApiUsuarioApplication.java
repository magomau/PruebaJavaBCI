package com.nttdata.apiusuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nttdata.apiusuario.controller.UsuarioController;

@SpringBootApplication
public class ApiUsuarioApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiUsuarioApplication.class);
	
	public static void main(String[] args) {
		logger.info("Se Inicia la Prueba JAVA.");
		SpringApplication.run(ApiUsuarioApplication.class, args);
		
	}

}
