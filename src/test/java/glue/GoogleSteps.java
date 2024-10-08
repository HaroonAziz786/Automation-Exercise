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
        // Click the "Tools" button
        WebElement toolsButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("hdtb-tls")));
        toolsButton.click();

        // Wait for the result stats to be visible after clicking "Tools"
        WebElement resultStats = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='result-stats']")));

        // Extracting text from result stats (e.g., "About 1,510,000,000 results (0.32 seconds)")
        String statsText = resultStats.getText(); 
        System.out.println("Result Stats Text: " + statsText); // Debug output to see the actual text
        
        // Normalize spaces in the text
        statsText = statsText.replace("\u00A0", " ");

        // Split to extract the number of results (e.g., "1,510,000,000")
        String resultsCountStr = statsText.split(" ")[1].replace(",", "").trim(); // Get second word, remove commas
        int resultsCount = Integer.parseInt(resultsCountStr); // Convert to int
        
        // Save resultsCount for further assertions
        this.resultsCount = resultsCount;

        // Assert that the number of results is greater than 1
        assertTrue("Number of results is not greater than 1. Actual count: " + resultsCount, resultsCount > 1);

        // Split to extract the time (e.g., "0.32")
        String timeStr = statsText.split("\\(")[1].split(" ")[0]; // Get value inside parentheses
        double seconds = Double.parseDouble(timeStr); // Convert to double
        
        // Save timeTaken for further assertions
        this.timeTaken = seconds;

        // Assert that the time taken is greater than 0
        assertTrue("Time taken is not greater than 0 seconds. Actual time: " + seconds, seconds > 0);

    } catch (NoSuchElementException e) {
        assertTrue("Tools button not found.", false);
    } catch (TimeoutException e) {
        assertTrue("Timed out waiting for Tools button to be clickable.", false);
    } catch (NumberFormatException e) {
        assertTrue("Error parsing the results count or time from result stats.", false);
    }
}


    
@Then("number of {string} is more than {int}")
public void number_of_is_more_than(String type, int threshold) {
    if (type.equals("results")) {
        assertTrue("Number of results is not greater than " + threshold, this.resultsCount > threshold);
    } else if (type.equals("seconds")) {
        assertTrue("Time taken is not greater than " + threshold + " seconds. Actual time: " + this.timeTaken, this.timeTaken > threshold);
    }
}
    
}
