package softDevExam.persistence;

import java.util.*;

import softDevExam.controller.GutenbergService;
import softDevExam.entity.Book;

import org.neo4j.driver.v1.*;
import org.neo4j.helpers.collection.MapUtil;

public class GutenbergNeo4J implements GutenbergService {

	private final String url;
	private final AuthToken token;

	public GutenbergNeo4J(String url, AuthToken token) {
		this.url = url;
		this.token = token;
	}

	@Override
	public List<Book> getBooksByCity(String city) throws Exception {
		List<Book> books = new ArrayList<>();

		try (Driver driver = GraphDatabase.driver(this.url, this.token)) {
			try (Session session = driver.session()) {
				String command = "MATCH (c:City {name: $cityName})<-[m:MENTIONS]-(b:Book)<-[r:HAS_WRITTEN]-(a:Author) "
						+ "RETURN b, a";

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("cityName", city);

				StatementResult response = session.run(command, parameters);
				for (Record record : response.list()) {
					books.add(new Book(record.get("id").asString(), record.get("name").asString()));
				}
			}
		}

		return books;
	}

	@Override
	public String getCitiesByBook(String book) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooksAndCitysByAuthor(String author) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooksByLocation(String location) {
		// TODO Auto-generated method stub
		return null;
	}

}
