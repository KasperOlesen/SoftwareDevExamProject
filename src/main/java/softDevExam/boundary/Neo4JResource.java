package softDevExam.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import softDevExam.controller.GutenbergController;
import softDevExam.persistence.GutenbergNeo4J;

@Produces(MediaType.APPLICATION_JSON)
@Path("neo4j")
public class Neo4JResource {

	private final GutenbergController controller = new GutenbergController(new GutenbergNeo4J());

	@Context
	UriInfo uriInfo;

	@GET
	@Path("/city/{city}")
	public Response getBooksByCity(@PathParam("city") String city) {
		return controller.getBooksByCity(city).build();
	}

	@GET
	@Path("/book/{book}")
	public Response getCitiessByBook(@PathParam("book") String book) {
		return controller.getCitiesByBook(book).build();
	}

	@GET
	@Path("/author/{author}")
	public Response getBooksAndCitysByAuthor(@PathParam("author") String author) {
		return controller.getBooksByCity(author).build();
	}

	@GET
	@Path("/location/{location}")
	public Response getBooksByLocation(@PathParam("location") String location) {
		return controller.getBooksByCity(location).build();
	}

}
