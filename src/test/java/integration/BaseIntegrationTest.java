package integration;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.ArrayList;
import java.util.List;

import launch.Grizz;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseIntegrationTest {
    public final String link = "http://localhost:8081/";
    private List<WebDriver> drivers = new ArrayList<>();

    protected WebDriver createDriver(){
        var driver = new ChromeDriver(); 
        this.drivers.add(driver);
        return driver;
    }

    static boolean doIntegration(){
        return System.getProperty("skip-integration") == null;
    }
    @BeforeEach()
    void beforeEachIntegration(){
        assumeTrue(doIntegration());
    }

    @AfterEach()
    void afterEachIntegration(){
        for (var driver: this.drivers){
            try {
                driver.close();
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        drivers.clear();
    }
}
