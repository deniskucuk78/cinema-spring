package fr.gtm.contacts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import fr.gtm.cinema.dao.FilmDao;
import fr.gtm.cinema.entities.Acteur;
import fr.gtm.cinema.entities.Film;
import fr.gtm.cinema.entities.Role;


@DataJpaTest
class CinemaSpringApplicationTests {
	
	@Autowired FilmDao dao;

	@Test
	void saveFilm() {
		
		Film film1 = new Film("la guerre des étoiles", 120,  25.50);
		Film film2 = new Film("Les pompiers", 145, 25.50);
		dao.save(film1);
		dao.save(film2);
		List<Film> films = new ArrayList<>();
		films = dao.findAll();
		assertEquals(2, films.size());
		
	}
	
	
	
	@Test
	void getActeurs() {
		Film film1 = new Film("Les pompiers", 120,  25.50);
		Acteur acteur1 = new Acteur("M", "JC", "Fagothey");
		Acteur acteur2 = new Acteur("M", "Johny", "Deep");
		Role role1 = new Role("freulon");
		Role role2 = new Role("Victime on fire n°1");
		
		film1.addRoles(role1, acteur1);
		film1.addRoles(role2, acteur2);
		assertEquals(2, film1.getRoles().size());
		
		dao.save(film1);
		
		List<Film> films = dao.findAll();
		assertEquals(1, films.size() );
		
		Film film1b = dao.getFilmWithActorById(film1.getId());
		assertEquals(2, film1b.getActeurs().size() );
		
		
	}
	
	
	
	
	
	

}
