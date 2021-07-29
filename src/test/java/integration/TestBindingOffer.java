package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBindingOffer extends BaseIntegrationTest {
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
        
        //---------------------------------Login as SP -------------------------------------------------------------
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

        //move step to the second state: Documents Check:
        WebElement nextStepButton = spDriver.findElement(By.id("moveStep"));
		nextStepButton.click();
		wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkButton")));

        nextStepButton.click();
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("interestOfferButton")));

        nextStepButton.click();

        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("bindingOfferButton")));
    }

    @Test
    public void testBindingOffer(){
        WebElement bindingOfferButton = this.spDriver.findElement(By.id("bindingOfferButton"));
        assertTrue(bindingOfferButton.isDisplayed());

        bindingOfferButton.click();

        //make sure the go to the binding offer page after click:
        String actualUrl = this.spDriver.getCurrentUrl();
        String expectedUrl = link + "#bindingoffer?m_id=" + this.mortgageIdStr.get(this.indexLID);
        assertEquals(expectedUrl, actualUrl);
        

        //wait for the page loading:
        WebDriverWait wait1 = new WebDriverWait(spDriver, 10);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("binding-page")));

        WebElement bindingPage = this.spDriver.findElement(By.id("binding-page"));
        assertTrue(bindingPage.isDisplayed());

        WebElement downloadButton = this.spDriver.findElement(By.id("downloadButton"));
        assertTrue(downloadButton.isDisplayed());

        //close the driver:
        spDriver.close();

        //--------------------------- TEST FOR CUSTOMER VIEW -----------------------------------------------------------

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
        WebDriverWait wait2 = new WebDriverWait(customerDriver, 15);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("homeScreenLoggedIn")));

        customerDriver.findElement(By.id("myMortgagePage")).click();

        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("mortgage-table")));

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
        assertEquals("Binding Offer", mortgageState.get(indexLID));
        assertEquals("Action Required", mortgageStatus.get(indexLID));

        //direct to the track-trace page:


        this.viewButtons.get(indexLID).click();
        String expectedUrl1 =link + "#tracktrace?id=" + this.mortgageIdStr.get(indexLID);
        String actualUrl1 = customerDriver.getCurrentUrl();
        assertEquals(expectedUrl1,actualUrl1);

        //wait till the correct page is loaded
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("bindingOfferButton")));

        //make sure the binding offer button exist
        WebElement bindingOfferCustomerButton = customerDriver.findElement(By.id("bindingOfferButton"));
        assertTrue(bindingOfferCustomerButton.isDisplayed());

        //direct to bindingOffer page:
        bindingOfferCustomerButton.click();

        //make sure the go to the binding offer page after click:
        String actualUrlCustomer = customerDriver.getCurrentUrl();
        String expectedUrlCustomer = link + "#bindingofferCustomer?m_id=" + mortgageIdStr.get(indexLID);
        assertEquals(expectedUrlCustomer, actualUrlCustomer);

    }
}
