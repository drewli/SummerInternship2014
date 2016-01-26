import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;


public class InternetExplorerVar {
	private WebDriver driver;
	private String baseURL;

	public InternetExplorerVar() {
		System.setProperty("webdriver.ie.driver", "C:\\Users\\anli\\IEDriverServer.exe");
		driver = new InternetExplorerDriver();
		baseURL = "";
	}
	
	public InternetExplorerVar(String baseurl){
		driver = new InternetExplorerDriver();
		baseURL = baseurl;
	}
	
	public void setDriver(WebDriver param){
		driver = param;
	}

	public WebDriver getDriver(){
		return driver;
	}
	
	public String getBaseURL(){
		return baseURL;
	}
	
	public void setBaseURL(String baseurl){
		baseURL = baseurl;
	}
}