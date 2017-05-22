package softDevExam.controller;

import java.util.List;

import softDevExam.entity.Book;

public interface GutenbergService {

	List<Book> getBooksByCity(String city);

	String getCitiesByBook(String book);

	String getBooksAndCitysByAuthor(String author);

	String getBooksByLocation(String location);

}
