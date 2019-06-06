package com.garrett_tech.simple_selenium.etc;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class Utils {
	public static String encodeString(String stringToEncode) {
		try {
			return URLEncoder.encode(stringToEncode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static WebDriver getDriverForOS(boolean headless) {

		WebDriver driver;
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(headless);

		try {
			String[] arguments = { "--window-position=2000,0" };
			options.addArguments(arguments);
			driver = new ChromeDriver(options);
		} catch (Exception e) {
			System.out.println("Unable to put on Secondary Monitor");
			driver = new ChromeDriver();
		}

		driver.manage().window().maximize();

		return driver;
	}
	
	public static WebDriver getDriver_Basic(boolean headless, String navigationPath){
	    return getDriver(headless, Constants.driver_protocol, Constants.driver_hostname, Constants.driver_port, Constants.driver_basePath, navigationPath, Constants.driver_username, Constants.driver_password);
	}

	public static WebDriver getDriver(boolean headless, String protocol,
			String hostname, String port, String basePath,
			String navigationPath, String username, String password) {
		WebDriver driver = getDriverForOS(headless);

		String fullURL = "";

		// Check if Username Password Not Null
		if (username != null && password != null) {

			username = encodeString(username);
			password = encodeString(password);
			basePath = encodeString(basePath);
			navigationPath = encodeString(navigationPath);

			fullURL = protocol + "://" + username + ":" + password + "@"
					+ hostname + ":" + port + "/" + basePath + "/"
					+ navigationPath;
		} else {
			fullURL = protocol + "://" + hostname + ":" + port + "/" + basePath
					+ "/" + navigationPath;
		}

		// Define Driver at this Path
		driver.get(fullURL);

		return driver;
	}

	public static String getTempDirectory() {
		String property = "java.io.tmpdir";
		String tempDirectory = System.getProperty(property);
		tempDirectory = tempDirectory + "/" + "Selenium_Temp_Directory";

		File tempDirectory_Variable = new File(tempDirectory);

		// attempt to create the directory here
		boolean successful = tempDirectory_Variable.mkdir();

		if (successful) {
			return tempDirectory;
		} else {
			return null;
		}
	}

	public static String getTimestamp() {
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		return ts.toString();
	}

	public static String driver_GetScreenshot(WebDriver driver, String fileName) {
		String tempDirectory = getTempDirectory();

		if (fileName == null) {
			fileName = getTimestamp();
		}

		String pathOfScreenshot = tempDirectory + "/" + fileName + ".png";

		File screenshot = (File) ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(screenshot, new File(pathOfScreenshot));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pathOfScreenshot;
	}

	// This method may only work on windows idk
	public static void driver_GetScreenshot_Open(WebDriver driver) {
		String screenshot = driver_GetScreenshot(driver, null);
		Runtime runtime = Runtime.getRuntime(); // getting Runtime object

		try {
			runtime.exec(screenshot); // opens new notepad instance
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> split(String stringToSplit, String whatToSplitBy)
	{
		List<String> listToReturn = new ArrayList<String>();
		
		while(stringToSplit.length() != 0)
		{
			if(stringToSplit != null && stringToSplit.contains(whatToSplitBy))
			{
				int indexOfToken = stringToSplit.indexOf(whatToSplitBy) + whatToSplitBy.length();
				String derivedPortion = stringToSplit.substring(0, indexOfToken);
				stringToSplit = stringToSplit.replace(derivedPortion, "");
				derivedPortion = derivedPortion.replace(whatToSplitBy, "");
				listToReturn.add(derivedPortion);
			}
			else
			{
				listToReturn.add(stringToSplit);
			}
		}
		
		return listToReturn;
	}

	public static String createXpathExpression(String attributeType,
			String xpathString) {
		// What Chrome Gives You
		// *[@id="MENUBAR.SidebarMenu_un10_MENUBAR.SidebarMenu_MENUITEM.SearchParties_MENUITEM.SearchPartieslink"]

		// All we really need is
		// #MENUBAR.SidebarMenu_un10_MENUBAR.SidebarMenu_MENUITEM.SearchParties_MENUITEM.SearchPartieslink

		// What We Need to Create
		// './/*[contains(@id, "MENUBAR.SidebarMenu_") and contains(@id,
		// "_MENUBAR.SidebarMenu_MENUITEM.SearchParties_MENUITEM.SearchPartieslink")]'

		// Test your own Xpath Expression in Chrome using the below template
		// $x('.//*[contains(@id, "{PUT_YOUR_VALUE_HERE}")]')		
		
		List<String> id_substring_list = split(xpathString, Constants.token_replace);
		
		String xpath = null;

		// Position is left in because python is stupid and puts the INDEX and
		// the VALUE together unless you separate them in a foreach, absolutely
		// annoying

		for(String id_substring : id_substring_list)
		{			
			if (xpath == null) {
				xpath = "(.//*[contains(" + attributeType + ", \""
						+ id_substring + "\")";
			} else {
				xpath = xpath + " and contains(" + attributeType + ", \""
						+ id_substring + "\")";
			}
		}
		
		xpath = xpath + "])";

		return xpath;
	}

	public static void doActionOnElement_decideAction(String actionType,
			WebDriver driver, WebElement element, String value) {
		if (actionType == Constants.actionType_click) {
			element.click();
		}
		if (actionType == Constants.actionType_fill) {
			element.clear();
			element.sendKeys(value);
		}
		if (actionType == Constants.actionType_select_dropdown) {
			Select select = new Select(element);
			select.selectByVisibleText(value);
		}
	}

	// From Stack Overflow - Hilarious:
	// Pass in true to scrollIntoView if the object you're scrolling to is
	// beneath where you currently are,
	// false if the object you're scrolling to is above where you currently are.
	// I had a hard time figuring that out. – Big Money Jul 27 '16 at 22:07

	public static void doActionOnElement_ScrollIntoView(WebDriver driver,
			WebElement element, String objectBeneathWhereYouCurrentlyAre) {
		// #Ensure Element is Viewable on the Screen
		((JavascriptExecutor) driver).executeScript(
				"arguments[0].scrollIntoView("
						+ objectBeneathWhereYouCurrentlyAre + ");", element);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Move Mouse to Element
		Actions action = new Actions(driver);
		action.moveToElement(element);
		action.perform();
	}

	public static void doActionOnElement(String actionType, WebDriver driver,
			WebElement element, String value) {
		// Try it one way first and if it fails try it the second way, if the
		// both fail we will hault the program here

		try {
			doActionOnElement_decideAction(actionType, driver, element, value);
		} catch (Exception e) {
			System.out.println("First attempt to act on element failed.");
			System.out
					.println("Retrying again with a repositioning of the mouse and screen.");

			try {
				// #Ensure Element is Viewable on the Screen
				doActionOnElement_ScrollIntoView(driver, element, "true");
				// #Attempt to do the action again
				doActionOnElement_decideAction(actionType, driver, element,
						value);
			} catch (Exception a) {
				System.out.println("Second attempt to act on element failed.");
				System.out
						.println("Retrying again with a repositioning of the mouse and screen.");

				// #Ensure Element is Viewable on the Screen
				doActionOnElement_ScrollIntoView(driver, element, "false");
				// #Attempt to do the action again
				doActionOnElement_decideAction(actionType, driver, element,
						value);
			}
		}
	}

	public static void doAction(String actionType, WebDriver driver,
			String attributeTypeToSearchOn, String attributeTypeToActOn,
			String xpath, boolean useXpathAsIs, String value) {
		if (useXpathAsIs == false) {
			if (xpath == null) {
				xpath = ".//*[contains(" + attributeTypeToSearchOn + ", \"" + value + "\")]";
			} else {
				xpath = createXpathExpression(attributeTypeToSearchOn, xpath);
			}
		}

		List<WebElement> elementList = driver.findElements(By.xpath(xpath));

		if (elementList == null || elementList.size() == 0) {
			throwException(attributeTypeToActOn, xpath);
		} else {
			if (attributeTypeToActOn == null) {
				doActionOnElement(actionType, driver, elementList.get(0), value);
				return;
			}

			for (WebElement element : elementList) {
				if (element.getTagName().equalsIgnoreCase(attributeTypeToActOn)) {
					doActionOnElement(actionType, driver, element, value);
					return;
				}
			}
		}

		throwException(attributeTypeToActOn, xpath);
	}
	
	public static void throwException(String attributeTypeToActOn, String xpath)
	{
		String exception = "Could not find " + attributeTypeToActOn + " at xpath: " + xpath;
		
		System.out.println(exception);
		System.out.println("Try using the following generated expression in Chrome Developer Console to Debug:");
		System.out.println("$x('" + xpath + "')");
		
		throw new NoSuchElementException(exception);
	}

	public static void fill(WebDriver driver, String attributeTypeToSearchOn,
			String attributeTypeToActOn, String xpath, String value) {
		doAction(Constants.actionType_fill, driver, attributeTypeToSearchOn,
				attributeTypeToActOn, xpath, false, value);
	}

	public static void select_dropdown(WebDriver driver,
			String attributeTypeToSearchOn, String attributeTypeToActOn,
			String xpath, String value) {
		doAction(Constants.actionType_select_dropdown, driver,
				attributeTypeToSearchOn, attributeTypeToActOn, xpath, false,
				value);
	}

	public static void click(WebDriver driver, String attributeTypeToSearchOn,
			String attributeTypeToActOn, String xpath) {
		doAction(Constants.actionType_click, driver, attributeTypeToSearchOn,
				attributeTypeToActOn, xpath, false, null);
	}

	public static void click_basic(WebDriver driver,
			String attributeTypeToSearchOn, String attributeTypeToActOn,
			String attributeText) {
		doAction(Constants.actionType_click, driver, attributeTypeToSearchOn,
				attributeTypeToActOn, null, false, attributeText);
	}

	public static void fill_basic(WebDriver driver,
			String attributeTypeToSearchOn, String xpath, String attributeText) {
		doAction(Constants.actionType_fill, driver, attributeTypeToSearchOn,
				null, xpath, false, attributeText);
	}

	public static void click_basic_span_text_exact(WebDriver driver,
			String attributeText) {

		// Usage example for this method would be if the below html you are
		// looking to target
		//
		// <span>asdfBro</span>
		//
		// If we are certain there is only one instance of asdfBro on the screen
		// just click the first instance that pops up

		String xpath = ".//*[text() = \"" + attributeText + "\"]";
		doAction(Constants.actionType_click, driver, null, null, xpath, true,
				attributeText);
	}

	public static void click_basic_span_text_wildcard(WebDriver driver,
			String attributeText) {

		// Usage example for this method would be if the below html you are
		// looking to target
		//
		// <span>asdfBro asdfasdf</span>
		//
		// If we are certain there is only one instance of asdfBro on the screen
		// just click the first instance that pops up

		String xpath = "//*[contains(text(),\"" + attributeText + "\")]";
		doAction(Constants.actionType_click, driver, null, null, xpath, true,
				attributeText);
	}
}