package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestApplicationStep extends BaseIntegrationTest {
	protected WebDriver driver2;
    
    protected List<WebElement> tableRows;
    protected List<WebElement> tableData;
    protected List<String> mortgageIdStr;
    protected List<String> mortgageState;
    protected List<String> mortgageStatus;
    
    
    protected List<WebElement> viewButtons;
    protected List<WebElement> deleteButtons;
    
    private int lastestID = -1;
    private int indexLID = -1;
    
    @BeforeEach
    public void setUpdriver2(){
    	//create application form
    	WebDriver driver = createDriver();
        driver.get(link+"#login");
        WebElement username = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.name("logIn"));
        username.sendKeys("bram@otte.biz");
        password.sendKeys("password");
        login.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("homeScreenLoggedIn")));
        driver.findElement(By.id("toApplicationForm")).click();
        WebElement applicationForm =  driver.findElement(By.tagName("form"));
        assertTrue(applicationForm.isDisplayed());
        
        WebElement grossIncomeInput = applicationForm.findElement(By.id("grossIncome"));
        grossIncomeInput.sendKeys("100000");

        //click all the nextButtons to go to the last application page
        List<WebElement> nextButtons = driver.findElements(By.className("next"));
        nextButtons.get(0).click();
        nextButtons.get(1).click();
        nextButtons.get(2).click();
        WebElement submitButton = driver.findElement(By.className("submit"));
        submitButton.click();
        driver.close();
        
        //login as SP
        this.mortgageIdStr = new ArrayList<String>();
        this.mortgageState = new ArrayList<String>();
        this.mortgageStatus = new ArrayList<String>();
        this.viewButtons = new ArrayList<WebElement>();
        this.deleteButtons = new ArrayList<WebElement>();
    	
        this.driver2 = createDriver();
        this.driver2.get(link+"#login");
        username = this.driver2.findElement(By.id("email"));
        password = this.driver2.findElement(By.id("password"));
        login = this.driver2.findElement(By.name("logIn"));
        username.sendKeys("staff@topicus.nl");
        password.sendKeys("admin");
        login.click();

        wait = new WebDriverWait(driver2, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SPTable")));  
        WebElement table = this.driver2.findElement(By.id("SPTable"));
        this.tableData = table.findElements(By.tagName("td"));
        
        for(int i=0; i<this.tableData.size(); i++){
            if(i%7 == 0){
                String data = this.tableData.get(i).getText();
                this.mortgageIdStr.add(data);
                if (Integer.parseInt(data) > lastestID) {
            		lastestID = Integer.parseInt(data);
            		indexLID = i/7;
            	}
            }
            if(i%7==6){
                this.viewButtons.add(this.tableData.get(i).findElement(By.id("viewButton")));
                this.deleteButtons.add(this.tableData.get(i).findElement(By.id("deleteButton")));
            }
            if (i%7==4) {
            	this.mortgageState.add(this.tableData.get(i).getText());
            }
            if (i%7==5) {
            	this.mortgageStatus.add(this.tableData.get(i).getText());
            }
        }

    }

    @Test
    public void testApplicationStep() {

        //------------------------Test application Step for SP---------------------------------------
    	assertEquals("Application", this.mortgageState.get(indexLID));
    	assertEquals("Processing", this.mortgageStatus.get(indexLID));
        // Test view button:
        this.viewButtons.get(indexLID).click();
        String expectedUrl =link + "#tracktraceSP?id=" + this.mortgageIdStr.get(indexLID);
        String actualUrl = this.driver2.getCurrentUrl();

        assertEquals(expectedUrl,actualUrl);
        driver2.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
        driver2.close();

        //-------------------------Test application step for Customer--------------------------------

        WebDriver driver3 = createDriver();
        mortgageIdStr = new ArrayList<String>();
        mortgageState = new ArrayList<String>();
        mortgageStatus = new ArrayList<String>();
        viewButtons = new ArrayList<WebElement>();
        deleteButtons = new ArrayList<WebElement>();

        driver3.get(link + "#login");
        WebElement username = driver3.findElement(By.id("email"));
        WebElement password = driver3.findElement(By.id("password"));
        WebElement login = driver3.findElement(By.name("logIn"));
        username.sendKeys("bram@otte.biz");
        password.sendKeys("password");
        login.click();

        WebDriverWait wait1 = new WebDriverWait(driver3, 15);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("homeScreenLoggedIn")));

        driver3.findElement(By.id("myMortgagePage")).click();

        WebDriverWait wait2 = new WebDriverWait(driver3, 10);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("mortgage-table")));

        WebElement table = driver3.findElement(By.id("mortgage-table"));
        this.tableData = table.findElements(By.tagName("td"));

        int latestID = -1;
        for(int i=0; i<this.tableData.size(); i++){
            if(i%6 == 0){
                String data = this.tableData.get(i).getText();
                this.mortgageIdStr.add(data);
                if(Integer.parseInt(data) > latestID){
                    latestID = Integer.parseInt(data);
                    indexLID = i/6;
                }
            }
            if(i%6==5){
                this.viewButtons.add(this.tableData.get(i).findElement(By.id("viewButton")));
                this.deleteButtons.add(this.tableData.get(i).findElement(By.id("deleteButton")));
            }
            if(i%6==3){
            	mortgageState.add(this.tableData.get(i).getText());
            }
            if(i%6 == 4){
            	mortgageStatus.add(this.tableData.get(i).getText());
            }
        }

        this.viewButtons.get(indexLID).click();
        String expectedUrl1 =link + "#tracktrace?id=" + this.mortgageIdStr.get(indexLID);
        String actualUrl1 = driver3.getCurrentUrl();
        assertEquals(expectedUrl1,actualUrl1);

        WebDriverWait wait = new WebDriverWait(driver3, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("applicationButton")));

        WebElement applicationButton = driver3.findElement(By.id("applicationButton"));
        assertTrue(applicationButton.isDisplayed());

        applicationButton.click();

        String expectedUrl2 = link + "#applicationPreview?m_id=" + this.mortgageIdStr.get(indexLID);
        String actualUrl2 = driver3.getCurrentUrl();
        assertEquals(expectedUrl2, actualUrl2);

        WebDriverWait wait3 = new WebDriverWait(driver3, 10);
        wait3.until(ExpectedConditions.visibilityOfElementLocated(By.id("applicationHeading")));

        assertTrue(driver3.findElement(By.id("applicationHeading")).isDisplayed());
        
    }

}
