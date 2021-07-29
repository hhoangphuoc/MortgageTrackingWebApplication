package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestHomeScreenLoggedIn extends BaseIntegrationTest {
    protected WebDriver driver;
    
    @BeforeEach
    public void setUpLogIn(){
        this.driver = createDriver();
        this.driver.get(link + "#login");
        WebElement username = this.driver.findElement(By.id("email"));
        WebElement password = this.driver.findElement(By.id("password"));
        WebElement login = this.driver.findElement(By.name("logIn"));
        username.sendKeys("bram@otte.biz");
        password.sendKeys("password");
        login.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("homeScreenLoggedIn")));
    }

    @Test
    public void testGoToApplicationForm(){

        

        this.driver.findElement(By.id("toApplicationForm")).click();

        String expectedUrl= this.driver.getCurrentUrl();
        String actualUrl= link +"#application";
        assertEquals(expectedUrl,actualUrl);

        Boolean display = driver.findElement(By.id("app")).isDisplayed();
        assertTrue(display);
    }

    @Test
    public void testGoToMyMortgage(){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myMortgagePage"))); 

        this.driver.findElement(By.id("myMortgagePage")).click();

        String expectedUrl= this.driver.getCurrentUrl();
        String actualUrl=link + "#mymortgage";
        assertEquals(expectedUrl,actualUrl);
        Boolean display = driver.findElement(By.id("title")).isDisplayed();
        assertTrue(display);

    }


}
