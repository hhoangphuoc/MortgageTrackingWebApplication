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

public class TestMyMortgagePage extends BaseIntegrationTest {

    protected WebDriver driver;

    protected List<WebElement> tableRows;
    protected List<WebElement> tableData;


    protected List<String> mortgageIdStr;
    protected List<String> allProcessStates;
    protected List<String> allStatus;
    protected List<WebElement> viewButtons;
    protected List<WebElement> deleteButtons;

    private int latestID = -1;
    private int indexLID = -1;
	
    @BeforeEach
    public void setUpLogin(){
        this.driver = createDriver();
        this.mortgageIdStr = new ArrayList<String>();
        this.allProcessStates = new ArrayList<String>();
        this.allStatus = new ArrayList<String>();
        this.viewButtons = new ArrayList<WebElement>();
        this.deleteButtons = new ArrayList<WebElement>();

        this.driver.get(link + "#login");
        WebElement username = this.driver.findElement(By.id("email"));
        WebElement password = this.driver.findElement(By.id("password"));
        WebElement login = this.driver.findElement(By.name("logIn"));
        username.sendKeys("bram@otte.biz");
        password.sendKeys("password");
        login.click();
        WebDriverWait wait1 = new WebDriverWait(driver, 15);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.id("homeScreenLoggedIn")));
        this.driver.findElement(By.id("myMortgagePage")).click();

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
                if(Integer.parseInt(data) >latestID){
                    latestID = Integer.parseInt(data);
                    indexLID = i/6;
                }
            }
            if(i%6==5){
                this.viewButtons.add(this.tableData.get(i).findElement(By.id("viewButton")));
                this.deleteButtons.add(this.tableData.get(i).findElement(By.id("deleteButton")));
            }
            if(i%6==3){
                this.allProcessStates.add(this.tableData.get(i).getText());
            }
            if(i%6 == 4){
                this.allStatus.add(this.tableData.get(i).getText());
            }
        }
    }
    @Test
    public void testMortgageTable(){

        // ensure that the table is exist by checking its header:
        WebElement table = this.driver.findElement(By.id("mortgage-table"));
        assertTrue(table.isDisplayed());

        List<WebElement> tableHeaders = table.findElements(By.tagName("th"));
        assertEquals(6,tableHeaders.size());

    }
    @Test
    public void testViewButton(){
        this.viewButtons.get(indexLID).click();

        String expectedUrl =link + "#tracktrace?id=" + this.mortgageIdStr.get(indexLID);
        String actualUrl = this.driver.getCurrentUrl();
        assertEquals(expectedUrl,actualUrl);
    }


}
