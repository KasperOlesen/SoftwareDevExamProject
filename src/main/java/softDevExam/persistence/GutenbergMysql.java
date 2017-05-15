package softDevExam.persistence;

import javax.ejb.Stateless;

import softDevExam.controller.GutenbergService;

@Stateless
public class GutenbergMysql implements GutenbergService {

	public GutenbergMysql() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getBooksByCity(String city) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCitiesByBook(String book) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getBooksAndCitysByAuthor(String author) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getBooksByLocation(String location) {
		// TODO Auto-generated method stub
		return "";
	}

}
