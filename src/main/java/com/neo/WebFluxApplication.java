package com.neo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.stream.Stream;

import com.neo.Dao.StudentRepository;
import com.neo.Entities.Student;

@SpringBootApplication
public class WebFluxApplication {

	@Autowired
	StudentRepository studentRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(WebFluxApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run() {
		return arg->{
			Stream.of(new Student(1, "said"), new Student(2, "khadija")).forEach(s->{
						studentRepository.save(s);
					});
			studentRepository.getStudents().forEach(std->{
				System.out.println(std);
			});
		};
	}
	
}
