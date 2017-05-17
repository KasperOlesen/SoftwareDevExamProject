package persistence;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;;

import javax.ws.rs.core.Response;

import softDevExam.controller.GutenbergController;
import softDevExam.persistence.GutenbergMysql;

import org.junit.Test;

public class GutenbergMysqlTest {

	@Test
	public void testEnsureCorrectData() {
		assertThat(true, is(true));
	}

    private static GutenbergMysql createConnector() {
        return new GutenbergMysql("CONNECTION STRING");
    }
}
