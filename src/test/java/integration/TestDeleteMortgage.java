package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestDeleteMortgage extends BaseIntegrationTest {
    protected List<WebElement> tableRows;
    protected List<WebElement> tableData;
    protected List<String> mortgageIdStr;
    protected List<String> mortgageState;
    protected List<String> mortgageStatus;
    
    
    protected List<WebElement> viewButtons;
    protected List<WebElement> deleteButtons;
    
    private int lastestID = -1;
    private int indexLID = -1;
    WebDriver driver;
    
    @BeforeEach
    public void setUpdriver(){
    	//create application form
    	driver = new ChromeDriver();;
        this.mortgageIdStr = new ArrayList<String>();
        this.mortgageState = new ArrayList<String>();
        this.mortgageStatus = new ArrayList<String>();
        this.viewButtons = new ArrayList<WebElement>();
        this.deleteButtons = new ArrayList<WebElement>();
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
        
        
        //go to my mortgage again
        wait = new WebDriverWait(driver, 10);
        WebElement myMortgagePagebutton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myMortgagePage")));
    	myMortgagePagebutton.click();
    	
        WebDriverWait wait2 = new WebDriverWait(driver, 10);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("mortgage-table")));

        //test whether or not the table is exist
        WebElement table = this.driver.findElement(By.id("mortgage-table"));
        assertTrue(table.isDisplayed());

        this.tableData = table.findElements(By.tagName("td"));

        for(int i=0; i<this.tableData.size(); i++){
            if(i%6 == 0){
                String data = this.tableData.get(i).getText();
                this.mortgageIdStr.add(data);
                if(Integer.parseInt(data) >lastestID){
                	lastestID = Integer.parseInt(data);
                    indexLID = i/6;
                }
            }
            if(i%6==5){
                this.viewButtons.add(this.tableData.get(i).findElement(By.id("viewButton")));
                this.deleteButtons.add(this.tableData.get(i).findElement(By.id("deleteButton")));
            }
            if(i%6==3){
                this.mortgageState.add(this.tableData.get(i).getText());
            }
            if(i%6 == 4){
                this.mortgageStatus.add(this.tableData.get(i).getText());
            }
        }
    }
    
    @Test
    public void testDeleteMortgage(){

        this.deleteButtons.get(indexLID).click();
        //test confirm box
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.alertIsPresent());
        // check confirm message:
        Alert alert = this.driver.switchTo().alert();

        alert.accept();

        // test window reload
        String urlBeforeReload = this.driver.getCurrentUrl();
        this.driver.navigate().refresh();
        String urlAfterReload = this.driver.getCurrentUrl();
        assertEquals(urlAfterReload,urlBeforeReload);


        WebDriverWait wait3 = new WebDriverWait(driver, 10);
        wait3.until(ExpectedConditions.visibilityOfElementLocated(By.id("mortgage-table")));

        this.mortgageIdStr = new ArrayList<String>();
        this.mortgageState = new ArrayList<String>();
        this.mortgageStatus = new ArrayList<String>();
        this.viewButtons = new ArrayList<WebElement>();
        this.deleteButtons = new ArrayList<WebElement>();
        
        WebElement table = this.driver.findElement(By.id("mortgage-table"));
        assertTrue(table.isDisplayed());

        this.tableData = table.findElements(By.tagName("td"));

        for(int i=0; i<this.tableData.size(); i++){
            if(i%6 == 0){
                String data = this.tableData.get(i).getText();
                this.mortgageIdStr.add(data);
                if(Integer.parseInt(data) >lastestID){
                	lastestID = Integer.parseInt(data);
                    indexLID = i/6;
                }
            }
            if(i%6==5){
                this.viewButtons.add(this.tableData.get(i).findElement(By.id("viewButton")));
                this.deleteButtons.add(this.tableData.get(i).findElement(By.id("deleteButton")));
            }
            if(i%6==3){
                this.mortgageState.add(this.tableData.get(i).getText());
            }
            if(i%6 == 4){
                this.mortgageStatus.add(this.tableData.get(i).getText());
            }
        }
        //test processState change to Delete Request:
        assertEquals("Delete Request", mortgageState.get(indexLID));

        this.driver.close();
        
        this.mortgageIdStr = new ArrayList<String>();
        this.mortgageState = new ArrayList<String>();
        this.mortgageStatus = new ArrayList<String>();
        this.viewButtons = new ArrayList<WebElement>();
        this.deleteButtons = new ArrayList<WebElement>();
        lastestID = -1;
        indexLID = -1;
        
        driver = new ChromeDriver();
        driver.get(link+"#login");
        WebElement username  = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement login = driver.findElement(By.name("logIn"));
        username.sendKeys("staff@topicus.nl");
        password.sendKeys("admin");
        login.click();

        wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SPTable")));  
        table = driver.findElement(By.id("SPTable"));
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
        
        
    	assertEquals("Delete Request", this.mortgageState.get(indexLID));
    	assertEquals("Processing", this.mortgageStatus.get(indexLID));
        // Test view button:
    	int tableSizeBeforeDelete = tableData.size();
        this.deleteButtons.get(indexLID).click();
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.alertIsPresent());
        alert = this.driver.switchTo().alert();

        alert.accept();

        // test window reload
        urlBeforeReload = this.driver.getCurrentUrl();
        this.driver.navigate().refresh();
        urlAfterReload = this.driver.getCurrentUrl();
        assertEquals(urlAfterReload,urlBeforeReload);
        
        wait3 = new WebDriverWait(driver, 10);
        wait3.until(ExpectedConditions.visibilityOfElementLocated(By.id("SPTable")));

        this.mortgageIdStr = new ArrayList<String>();
        this.mortgageState = new ArrayList<String>();
        this.mortgageStatus = new ArrayList<String>();
        this.viewButtons = new ArrayList<WebElement>();
        this.deleteButtons = new ArrayList<WebElement>();
        lastestID = -1;
        indexLID = -1;
        table = this.driver.findElement(By.id("SPTable"));
        assertTrue(table.isDisplayed());

        this.tableData = table.findElements(By.tagName("td"));
        assertEquals(tableData.size(), tableSizeBeforeDelete-7);
        driver.close();
    }   
}
