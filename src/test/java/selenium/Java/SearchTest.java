import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
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

    private static final int WAIT_MAX = 10;
    static WebDriver driver;

    @BeforeClass
    public static void setup() {
        /*########################### IMPORTANT ######################*/
        /*## Change this, according to your own OS and location of driver(s) ##*/
        /*############################################################*/
        System.setProperty("webdriver.gecko.driver", "C:\\drivers\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver.exe");

        //Reset DB function
        /*com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");*/
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    @Test
    //Verify that page is loaded and that all div elements are loaded
    public void test1() throws Exception {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("mainDiv"));
            List<WebElement> childs = e.findElements(By.xpath(".//*"));
            Assert.assertThat(childs.size(), is(4));
            return true;
        });
    }

    @Test
    //Verify that textinput is available when checkbox (checked: true)
    public void test2() throws Exception {
        WebElement element = driver.findElement(By.id("boxLocation"));
        element.click();
        WebElement fieldName = driver.findElement(By.id("location"));
        fieldName.sendKeys("abc");
        String fieldNameVal = fieldName.getAttribute("value");
        Assert.assertThat(fieldNameVal.equals("abc"));
    }

    @Test
    //Verify that input is cleared when checkbox (checked: false)
    public void test3() throws Exception {
        WebElement element = driver.findElement(By.id("boxAuthor"));
        element.click();
        WebElement fieldName = driver.findElement(By.id("location"));
        String fieldNameVal = fieldName.getAttribute("value");
        Assert.assertThat(fieldNameVal.equals(""));
    }

    @Test
    //Verify that the returned result is not empty
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
            Assert.assertThat(rows, is(not(empty())));
            return true;
        }); 
    }

    @Test
    //Switching DB, test is equal to test4()
    public void test5() throws Exception {
        tearDown();
        setup();
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement db = driver.findElement(By.id("DBswitch"));
            db.click();
            WebElement element = driver.findElement(By.id("boxLocation"));
            element.click();
            WebElement fieldName = driver.findElement(By.id("location"));
            fieldName.sendKeys("paris");
            WebElement submit = driver.findElement(By.id("searchBtn"));
            submit.click();

            (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver dd) -> {
                WebElement result = dd.findElement(By.id("resultList"));
                List<WebElement> rows = result.findElements(By.tagName("li"));
                Assert.assertThat(rows, is(not(empty())));
                return true;
            }); 
            return true;
        });
    }
}