package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestApplyApplicationForm extends BaseIntegrationTest {
    protected WebDriver driver;
	
    @BeforeEach
    public void setUpLogin(){
        this.driver = createDriver();
        
        this.driver.get(link+"#login");
        WebElement username = this.driver.findElement(By.id("email"));
        WebElement password = this.driver.findElement(By.id("password"));
        WebElement login = this.driver.findElement(By.name("logIn"));
        username.sendKeys("bram@otte.biz");
        password.sendKeys("password");
        login.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("homeScreenLoggedIn")));

        this.driver.findElement(By.id("toApplicationForm")).click();
    }
    @Test
    public void testFormExist(){
        WebElement applicationForm =  this.driver.findElement(By.tagName("form"));
        assertTrue(applicationForm.isDisplayed());
    }

    @Test
    public void testFormInput(){
        WebElement applicationForm =  this.driver.findElement(By.tagName("form"));
        assertTrue(applicationForm.isDisplayed());
        
        WebElement grossIncomeInput = applicationForm.findElement(By.id("grossIncome"));
        grossIncomeInput.sendKeys("100000");

    }
    @Test
    public void testNextAndPreviousButton(){
        List<WebElement> nextButtons = this.driver.findElements(By.className("next"));
        List<WebElement> prevButtons = this.driver.findElements(By.className("prev"));
        //ensure the prev button exist:
        assertTrue(prevButtons.size()!= 0);
        assertTrue(nextButtons.size()!= 0);

        // ensure the form has total 3 prev button:
        assertEquals(3, prevButtons.size());
        assertEquals(3, nextButtons.size());

        // ensure the first next button will go to the second application page after click
        nextButtons.get(0).click();
        assertEquals(link+"#application#2",this.driver.getCurrentUrl());

        // ensure the first previous button will go back to the first application page after click
        prevButtons.get(0).click();
        assertEquals(link + "#application#1",this.driver.getCurrentUrl());

    }
    @Test
    public void testSubmitButton(){


        WebElement applicationForm =  this.driver.findElement(By.tagName("form"));
        assertTrue(applicationForm.isDisplayed());
        
        WebElement grossIncomeInput = applicationForm.findElement(By.id("grossIncome"));
        grossIncomeInput.sendKeys("100000");

        //click all the nextButtons to go to the last application page
        List<WebElement> nextButtons = this.driver.findElements(By.className("next"));
        nextButtons.get(0).click();
        nextButtons.get(1).click();
        nextButtons.get(2).click();



        WebElement submitButton = this.driver.findElement(By.className("submit"));
        submitButton.click();
        //make sure the submit button will go back to home page:
        assertEquals(link + "#application#4",this.driver.getCurrentUrl());


    }
    @Test
    public void testProgressBar(){

    }
}
