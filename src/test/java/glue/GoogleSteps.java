package glue;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertTrue;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GoogleSteps {

    private WebDriver driver;
    private WebDriverWait wait;
    private double timeTaken;
    private int resultsCount;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("url {string} is launched")
    public void url(String url) {
        driver.get(url);
        acceptCookiesIfWarned();
    }

    private void acceptCookiesIfWarned() {
        try {
            WebElement acceptCookiesButton = driver.findElement(By.cssSelector("#L2AGLb"));
            acceptCookiesButton.click();
        } catch (NoSuchElementException ignored) {
        }
    }

    @When("About page is shown")
    public void about_page_is_shown() {
        WebElement aboutLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='About']")));
        aboutLink.click();
    }

    @Then("page displays {string}")
    public void page_displays(String expectedText) {
        WebElement missionStatement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Our mission')]")));
        assertTrue("The mission statement does not contain the expected text.", missionStatement.getText().contains(expectedText));
    }

    @When("searching for {string}")
    public void searching_for(String query) {
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys(query);
        searchBox.submit();
    }

    @Then("results contain {string}")
    public void results_contain(String expectedText) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search")));
        List<WebElement> results = driver.findElements(By.cssSelector("h3"));

        boolean found = false;
        for (WebElement result : results) {
            if (result.getText().contains(expectedText)) {
                found = true;
                break;
            }
        }
        assertTrue("The search results do not contain the expected text: " + expectedText, found);
    }



    @Then("result stats are displayed")
    public void result_stats_are_displayed() {
        try {
            // Click the "Tools" button if needed
            WebElement toolsButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("hdtb-tls")));
            toolsButton.click();
    
            // Wait for the result stats to be visible
            WebElement resultStats = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("result-stats")));
            
            // Assert that result stats element is displayed
            assertTrue("Result stats are not displayed.", resultStats.isDisplayed());
    
        } catch (NoSuchElementException e) {
            assertTrue("Tools button or Result stats element not found.", false);
        } catch (TimeoutException e) {
            assertTrue("Timed out waiting for result stats to be visible.", false);
        }
    }
    

    @Then("number of {string} is more than {int}")
    public void number_of_is_more_than(String type, int threshold) {
        try {
            // Wait for the result stats to be visible
            WebElement resultStats = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("result-stats")));
            String statsText = resultStats.getText();
    
            if (type.equals("results")) {
                // Extract and parse the results count
                String resultsCountStr = statsText.split(" ")[1].replace(",", ""); // Remove commas
                int resultsCount = Integer.parseInt(resultsCountStr);
    
                // Assert that the number of results is greater than the threshold
                assertTrue("Number of results is not greater than " + threshold + ". Actual: " + resultsCount, resultsCount > threshold);
    
            } else if (type.equals("seconds")) {
                // Extract and parse the time taken
                String timeStr = statsText.split("\\(")[1].split(" ")[0]; // Extract the time in seconds
                double seconds = Double.parseDouble(timeStr);
    
                // Assert that the time taken is greater than the threshold
                assertTrue("Time taken is not greater than " + threshold + " seconds. Actual: " + seconds, seconds > threshold);
            }
    
        } catch (Exception e) {
            assertTrue("Failed to parse the stats for type: " + type, false);
        }
    }
    
}
