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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestApplicationOverviewBySP extends BaseIntegrationTest { 
    protected WebDriver driver;
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
    public void setUpDriver(){

        this.mortgageIdStr = new ArrayList<String>();
        this.viewButtons = new ArrayList<WebElement>();
        this.deleteButtons = new ArrayList<WebElement>();
        this.mortgageState = new ArrayList<String>();
        this.mortgageStatus = new ArrayList<String>();
        this.driver = createDriver();
        this.driver.get(link+"#login");
        WebElement username = this.driver.findElement(By.id("email"));
        WebElement password = this.driver.findElement(By.id("password"));
        WebElement login = this.driver.findElement(By.name("logIn"));
        username.sendKeys("staff@topicus.nl");
        password.sendKeys("admin");
        login.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SPTable")));  
        WebElement table = this.driver.findElement(By.id("SPTable"));
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
    public void testTable() {
    	//test whether or not the table is exist
        WebElement table = this.driver.findElement(By.id("SPTable"));
        assertTrue(table.isDisplayed());

        // List<WebElement> tableHeaders = this.findElements(By.tagName("th"));
        List<WebElement> tableHeaders = table.findElements(By.tagName("th"));
        
        assertEquals(7,tableHeaders.size());
        assertEquals("MORTGAGE ID", tableHeaders.get(0).getText());
        assertEquals("START DATE", tableHeaders.get(1).getText());
        assertEquals("CUSTOMER ID", tableHeaders.get(2).getText());
        assertEquals("CUSTOMER NAME", tableHeaders.get(3).getText());
        assertEquals("STATE",tableHeaders.get(4).getText());
        assertEquals("STATUS", tableHeaders.get(5).getText());
        assertEquals("Action", tableHeaders.get(6).getText());
    }
    
    @Test
    public void testView() {
        // Test view button:
        this.viewButtons.get(indexLID).click();
        String expectedUrl =link + "#tracktraceSP?id=" + this.mortgageIdStr.get(indexLID);
        String actualUrl = this.driver.getCurrentUrl();

        assertEquals(expectedUrl,actualUrl);
    }
    
}
