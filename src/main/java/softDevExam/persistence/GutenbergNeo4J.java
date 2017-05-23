package softDevExam.persistence;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateless;

import softDevExam.controller.GutenbergService;
import softDevExam.entity.Book;
import softDevExam.entity.City;

public class GutenbergNeo4J implements GutenbergService {

	@Override
	public List<Book> getBooksByCity(String city) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<City> getCitiesByBook(String book) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getBooksAndCitysByAuthor(String author) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getBooksByLocation(double latitude, double longitude) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
