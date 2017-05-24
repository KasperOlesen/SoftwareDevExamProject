package softDevExam.controller;

import java.util.List;

import softDevExam.entity.Book;
import softDevExam.entity.City;

public interface GutenbergService {

	List<Book> getBooksByCity(String city) throws Exception;

	List<City> getCitiesByBook(String book) throws Exception;

	List<Book> getBooksAndCitysByAuthor(String author) throws Exception;

	List<Book> getBooksByLocation(double longitude, double latitude) throws Exception;

}
