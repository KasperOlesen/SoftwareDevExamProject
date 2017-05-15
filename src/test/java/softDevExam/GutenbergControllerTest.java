package softDevExam;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import softDevExam.controller.GutenbergController;
import softDevExam.persistence.GutenbergMysql;

public class GutenbergControllerTest {

	@Mock
	private GutenbergMysql mysqlService;

	@InjectMocks
	private GutenbergController controller = new GutenbergController();

	private String city = "Ajax";
	private String book = "The Bible, King James Version, Complete";
	private String author = "Frederick Douglass";
	private String location = "123123";

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetBooksByCityReturnBook() {
		when(mysqlService.getBooksByCity(anyString())).thenReturn(book);

		Response result = controller.getBooksByCity(city).build();
		assertThat(result.getEntity(), is(equalTo(book)));
	}

	@Test
	public void testGetCitiesByBookReturnCities() {
		when(mysqlService.getCitiesByBook(anyString())).thenReturn(city);

		Response result = controller.getCitiesByBook(book).build();
		assertThat(result.getEntity(), is(equalTo(city)));
	}

	@Test
	public void testGetBooksAndCitysByAuthor() {
		when(mysqlService.getBooksAndCitysByAuthor(author)).thenReturn(city);

		Response result = controller.getBooksAndCitysByAuthor(author).build();
		assertThat(result.getEntity(), is(equalTo(city)));
	}

	@Test
	public void testGetBooksByLocation() {
		when(mysqlService.getBooksByLocation(location)).thenReturn(book);

		Response result = controller.getBooksByLocation(location).build();
		assertThat(result.getEntity(), is(equalTo(book)));
	}

}
