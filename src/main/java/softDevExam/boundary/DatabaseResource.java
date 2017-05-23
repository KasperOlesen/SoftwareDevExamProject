package softDevExam.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import softDevExam.controller.GutenbergController;

import softDevExam.factories.*;

@Produces(MediaType.APPLICATION_JSON)
@Path("")
public class DatabaseResource {

	private GutenbergControllerFactory factory = new GutenbergControllerFactory();

	@Context
	UriInfo uriInfo;

	@GET
	@Path("{databaseType}/city")
	public Response getBooksByCity(@PathParam("databaseType") String databaseType, @QueryParam("city") String city) {
		GutenbergController controller = factory.create(databaseType);
		return controller.getBooksByCity(city).build();
	}

	@GET
	@Path("{databaseType}/book")
	public Response getCitiesByBook(@PathParam("databaseType") String databaseType, @QueryParam("book") String book) {
		GutenbergController controller = this.factory.create(databaseType);

		return controller.getCitiesByBook(book).build();
	}

	@GET
	@Path("{databaseType}/author")
	public Response getBooksAndCitysByAuthor(@PathParam("databaseType") String databaseType,
			@QueryParam("author") String author) {
		GutenbergController controller = this.factory.create(databaseType);

		return controller.getBooksAndCitysByAuthor(author).build();
	}

	@GET
	@Path("{databaseType}/geo")
	public Response getBooksByLocation(@PathParam("databaseType") String databaseType,
			@QueryParam("latitude") Double latitude, @QueryParam("longitude") Double longitude) {
		GutenbergController controller = this.factory.create(databaseType);

		return controller.getBooksByLocation(longitude.doubleValue(), latitude.doubleValue()).build();
	}

}
