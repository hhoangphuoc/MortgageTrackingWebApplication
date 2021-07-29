package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


//TODO: only run this with a specific command since it takes really long

public class TestLogin extends BaseIntegrationTest {
	protected WebDriver driver;
    //public final String link = "http://localhost:8080/";
	@BeforeEach
    public void setUpDriver(){
        this.driver = createDriver();
        this.driver.get(link+"#login");
    }

    
    @Test
    public void loginAsSP() {
        WebElement username = this.driver.findElement(By.id("email"));
        WebElement password = this.driver.findElement(By.id("password"));
        WebElement login = this.driver.findElement(By.name("logIn"));
        username.sendKeys("staff@topicus.nl");
        password.sendKeys("admin");
        login.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SPTable")));        
        String expectedUrl =link+"#";
        String actualUrl = this.driver.getCurrentUrl();
        assertEquals(expectedUrl,actualUrl);
    }

    @Test
    public void loginAsCustomer() {
        WebElement username = this.driver.findElement(By.id("email"));
        WebElement password = this.driver.findElement(By.id("password"));
        WebElement login = this.driver.findElement(By.name("logIn"));
        username.sendKeys("bram@otte.biz");
        password.sendKeys("password");
        login.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myMortgagePage")));        

        String expectedUrl =link+"#";
        String actualUrl = this.driver.getCurrentUrl();
        assertEquals(expectedUrl,actualUrl);
    }
    
    @Test
    public void goToRegister(){
        
        WebElement registerRedirect = this.driver.findElement(By.id("goToRegister"));

        registerRedirect.click();
        String expectedUrl= this.driver.getCurrentUrl();
        String actualUrl=link+"#register";
        assertEquals(expectedUrl,actualUrl);
    }

    @Test
    public void loginWithWrongPass() {
        WebElement username = this.driver.findElement(By.id("email"));
        WebElement password = this.driver.findElement(By.id("password"));
        WebElement login = this.driver.findElement(By.name("logIn"));
        
        username.sendKeys("bram@otte.biz");
        password.sendKeys("lala");
        login.click();
        
        String expectedUrl =link+"#login";
        String actualUrl = this.driver.getCurrentUrl();
        assertEquals(expectedUrl,actualUrl);
    }
    
    @Test
    public void loginInvalidAccount() {
        WebElement username = this.driver.findElement(By.id("email"));
        WebElement password = this.driver.findElement(By.id("password"));
        WebElement login = this.driver.findElement(By.name("logIn"));
        
        username.sendKeys("bram@otte.biz");
        password.sendKeys("lala");
        login.click();
        
        String expectedUrl =link+"#login";
        String actualUrl = this.driver.getCurrentUrl();
        assertEquals(expectedUrl,actualUrl);
    }
}

