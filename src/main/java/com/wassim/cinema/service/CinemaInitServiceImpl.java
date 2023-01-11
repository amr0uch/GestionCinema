package com.wassim.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wassim.cinema.dao.CategorieRepository;
import com.wassim.cinema.dao.CinemaRepository;
import com.wassim.cinema.dao.FilmRepository;
import com.wassim.cinema.dao.PlaceRepository;
import com.wassim.cinema.dao.ProjectionRepository;
import com.wassim.cinema.dao.SalleRepository;
import com.wassim.cinema.dao.SeanceRepository;
import com.wassim.cinema.dao.TicketRepository;
import com.wassim.cinema.dao.VilleRepository;
import com.wassim.cinema.entities.Categorie;
import com.wassim.cinema.entities.Cinema;
import com.wassim.cinema.entities.Film;
import com.wassim.cinema.entities.Place;
import com.wassim.cinema.entities.Projection;
import com.wassim.cinema.entities.Salle;
import com.wassim.cinema.entities.Seance;
import com.wassim.cinema.entities.Ticket;
import com.wassim.cinema.entities.Ville;

@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService  {
	
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	@Autowired
	private TicketRepository ticketRepository;
	
	
	@Override
	public void initVilles() {
		Stream.of("Casablanca","Marrakesh","Rabat","Tanger").forEach(nameVille->{
			Ville ville = new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
			//villeRepository.save(new Ville(null,v));
		});
		
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v->{
			Stream.of("Megarama","IMAX","FOUNOUN","CHAHRAZAD","DAWLIZ")
			.forEach(nameCinema->{
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0;i<cinema.getNombreSalles();i++) {
				Salle salle= new Salle();
				salle.setName("Salle"+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
		
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i=0;i<salle.getNombrePlace();i++) {
				Place place = new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	public void initSeances() {
			DateFormat dateFormat= new SimpleDateFormat("HH:mm");
				Stream.of("12:30","15:00","17:00","19:00","21:00").forEach(s->{
				Seance seance= new Seance();
				try {
					seance.setHeureDebut(dateFormat.parse(s));
					seanceRepository.save(seance);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		
	}

	@Override
	public void initCategories() {
		Stream.of("Histoire","Fiction","Actions","Drama").forEach(cat->{
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
		
	}

	@Override
	public void initFilms() {
		double[] durees= new double[] {1,1.5,2,2.5,3} ;
		List<Categorie> categories=categorieRepository.findAll();
		Stream.of("Got","Lord of the rings","spider man","Iron man","Catwoman","Godfather").forEach(TitreFilm->{
			Film film=new Film();
			film.setTitre(TitreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(TitreFilm.replaceAll(" ", "")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
		
	}

	@Override
	public void initProjections() {
		double[] prices = new double[] {30,50,60,70,80,90,100} ;
		 villeRepository.findAll().forEach(ville->{
			 ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					filmRepository.findAll().forEach(film->{
						seanceRepository.findAll().forEach(seance->{
							Projection projection = new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
					});
				}) ;
			 });
		 });
		
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				//ticket.setNomClient("");
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
	}

}
