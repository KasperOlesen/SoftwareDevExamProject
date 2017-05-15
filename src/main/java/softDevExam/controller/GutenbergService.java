package softDevExam.controller;

public interface GutenbergService {

	String getBooksByCity(String city);

	String getCitiesByBook(String book);

	String getBooksAndCitysByAuthor(String author);

	String getBooksByLocation(String location);

}
