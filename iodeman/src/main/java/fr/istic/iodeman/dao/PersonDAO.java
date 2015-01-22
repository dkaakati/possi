package fr.istic.iodeman.dao;

import java.util.List;

import fr.istic.iodeman.model.Person;

public interface PersonDAO {

	public void persist(Person person);
	
	public Person findById(int ID);

	public void delete(Person person);

	public List<Person> findAll();

	public void deleteAll();
}