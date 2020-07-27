package Base_Class;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class BaseSetup {
	/**
	public static  WebDriver driver;
	@BeforeClass
	public static void openBrowser(String browser){
		try{
			String os=System.getProperty("os.name");
			if(System.getProperty("os.name").contains("Mac")){
				if(browser.equalsIgnoreCase("chrome")){
					System.setProperty("webdriver.chrome.driver", "conf/Driver/mac/chromedriver");
					driver=new ChromeDriver();
				    driver.manage().window().setSize(new Dimension(1600,900));
					
				}
				if(browser.equalsIgnoreCase("firefox")){
					System.setProperty("webdriver.gecko.driver", "conf/Driver/mac/geckodriver");
					driver=new FirefoxDriver();
					driver.manage().window().maximize();
				}
			}
			if(System.getProperty("os.name").contains("window")){
			if(browser.equalsIgnoreCase("chrome")){
				System.setProperty("webdriver.chrome.driver", "./conf/Driver/window/chromedriver.exe");
				driver=new ChromeDriver();
			}
			if(browser.equalsIgnoreCase("firefox")){
				System.setProperty("webdriver.gecko.driver", "./conf/Driver/window/geckodriver.exe");
				driver=new FirefoxDriver();
				driver.manage().window().maximize();
			}	
		}
		}catch(Exception e){
			System.out.println("Exception Occured while  intializing the webdriver");
		}
	}

	@AfterClass	
	public static void close(){
		try{
			driver.close();
		}catch(Exception e){
			System.out.println("Exception occured while closing the browser");
		}
	}
	*/

public static void main( String args[]){
	
}
}
