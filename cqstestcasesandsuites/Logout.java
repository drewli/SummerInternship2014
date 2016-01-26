import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Logout {
	public static void doItHere(InternetExplorerVar ieVar){
		WebDriver driver = ieVar.getDriver();
		driver.get(ieVar.getBaseURL() + "/pages/home/home.aspx");
		String parentWindowHandler = driver.getWindowHandle();
		try {
			WebElement element = driver.findElement(By.partialLinkText("Logout"));
			element.click();
			driver.switchTo().alert().accept();
		}
		catch (NoSuchElementException e){
			driver.get(ieVar.getBaseURL() + "/pages/login/login.aspx");
		}
    	catch (NoAlertPresentException e) {}
		driver.switchTo().window(parentWindowHandler);
	}
}
