import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditingOrganization {

	public static void main(String[] args) {
		InternetExplorerVar ieVar = new InternetExplorerVar();
		LoggingIn.doItHere(ieVar);
		WebDriver driver = ieVar.getDriver();
		WebElement element;
		driver.get(ieVar.getBaseURL() + "/pages/setup/organization/organization_list.aspx");
		element = driver.findElement(By.id("key"));
		element.clear();
		element.sendKeys(new String[]{"ANDRHO23"});
        element = driver.findElement(By.id("filter"));
        element.clear();
        element.sendKeys(new String[]{"Andrew\'s Health Organization"});
        element = driver.findElement(By.id("search_btn"));
        element.click();
        final Set<String> handles = driver.getWindowHandles();
        String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
		String subWindowHandler = "";
        for (int i = 0; true; i++){
        	try {
        		Thread.sleep(5);
        	}
        	catch(InterruptedException e){
        		continue;
        	}
        	if (i == 200) {
        		return;
        	}
        	try {
        		element = driver.findElement(By.xpath("//div[@id=\'list_list_body\']/div/div"));
        		element.click();
        		element = driver.findElement(By.id("list_edit_button"));
        		element.click();   
        		
        		subWindowHandler = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<String>() {
        			public String apply(WebDriver input) {
        				Set<String> newHandles = input.getWindowHandles();
        				newHandles.removeAll(handles);
        				if (newHandles.size() > 0) {
        				return newHandles.iterator().next();
        				}
        			return null;
        			}
        		});
        	}
        	catch (NoSuchElementException e){
        		continue;
        	}
        	catch (StaleElementReferenceException e){
        		continue;
        	}
        	catch (ElementNotVisibleException e){
        		continue;
        	}
        	catch (TimeoutException e){
        		i = 0;
        		continue;
        	}
        	break;
        }
		
        driver.switchTo().window(subWindowHandler);
        
        element = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<WebElement>() {
        	public WebElement apply(WebDriver input) {
        		return input.findElement(By.id("key"));
        	}
        });

        element = driver.findElement(By.id("name"));
        element.clear();
        element.sendKeys(new String[]{"Andrew\'s New Organization"});
        element = driver.findElement(By.id("description"));
        element.clear();
        element.sendKeys(new String[]{"A testB organization"});
		element = driver.findElement(By.id("save_button"));
		element.click();
		driver.switchTo().window(parentWindowHandler);
		try {
			driver.switchTo().window(subWindowHandler);
			driver.close();
			driver.switchTo().window(parentWindowHandler);
			System.out.println("The edit provider group window was still open after saving " + 
		                       "with all the required fields");
		}
		catch (NoSuchWindowException e){
		}
		element = driver.findElement(By.id("key"));
		element.clear();
		element.sendKeys(new String[]{"ANDRHO23"});
        element = driver.findElement(By.id("filter"));
        element.clear();
        element.sendKeys(new String[]{"Andrew\'s New Organization"});
        element = driver.findElement(By.id("search_btn"));
        element.click();
        for (int i = 0; true; i++){
        	try {
        		Thread.sleep(5);
        	}
        	catch(InterruptedException e){
        		continue;
        	}
        	if (i == 200) {
        		System.out.println("Name of Organization could not be changed.");
        		break;
        	}
        	try {
        		element = driver.findElement(By.xpath("//div[@id=\'list_list_body\']/div/div"));
        		element.click();
        	}
        	catch (NoSuchElementException e){
        		continue;
        	}
        	catch (StaleElementReferenceException e){
        		continue;
        	}
        	catch (ElementNotVisibleException e){
        		continue;
        	}
        	catch (TimeoutException e){
        		i = 0;
        		continue;
        	}
        	break;
        }
		element = driver.findElement(By.id("key"));
		element.clear();
		element.sendKeys(new String[]{"ANDRHO23"});
        element = driver.findElement(By.id("filter"));
        element.clear();
        element.sendKeys(new String[]{"A testB organization"});
        element = driver.findElement(By.id("search_btn"));
        element.click();
        for (int i = 0; true; i++){
        	try {
        		Thread.sleep(5);
        	}
        	catch(InterruptedException e){
        		continue;
        	}
        	if (i == 200) {
        		System.out.println("Description of Organization could not be changed.");
        		break;
        	}
        	try {
        		element = driver.findElement(By.xpath("//div[@id=\'list_list_body\']/div/div"));
        		element.click();
        	}
        	catch (NoSuchElementException e){
        		continue;
        	}
        	catch (StaleElementReferenceException e){
        		continue;
        	}
        	catch (ElementNotVisibleException e){
        		continue;
        	}
        	catch (TimeoutException e){
        		i = 0;
        		continue;
        	}
        	break;
        }
		Logout.doItHere(ieVar);
	}
	
	public static void doItHere(InternetExplorerVar ieVar) {
		WebDriver driver = ieVar.getDriver();
		WebElement element;
		driver.get(ieVar.getBaseURL() + "/pages/setup/organization/organization_list.aspx");
		element = driver.findElement(By.id("key"));
		element.clear();
		element.sendKeys(new String[]{"ANDRHO23"});
        element = driver.findElement(By.id("filter"));
        element.clear();
        element.sendKeys(new String[]{"Andrew\'s Health Organization"});
        element = driver.findElement(By.id("search_btn"));
        element.click();
        final Set<String> handles = driver.getWindowHandles();
        String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
		String subWindowHandler = "";
        for (int i = 0; true; i++){
        	try {
        		Thread.sleep(5);
        	}
        	catch(InterruptedException e){
        		continue;
        	}
        	if (i == 200) {
        		return;
        	}
        	try {
        		element = driver.findElement(By.xpath("//div[@id=\'list_list_body\']/div/div"));
        		element.click();
        		element = driver.findElement(By.id("list_edit_button"));
        		element.click();   
        		
        		subWindowHandler = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<String>() {
        			public String apply(WebDriver input) {
        				Set<String> newHandles = input.getWindowHandles();
        				newHandles.removeAll(handles);
        				if (newHandles.size() > 0) {
        				return newHandles.iterator().next();
        				}
        			return null;
        			}
        		});
        	}
        	catch (NoSuchElementException e){
        		continue;
        	}
        	catch (StaleElementReferenceException e){
        		continue;
        	}
        	catch (ElementNotVisibleException e){
        		continue;
        	}
        	catch (TimeoutException e){
        		i = 0;
        		continue;
        	}
        	break;
        }
		
        driver.switchTo().window(subWindowHandler);
        
        element = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<WebElement>() {
        	public WebElement apply(WebDriver input) {
        		return input.findElement(By.id("key"));
        	}
        });

        element = driver.findElement(By.id("name"));
        element.clear();
        element.sendKeys(new String[]{"Andrew\'s New Organization"});
        element = driver.findElement(By.id("description"));
        element.clear();
        element.sendKeys(new String[]{"A testB organization"});
		element = driver.findElement(By.id("save_button"));
		element.click();
		driver.switchTo().window(parentWindowHandler);
		try {
			driver.switchTo().window(subWindowHandler);
			driver.close();
			driver.switchTo().window(parentWindowHandler);
			System.out.println("The edit provider group window was still open after saving " + 
		                       "with all the required fields");
		}
		catch (NoSuchWindowException e){
		}
		element = driver.findElement(By.id("key"));
		element.clear();
		element.sendKeys(new String[]{"ANDRHO23"});
        element = driver.findElement(By.id("filter"));
        element.clear();
        element.sendKeys(new String[]{"Andrew\'s New Organization"});
        element = driver.findElement(By.id("search_btn"));
        element.click();
        for (int i = 0; true; i++){
        	try {
        		Thread.sleep(5);
        	}
        	catch(InterruptedException e){
        		continue;
        	}
        	if (i == 200) {
        		System.out.println("Name of Organization could not be changed.");
        		break;
        	}
        	try {
        		element = driver.findElement(By.xpath("//div[@id=\'list_list_body\']/div/div"));
        		element.click();
        	}
        	catch (NoSuchElementException e){
        		continue;
        	}
        	catch (StaleElementReferenceException e){
        		continue;
        	}
        	catch (ElementNotVisibleException e){
        		continue;
        	}
        	catch (TimeoutException e){
        		i = 0;
        		continue;
        	}
        	break;
        }
		element = driver.findElement(By.id("key"));
		element.clear();
		element.sendKeys(new String[]{"ANDRHO23"});
        element = driver.findElement(By.id("filter"));
        element.clear();
        element.sendKeys(new String[]{"A testB organization"});
        element = driver.findElement(By.id("search_btn"));
        element.click();
        for (int i = 0; true; i++){
        	try {
        		Thread.sleep(5);
        	}
        	catch(InterruptedException e){
        		continue;
        	}
        	if (i == 200) {
        		System.out.println("Description of Organization could not be changed.");
        		break;
        	}
        	try {
        		element = driver.findElement(By.xpath("//div[@id=\'list_list_body\']/div/div"));
        		element.click();
        	}
        	catch (NoSuchElementException e){
        		continue;
        	}
        	catch (StaleElementReferenceException e){
        		continue;
        	}
        	catch (ElementNotVisibleException e){
        		continue;
        	}
        	catch (TimeoutException e){
        		i = 0;
        		continue;
        	}
        	break;
        }
	}


}
