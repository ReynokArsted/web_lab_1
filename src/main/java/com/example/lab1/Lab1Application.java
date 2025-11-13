package com.example.lab1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger log = LoggerFactory.getLogger(Lab1Application.class);

	@Bean
    public CommandLineRunner demo(NoteRepository repository) {
        return (args) -> {
			/*
			Tag tag1 = new Tag("Work");
			Tag tag2 = new Tag("Personal");	
			repository.save(new Note("Title", "text", tag2));
			repository.save(new Note("Next-hop", "switch", tag1));
			 */

            // Вывод всех заметок
            log.info("All notes:");
            repository.findAll().forEach(c -> log.info(c.toString()));

			repository.findById(2L).ifPresent
			(note -> 
			{
				log.info("Note findById(2):");
				log.info(note.toString());
			});
        };
    }
}