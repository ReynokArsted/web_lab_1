package com.example.lab1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.lab1.repositories.NoteRepository;

@SpringBootApplication
public class Lab1Application {
	public static void main(String[] args) {
		SpringApplication.run(Lab1Application.class, args);
	}

	// Подключение логирования
	//private static final Logger log = LoggerFactory.getLogger(Lab1Application.class);

	@Bean
    public CommandLineRunner demo(NoteRepository repository) {
        return (args) -> {
            // Вывод заметок
            //log.info("All notes:");
            //repository.findAll().forEach(c -> log.info(c.toString()));
        };
    }
}