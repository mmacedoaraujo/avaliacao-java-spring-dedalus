package com.mmacedoaraujo.avaliacaojavaspringdedalus;

import com.mmacedoaraujo.avaliacaojavaspringdedalus.domain.User;
import com.mmacedoaraujo.avaliacaojavaspringdedalus.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
@AllArgsConstructor
public class AvaliacaoJavaSpringDedalusApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvaliacaoJavaSpringDedalusApplication.class, args);
	}

}
