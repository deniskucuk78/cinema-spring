 package fr.gtm.cinema.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.gtm.cinema.bo.MailReceptor;
import fr.gtm.cinema.dao.ActeurDao;
import fr.gtm.cinema.dao.FilmDao;
import fr.gtm.cinema.entities.Acteur;
import fr.gtm.cinema.entities.ActeurDTO;
import fr.gtm.cinema.entities.Film;
import fr.gtm.cinema.entities.FilmDTO;
import fr.gtm.cinema.entities.Role;

@RestController
@RequestMapping("/cinema")
public class CinemaRestController {
	
	@Autowired private ActeurDao acteurDao;
	@Autowired private FilmDao filmDao;
	@Autowired private JavaMailSender mailSender;
	public final static Logger LOG = Logger.getLogger("foo");
	
	@GetMapping(path="/acteur/{id}")
	public ActeurDTO findActeurById(@PathVariable("id") long id) {
		Optional<Acteur> optional = acteurDao.findById(id);
		if(optional.isPresent())
		{
			return new ActeurDTO(optional.get());
		}
		
		return null;
	}
	
	@PostMapping(path="/creerActeur")
	public String creerActeur(@RequestBody ActeurDTO dto) {
		
		Acteur acteur = dto.toActeur();
		acteurDao.save(acteur);
		return "ok";
		
	}
	
	@PostMapping("/mail/send")
	public void testMail(@RequestBody MailReceptor mr) {
		try {
			new Thread(()->sendEmail(mr)).start();   //Expression Lambda
        
		} catch(Exception e) {
			LOG.severe(e.getMessage());
			
		}
		
	}
	
	private void sendEmail(MailReceptor mr) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(mr.getTo());
		mail.setFrom("gaston@bovoyage.net");
		
		mail.setSubject("Testing from Spring Boot");
        mail.setText(mr.getText());
        
        mailSender.send(mail);
	}
	
	@GetMapping("/films")
	public List<FilmDTO> getAllFilms() {
		List<FilmDTO> filmsDto = new ArrayList<>();
		
		List<Film> films = filmDao.findAll();
		
		for(Film f : films) {
			filmsDto.add(new FilmDTO(f));
		}
		
		return filmsDto;
		
	}
	
	@GetMapping("/films2")
	public List<Film> getAllFilms2() {
		
		List<Film> films = filmDao.findAll();
		return films;
		
	}
	
	
	
	@GetMapping("/film/role/{id}")
	public List<ActeurDTO> getRolesByFilmId(@PathVariable("id") long id) {
		
		List<ActeurDTO> acteursDto = new ArrayList<>();
		Film films = filmDao.getFilmWithActorById(id);
		
		List<Acteur> acteurs = films.getActeurs();
		
		for(Acteur a : acteurs) {
			acteursDto.add(new ActeurDTO(a));
		}
		
		return acteursDto;
		
		
	}
	
	@GetMapping("/film/role2/{id}")
	public List<Acteur> getRoles2ByFilmId(@PathVariable("id") long id) {
		

		Film films = filmDao.getFilmWithActorById(id);
		
		List<Acteur> acteurs = films.getActeurs();
		
		return acteurs;
		
		
	}
	
	@PostMapping("/film/ajout")
	public String ajoutFilm(@RequestBody FilmDTO filmdto) {
		//filmDao.save(filmdto.toFilm());
		Film film = filmdto.toFilm();
		
		List<Film> filmsBase = filmDao.findAll();
		
		for(Film f : filmsBase)
		{
			if(f.getId()== film.getId() )
			{
				f.setTitre(film.getTitre());
				f.setRealisateur(film.getRealisateur());
				f.setDateSortie(film.getDateSortie());
				f.setDuree(film.getDuree());
				f.setPrixHT(film.getPrixHT());
				
				filmDao.save(f);
				
				return "film existant déjà donc MàJ";
			}

		}
		filmDao.save(film);
		return "film ajouté";
	
	}
	
	@PostMapping("/film/ajout2")
	public String ajoutFilm2(@RequestBody FilmDTO filmdto) {
		//filmDao.save(filmdto.toFilm());
		Film film = filmdto.toFilm();
		
		List<Film> filmsBase = filmDao.findAll();
		
		for(Film f : filmsBase)
		{
			if(f.getId()== film.getId() )
			{

				return "film existant déjà";
			}

		}
		filmDao.save(film);
		return "film ajouté";
	
	}
	
	

	
	
	

	
	
	
	

}
