package Base_Class;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BaseSetup {
	/**
	public static  WebDriver driver;
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


	public static void close(){
		try{
			driver.close();
		}catch(Exception e){
			System.out.println("Exception occured while closing the browser");
		}
	}


	public static void selectdropdown() {
		WebElement element= driver.findElement(By.xpath(""));// put your selector here
		Select sel= new Select(element);
		sel.selectByValue("India");
		WebDriverWait wait =new WebDriverWait(driver, 10);
		List<WebElement> product_List=driver.findElements(By.xpath(".//div[@class='bxc-grid__image   bxc-grid__image--light']"));
		for(int i=0;i<=product_List.size();i++) {

			String s=product_List.get(i).getAttribute("name");
		}
	}

	public static void main( String args[]){

	}
	 **/
}
