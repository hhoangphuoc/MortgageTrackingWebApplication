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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestDocumentCheckStep extends BaseIntegrationTest {    
    protected List<WebElement> tableRows;
    protected List<WebElement> tableData;
    protected List<String> mortgageIdStr;
    protected List<String> mortgageState;
    protected List<String> mortgageStatus;
    
    
    protected List<WebElement> viewButtons;
    protected List<WebElement> deleteButtons;
    
    private int lastestID = -1;
    private int indexLID = -1;
    WebDriver driver2;
    
    @BeforeEach
    public void setUpdriver2(){
    	//create application form
    	WebDriver driver = new ChromeDriver();
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

        //find the submit button and submit for application form
        WebElement submitButton = driver.findElement(By.className("submit"));
        submitButton.click();

        //close the application driver
        driver.close();
        
        //login as SP
        this.mortgageIdStr = new ArrayList<String>();
        this.mortgageState = new ArrayList<String>();
        this.mortgageStatus = new ArrayList<String>();
        this.viewButtons = new ArrayList<WebElement>();
        this.deleteButtons = new ArrayList<WebElement>();
    	
        driver2 = new ChromeDriver();
        driver2.get(link+"#login");
        username = driver2.findElement(By.id("email"));
        password = driver2.findElement(By.id("password"));
        login = driver2.findElement(By.name("logIn"));
        username.sendKeys("staff@topicus.nl");
        password.sendKeys("admin");
        login.click();

        wait = new WebDriverWait(driver2, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SPTable")));  
        WebElement table = driver2.findElement(By.id("SPTable"));
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
        String actualUrl = driver2.getCurrentUrl();

        assertEquals(expectedUrl,actualUrl);
        

        WebDriverWait wait2 = new WebDriverWait(driver2, 10);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("moveStep")));
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("show-application")));
        WebElement nextStepButton = driver2.findElement(By.id("moveStep"));
        
		nextStepButton.click();
		wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkButton")));

    }
    
    @Test
    public void test() {
        //------------------------TEST FOR SERVICE PROVIDER VIEW:----------------------------------------------

        // test for document request:
        WebElement documentRequired = driver2.findElement(By.id("documentRequired"));
        assertTrue(documentRequired.isDisplayed());

        WebElement documentText = driver2.findElement(By.id("documentName"));
        documentText.sendKeys("Proof of Income");
        WebElement requestDocButton = driver2.findElement(By.id("requestDoc"));
        requestDocButton.click();

        WebDriverWait wait0 = new WebDriverWait(driver2, 3);

        //test for document Check
        WebElement checkButton = driver2.findElement(By.id("checkButton"));
        checkButton.click();
        WebDriverWait wait3 = new WebDriverWait(driver2, 10);
        wait3.until(ExpectedConditions.visibilityOfElementLocated(By.id("has_income_check")));
        wait3.until(ExpectedConditions.visibilityOfElementLocated(By.id("no_financial_check")));
        wait3.until(ExpectedConditions.visibilityOfElementLocated(By.id("no_collateral_check")));
        WebElement incomeCheck = driver2.findElement(By.id("has_income_check"));
        incomeCheck.click();
        WebElement obligationCheck = driver2.findElement(By.id("no_financial_check"));
        obligationCheck.click();
        WebElement collateralCheck = driver2.findElement(By.id("no_collateral_check"));
        collateralCheck.click();
        wait3.until(ExpectedConditions.visibilityOfElementLocated(By.id("no_collateral_check")));
        WebElement explan = driver2.findElement(By.id("failedExplannation"));
        explan.sendKeys("This is not accepted");
        WebElement submit = driver2.findElement(By.id("submit-button"));
        submit.click();
        driver2.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);

        //close the driver connection:
        driver2.close();


        // -------------------------------TEST FOR CUSTOMER VIEW:-----------------------------------------------

        WebDriver driver3 = createDriver();
        mortgageIdStr = new ArrayList<String>();
        mortgageState = new ArrayList<String>();
        mortgageStatus = new ArrayList<String>();
        viewButtons = new ArrayList<WebElement>();
        deleteButtons = new ArrayList<WebElement>();

        //login as customer account:
        driver3.get(link + "#login");
        WebElement username = driver3.findElement(By.id("email"));
        WebElement password = driver3.findElement(By.id("password"));
        WebElement login = driver3.findElement(By.name("logIn"));
        username.sendKeys("bram@otte.biz");
        password.sendKeys("password");
        login.click();

        //direct to myMortgage page
        WebDriverWait wait1 = new WebDriverWait(driver3, 10);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("homeScreenLoggedIn")));
        driver3.findElement(By.id("myMortgagePage")).click();

        //wait till the mortgage table is loaded
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("mortgage-table")));

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

        //direct to the trackTrace page:
        this.viewButtons.get(indexLID).click();
        String expectedUrl1 =link + "#tracktrace?id=" + this.mortgageIdStr.get(indexLID);
        String actualUrl1 = driver3.getCurrentUrl();
        assertEquals(expectedUrl1,actualUrl1);

        
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("documentCheckContainer")));

        // test the document check box field:
        WebElement documentCheckBox = driver3.findElement(By.id("documentCheckContainer"));
        assertTrue(documentCheckBox.isDisplayed());

        //test the document required field:
        WebElement customerDocumentRequired = driver3.findElement(By.id("documentRequired"));
        assertTrue(customerDocumentRequired.isDisplayed()); 



    }
}
