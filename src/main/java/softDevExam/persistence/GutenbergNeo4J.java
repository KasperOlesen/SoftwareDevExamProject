package softDevExam.persistence;

import java.util.List;

import javax.ejb.Stateless;

import softDevExam.controller.GutenbergService;
import softDevExam.entity.Book;

public class GutenbergNeo4J implements GutenbergService {

	

	@Override
	public List<Book> getBooksByCity(String city) {
		// TODO Auto-generated method stub
		return null;
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
