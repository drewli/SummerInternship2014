import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class LoggingIn {
	
	public static void main(String[] args) {
		System.setProperty("webdriver.ie.driver", "C:\\Users\\anli\\IEDriverServer.exe");
		WebDriver driver = new InternetExplorerDriver();
		driver.get("http://qa-cqs-34-01/pages/login/login.aspx");
		WebElement element = driver.findElement(By.id("customer"));
		element.clear();
		element.sendKeys(new String[]{"admin"});
		element = driver.findElement(By.id("userid"));
		element.clear();
		element.sendKeys(new String[]{"admin"});
		element = driver.findElement(By.id("password"));
		element.clear();
		element.sendKeys(new String[]{"P@ssw0rd!"});
		element = driver.findElement(By.id("login_button"));
		element.click();
	  }
	
	public static void doItHere(InternetExplorerVar ieVariable) {
		WebDriver driver = ieVariable.getDriver();
		ieVariable.setBaseURL("http://qa-cqs-34-01");
		driver.get(ieVariable.getBaseURL() + "/pages/login/login.aspx");
		WebElement element = driver.findElement(By.id("customer"));
		element.clear();
		element.sendKeys(new String[]{"admin"});
		element = driver.findElement(By.id("userid"));
		element.clear();
		element.sendKeys(new String[]{"admin"});
		element = driver.findElement(By.id("password"));
		element.clear();
		element.sendKeys(new String[]{"P@ssw0rd!"});
		element = driver.findElement(By.id("login_button"));
		element.click();
	  }
	
	public static void toSetupTab(InternetExplorerVar ieVariable){
		WebDriver driver = ieVariable.getDriver();
		WebElement element = driver.findElement(By.cssSelector("span[url=\"getHomePageLink(\'setup\')\"]"));
		element.click();
		ieVariable.setDriver(driver);
	}
	
}