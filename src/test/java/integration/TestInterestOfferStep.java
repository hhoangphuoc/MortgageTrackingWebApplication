package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TestInterestOfferStep extends BaseIntegrationTest{

    protected List<WebElement> tableRows;
    protected List<WebElement> tableData;
    protected List<String> mortgageIdStr;
    protected List<String> mortgageState;
    protected List<String> mortgageStatus;
    
    
    protected List<WebElement> viewButtons;
    protected List<WebElement> deleteButtons;
    
    private int lastestID = -1;
    private int indexLID = -1;
    WebDriver spDriver;

    @BeforeEach
    public void setUp(){
        // login and create application form
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
    	
        spDriver = new ChromeDriver();
        spDriver.get(link+"#login");
        username = spDriver.findElement(By.id("email"));
        password = spDriver.findElement(By.id("password"));
        login = spDriver.findElement(By.name("logIn"));
        username.sendKeys("staff@topicus.nl");
        password.sendKeys("admin");
        login.click();

        wait = new WebDriverWait(spDriver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SPTable")));  
        WebElement table = spDriver.findElement(By.id("SPTable"));
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
        
    	assertEquals("Application", this.mortgageState.get(indexLID));
    	assertEquals("Processing", this.mortgageStatus.get(indexLID));
        // Test view button:
        this.viewButtons.get(indexLID).click();
        String expectedUrl =link + "#tracktraceSP?id=" + this.mortgageIdStr.get(indexLID);
        String actualUrl = spDriver.getCurrentUrl();

        assertEquals(expectedUrl,actualUrl);
        

        WebDriverWait wait2 = new WebDriverWait(spDriver, 10);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("moveStep")));
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("show-application")));

        //move step to the correct state:
        WebElement nextStepButton = spDriver.findElement(By.id("moveStep"));
		nextStepButton.click();
		wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkButton")));

		nextStepButton.click();

        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("interestOfferButton")));

    }

    @Test
    public void testInterestOffer(){

        //------------------------TEST FOR SERVICE PROVIDER VIEW:----------------------------------------------

        //Check the view Interest Offer button is showed:
        WebElement interestOfferButton = this.spDriver.findElement(By.id("interestOfferButton"));
        assertTrue(interestOfferButton.isDisplayed());

        interestOfferButton.click();

        WebDriverWait wait1 = new WebDriverWait(this.spDriver, 10);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("interest-content")));

        //check the text box showed up after click the button:
        WebElement interestInfoBox = this.spDriver.findElement(By.id("interest-content"));

        WebElement duration = interestInfoBox.findElement(By.id("duration"));
        assertTrue(duration.isDisplayed());

        WebElement offer = interestInfoBox.findElement(By.id("offer"));
        assertTrue(offer.isDisplayed());


        //close the driver:
        spDriver.close();

        //-------------------------TEST FOR CUSTOMER VIEW -----------------------------------------------------------

        //create new driver for customer:
        WebDriver customerDriver = createDriver();
        mortgageIdStr = new ArrayList<String>();
        mortgageState = new ArrayList<String>();
        mortgageStatus = new ArrayList<String>();
        viewButtons = new ArrayList<WebElement>();
        deleteButtons = new ArrayList<WebElement>();

        //login as customer account:
        customerDriver.get(link + "#login");
        WebElement username = customerDriver.findElement(By.id("email"));
        WebElement password = customerDriver.findElement(By.id("password"));
        WebElement login = customerDriver.findElement(By.name("logIn"));
        username.sendKeys("bram@otte.biz");
        password.sendKeys("password");
        login.click();

        //direct to myMortgage page
        WebDriverWait wait2 = new WebDriverWait(customerDriver, 10);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("homeScreenLoggedIn")));
        customerDriver.findElement(By.id("myMortgagePage")).click();

        //wait until the page is load
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("mortgage-table")));

        //get the mortgageId, status and state from mortgage table:
        WebElement table = customerDriver.findElement(By.id("mortgage-table"));
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

        //check process status  and state  of the mortgage:
        assertEquals("Interest Offer", mortgageState.get(indexLID));
        assertEquals("Action Required", mortgageStatus.get(indexLID));


        // check the path and click the view button:

        this.viewButtons.get(indexLID).click();
        String expectedUrl1 =link + "#tracktrace?id=" + this.mortgageIdStr.get(indexLID);
        String actualUrl1 = customerDriver.getCurrentUrl();
        assertEquals(expectedUrl1,actualUrl1);

        //wait till the correct page is loaded
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("interestOfferButton")));

    
        //Check the View Interest Button:
        WebElement customerOfferButton = customerDriver.findElement(By.id("interestOfferButton"));
        assertTrue(customerOfferButton.isDisplayed());

        //Check the interest information box is showed after click
        customerOfferButton.click();

        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("interest-content")));

        
        //test choose another option button:
        WebElement chooseDurationButton = customerDriver.findElement(By.id("chooseDurationButton"));
        assertTrue(chooseDurationButton.isDisplayed());

        chooseDurationButton.click();

        //add duration to ensure the process is continue:
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("durationChanged")));


        Select chooseDurationSelection = new Select(customerDriver.findElement(By.id("duration-select")));

        chooseDurationSelection.selectByVisibleText("10 years");

        //test confirm duration button:
        WebElement confirmButton = customerDriver.findElement(By.id("confirmInterest"));
        assertTrue(confirmButton.isDisplayed());

        confirmButton.click();

    }
    @AfterEach
    public void terminate(){
        
    }

    
}
