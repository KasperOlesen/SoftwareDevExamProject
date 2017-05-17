package selenium.Java;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author kAlex
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SearchTest {

    private static final int WAIT_MAX = 4;
    private static WebDriver driver;

    @BeforeClass
    public static void setup() {
        /* ########################### IMPORTANT ###################### */
 /*
		 * ## Change this, according to your own OS and location of driver(s) ##
         */
 /* ############################################################ */
        System.setProperty("webdriver.gecko.driver", "docs/test/testdata/geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "docs/test/testdata/chromedriver.exe");
        driver = new ChromeDriver(); 
        driver.get("https://limitless-oasis-66630.herokuapp.com/");

        // Reset DB function
        /*
		 * com.jayway.restassured.RestAssured.given().get(
		 * "http://localhost:3000/reset");
		 * 
		 * driver = new ChromeDriver(); driver.get("http://localhost:3000");
         */
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    @Test
    // Verify that page is loaded and that all input elements are loaded
    public void test1() throws Exception {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.id("mainDiv"));
            List<WebElement> childs = e.findElements(By.xpath("//input[@type='text']"));
            assertThat(childs.size(), is(5));
            return true;
            
        });
    }

    @Test
    // Verify that textinput is available when checkbox (checked: true)
    public void test2() throws Exception {
        WebElement element = driver.findElement(By.id("boxLocation"));
        element.click();
        WebElement fieldName = driver.findElement(By.id("location"));
        fieldName.sendKeys("abc");
        String fieldNameVal = fieldName.getAttribute("value");
        assertThat(fieldNameVal, is("abc"));
    }

    @Test
    // Verify that input is cleared when checkbox (checked: false)
    public void test3() throws Exception {
        WebElement element = driver.findElement(By.id("boxAuthor"));
        element.click();
        WebElement fieldName = driver.findElement(By.id("location"));
        String fieldNameVal = fieldName.getAttribute("value");
        assertThat(fieldNameVal, is(""));
    }

    @Test
    // Verify that the returned result is not empty
    public void test4() throws Exception {
        WebElement element = driver.findElement(By.id("boxLocation"));
        element.click();
        WebElement fieldName = driver.findElement(By.id("location"));
        fieldName.sendKeys("paris");
        WebElement submit = driver.findElement(By.id("searchBtn"));
        submit.click();

        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement result = d.findElement(By.id("resultList"));
            List<WebElement> rows = result.findElements(By.tagName("li"));
            assertThat(rows, is(not(rows.isEmpty())));
            return true;
        });
    }

    @Test
    @Ignore
    // Switching DB, test is equal to test4()
    public void test5() throws Exception {
        WebElement db = driver.findElement(By.className("dbDiv"));
        db.click();
        WebElement element = driver.findElement(By.id("boxLocation"));
        element.click();
        WebElement fieldName = driver.findElement(By.id("location"));
        fieldName.sendKeys("paris");
        WebElement submit = driver.findElement(By.id("searchBtn"));
        submit.click();

        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement result = d.findElement(By.id("resultList"));
            List<WebElement> rows = result.findElements(By.tagName("li"));
            assertThat(rows, is(not(rows.isEmpty())));
            return true;
        });
    }
}
