package softDevExam.controller;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import softDevExam.persistence.GutenbergMysql;
import softDevExam.persistence.GutenbergNeo4J;

public class GutenbergController {

	private GutenbergService service;

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
			return Response.status(Status.OK).entity(service.getBooksByCity(city));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage());
		}
	}

	public ResponseBuilder getCitiesByBook(String book) {
		try {
			return Response.status(Status.OK).entity(service.getCitiesByBook(book));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage());
		}

	}

	public ResponseBuilder getBooksAndCitysByAuthor(String author) {
		try {
			return Response.status(Status.OK).entity(service.getBooksAndCitysByAuthor(author));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage());
		}

	}

	public ResponseBuilder getBooksByLocation(double lat, double lng) {
		try {
			return Response.status(Status.OK).entity(service.getBooksByLocation(lat, lng));
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage());
		}
	}

}
