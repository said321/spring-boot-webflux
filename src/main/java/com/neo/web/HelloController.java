package com.neo.web;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.neo.Dao.StudentRepository;
import com.neo.Entities.Event;
import com.neo.Entities.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/* 
 * if you wanna use curl tool
 * curl -d "{\"id\":3, \"name\":\"hassan\"}" -H "Content-Type: application/json" -X POST "localhost:8080/student/add"
 */

@RestController
public class HelloController {
	
	@Autowired
	StudentRepository studentRepository;

    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Welcome to reactive world ~");
    }
    
    // Flux<Student> as json array
    @GetMapping("/students")
    public Flux<Student> findAllStudents(){
		return studentRepository.findAll();
    }
    
    // Flux<Student> as Server Sent Event (SSE)
    @GetMapping(value="/students/sse", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Student> findAllStudentsSSE(){
		return studentRepository.findAll();
    }
    
    // Flux<Student> as json Stream
    @GetMapping(value="/students/stream", produces=MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Student> findAllStudentsStream(){
    	Flux<Long> interval = Flux.interval(Duration.ofMillis(1000));
    	Flux<Student> stds = studentRepository.findAll();
		return Flux.zip(interval, stds)
				.map(data->{
					return data.getT2();
				});
    }
    
    @GetMapping("/student/{id}")
    public Mono<Student> findStudent(@PathVariable int id){
		return studentRepository.findById(id);
    }
    
    @PostMapping("/student/add")
    public Mono<Student> saveStudent(@RequestBody Student student){
		return studentRepository.save(student);
    }
    
    @GetMapping("/student/del/{id}")
    public Mono<Boolean> delStudent(@PathVariable int id){
		return studentRepository.delete(id);
    }
    
    @PutMapping("student/update")
    public Mono<Student> update(@RequestBody Student student){
    	return studentRepository.update(student);
    }
    
    @GetMapping(value="/students/stream/{id}", produces=MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Event> studentStream(@PathVariable int id){
    	Flux<Long> interval = Flux.interval(Duration.ofMillis(1000));
    	Flux<Event> events = Flux.fromStream(Stream.generate(()->{
    		Event event = new Event(Instant.now(), 
    				studentRepository.findById(id).block(),
    				1100+Math.random()*1000
    				);
    		return event;
    	}));
    	
    	return Flux.zip(interval, events)
    			.map(data->{
    				return data.getT2();
    			});
    }
    
    @GetMapping(value="/otherStream/{id}", produces=MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Student> otherStream(@PathVariable int id){
    	WebClient client = WebClient.create("http://localhost:8080");
    	Flux<Student> events = client.get()
    			.uri("/students/stream/"+id)
    			.retrieve().bodyToFlux(Event.class)
    			.map(data->data.getStudent());
    	return events;
    }
    
}












