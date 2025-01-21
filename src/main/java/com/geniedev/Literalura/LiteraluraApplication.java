package com.geniedev.Literalura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.geniedev.Literalura.principal.Principal;
import com.geniedev.Literalura.repository.AutorRepository;
import com.geniedev.Literalura.repository.LibroRepository;
import com.geniedev.Literalura.service.AutorService;
import com.geniedev.Literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LibroService libroService;
	@Autowired
	private AutorService autorService;
	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(libroService, autorService);

		principal.muestraMenu();

	}
}