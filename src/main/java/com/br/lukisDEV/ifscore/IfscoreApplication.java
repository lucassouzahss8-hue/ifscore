package com.br.lukisDEV.ifscore;

import com.br.lukisDEV.ifscore.service.CampusService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IfscoreApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(IfscoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(CampusService campusService) {
		return args -> {
			campusService.seedCampuses();
		};
	}

}
