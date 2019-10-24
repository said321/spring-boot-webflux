package com.neo.Entities;

import java.time.Instant;

public class Event {
	
	private Instant instance;
	private Student student;
	private double value;
	
	public Event() {
	}
	public Event(Instant instance, Student student, double value) {
		this.instance = instance;
		this.student = student;
		this.value = value;
	}
	public Instant getInstance() {
		return instance;
	}
	public void setInstance(Instant instance) {
		this.instance = instance;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

}
