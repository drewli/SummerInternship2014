import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DeletingProviderGroup {
	
	public static void main(String[] args) {
		InternetExplorerVar ieVar = new InternetExplorerVar();
		LoggingIn.doItHere(ieVar);
		WebDriver driver = ieVar.getDriver();
		WebElement element;
		driver.get(ieVar.getBaseURL() + "/pages/setup/department/department_list.aspx");
		element = driver.findElement(By.id("key"));
		element.clear();
		element.sendKeys(new String[]{"ANDRPRGR"});
        element = driver.findElement(By.id("filter"));
        element.clear();
        element.sendKeys(new String[]{"Andrey\'s Providers"});
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
        		return;
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
        String parentWindowHandler = driver.getWindowHandle();
        element = driver.findElement(By.id("list_delete_button"));
		element.click();
		try{
			while (true){
				driver.switchTo().alert().accept();
			}
		}
		catch (NoAlertPresentException e) {
		}
		driver.switchTo().window(parentWindowHandler);
		Logout.doItHere(ieVar);
	}
	
	public static void doItHere(InternetExplorerVar ieVar) {
		WebDriver driver = ieVar.getDriver();
		WebElement element;
		driver.get(ieVar.getBaseURL() + "/pages/setup/department/department_list.aspx");
		element = driver.findElement(By.id("key"));
		element.clear();
		element.sendKeys(new String[]{"ANDRPRGR"});
        element = driver.findElement(By.id("filter"));
        element.clear();
        element.sendKeys(new String[]{"Andrey\'s Providers"});
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
        		return;
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
        String parentWindowHandler = driver.getWindowHandle();
        element = driver.findElement(By.id("list_delete_button"));
		element.click();
		try{
			while (true){
				driver.switchTo().alert().accept();
			}
		}
		catch (NoAlertPresentException e) {
		}
		driver.switchTo().window(parentWindowHandler);
	}
}
