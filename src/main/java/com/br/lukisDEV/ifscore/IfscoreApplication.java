package com.br.lukisDEV.ifscore;

import com.br.lukisDEV.ifscore.service.CampusService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IfscoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(IfscoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(CampusService campusService) {
		return args -> {
			campusService.seedCampuses();
		};
	}

}
