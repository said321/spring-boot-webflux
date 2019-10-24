package com.neo.Dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.neo.Entities.Student;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class StudentRepository {
	
	private List<Student> students = new ArrayList<Student>();
	
	public Flux<Student> findAll(){
		return Flux.fromStream(students.stream());
	}
	
	public Mono<Student> findById(int id) {
		for (int i = 0; i < students.size(); i++) {
			Student std = students.get(i);
			if(std.getId() == id)
				return Mono.just(std);
		}
		return null;
	}
	
	public Mono<Student> save(Student std) {
		if(students.add(std))
			return Mono.just(std);
		else
			return null;
	}
	
	public Mono<Boolean> delete(int id) {
		if(findById(id) != null) {
			students.remove(findById(id).block());
			return Mono.just(true);
		}else
			return Mono.just(false);
	}
	
	public Mono<Student> update(Student std){
		if(findById(std.getId()) != null) {
			findById(std.getId()).block().setName(std.getName());
			return Mono.just(std);			
		}
		else
			return null;
	}
	
	public List<Student> getStudents(){
		return students;
	}

}
