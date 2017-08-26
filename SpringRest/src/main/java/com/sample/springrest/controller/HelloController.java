package com.sample.springrest.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.springrest.model.Person;

@RestController
public class HelloController {
	
	@RequestMapping(value="/getperson")
	public Person displayPerson(@ModelAttribute Person person){
		return person;
	}
	
	
	@RequestMapping(value="/sayHello")
	public String displayPerson(){
		return "Hello welcome!!";
	}

}
