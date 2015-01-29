package fr.istic.iodeman.stategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import fr.istic.iodeman.dao.PersonDAO;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.resolver.PersonResolver;
import fr.istic.iodeman.strategy.ParticipantsExcelImport;
import fr.istic.iodeman.strategy.ParticipantsImport;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestParticipantsExcelImport extends AbstractSpringUnitTest{

	@Autowired
	private PersonDAO personDAO;
	
	private class PersonResolverMock implements PersonResolver {

		public Person resolve(String email) {
			Person person = personDAO.findByEmail(email);

			if (person == null) {
				person = new Person();
				person.setEmail(email);
				personDAO.persist(person);
			}

			return person;
		}
	}

	@Test
	public void testExecute() throws Exception{
		ParticipantsImport particpantsImport = new ParticipantsExcelImport();
		String filename = "/import_couple.xls";

		File excelFile = new File(getClass().getResource(filename).toURI());
		assertTrue(excelFile.exists());

		particpantsImport.configure(new PersonResolverMock());
		List<Participant> participants = Lists.newArrayList(particpantsImport.execute(excelFile));

		assertTrue(participants != null);
		assertTrue(participants.size() > 0);
		assertTrue(participants.size() == 6);

		// first one
		Participant firstOne = participants.get(0);
		assertEquals(firstOne.getStudent().getEmail(), "corentin.clement@etudiant.univ-rennes1.fr");
		assertEquals(firstOne.getFollowingTeacher().getEmail(), "didier.certain@univ-rennes1.fr");

		// lastone
		Participant lastOne = participants.get(participants.size() - 1);
		assertEquals(lastOne.getStudent().getEmail(), "maxime.bourdel@etudiant.univ-rennes1.fr");
		assertEquals(lastOne.getFollowingTeacher().getEmail(), "gilles.lesventes@univ-rennes1.fr");


	}
}
