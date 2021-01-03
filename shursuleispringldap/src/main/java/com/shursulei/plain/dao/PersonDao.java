package com.shursulei.plain.dao;

import java.util.List;

import com.shursulei.plain.domain.Person;

public interface PersonDao {
	   void create(Person person);

	   void update(Person person);

	   void delete(Person person);

	   List<String> getAllPersonNames();
	   
	   List<Person> findAll();

	   Person findByPrimaryKey(String country, String company, String fullname);
}
