package softDevExam.controller;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import javax.ws.rs.core.GenericEntity;

import softDevExam.persistence.GutenbergMysql;
import softDevExam.persistence.GutenbergNeo4J;

import java.util.List;

import softDevExam.entity.*;

public class GutenbergController {

	private GutenbergService service;
	private final Gson gson = new Gson();

	public GutenbergController() {
	}

	public GutenbergController(GutenbergMysql service) {
		this.service = service;
	}

	public GutenbergController(GutenbergNeo4J service) {
		this.service = service;
	}

	public ResponseBuilder getBooksByCity(String city) {
		try {
			List<Book> books = service.getBooksByCity(city);

			return Response.status(Status.OK).entity(this.gson.toJson(books));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage());
		}
	}

	public ResponseBuilder getCitiesByBook(String book) {
		try {
			List<City> cities = service.getCitiesByBook(book);

			return Response.status(Status.OK).entity(this.gson.toJson(cities));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage());
		}

	}

	public ResponseBuilder getBooksAndCitysByAuthor(String author) {
		try {
			List<Book> books = service.getBooksAndCitysByAuthor(author);

			return Response.status(Status.OK).entity(this.gson.toJson(books));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage());
		}

	}

	public ResponseBuilder getBooksByLocation(double longitude, double latitude) {
		try {
			List<Book> books = service.getBooksByLocation(longitude, latitude);

			return Response.status(Status.OK).entity(this.gson.toJson(books));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage());
		}
	}

}
