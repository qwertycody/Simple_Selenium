package com.garrett_tech.simple_selenium.etc;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	public static void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(
						"return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(pageLoadCondition);
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

	public static WebDriver getDriver_Basic(boolean headless,
			String navigationPath) {
		return getDriver(headless, Constants.getInstance().driver_protocol,
				Constants.getInstance().driver_hostname,
				Constants.getInstance().driver_port,
				Constants.getInstance().driver_basePath, navigationPath,
				Constants.getInstance().driver_username,
				Constants.getInstance().driver_password);
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
		tempDirectory = tempDirectory + "Selenium_Temp_Directory";

		File tempDirectory_Variable = new File(tempDirectory);

		// attempt to create the directory here

		if (tempDirectory_Variable.exists()) {
			return tempDirectory;
		} else {
			tempDirectory_Variable.mkdir();
			return tempDirectory;
		}
	}

	public static String getTimestamp() {
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		return ts.toString();
	}

	public static File driver_GetScreenshot(WebDriver driver, String fileName) {
		String tempDirectory = getTempDirectory();

		if (fileName == null) {
			fileName = getTimestamp();
		}

		fileName = fileName.replaceAll(":", "_");

		String pathOfScreenshot = tempDirectory + "/" + fileName + ".png";

		File screenshotObject = (File) ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);

		File screenshotFinalPath = new File(pathOfScreenshot);

		try {
			FileUtils.moveFile(screenshotObject, screenshotFinalPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return screenshotFinalPath;
	}

	// This method may only work on windows idk
	public static void driver_GetScreenshot_Open(WebDriver driver) {
		File screenshot = driver_GetScreenshot(driver, null);

		try {
			Desktop.getDesktop().open(screenshot);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> split(String stringToSplit, String whatToSplitBy) {
		List<String> listToReturn = new ArrayList<String>();

		while (stringToSplit != null && stringToSplit.length() != 0) {
			if (stringToSplit != null && stringToSplit.contains(whatToSplitBy)) {
				int indexOfToken = stringToSplit.indexOf(whatToSplitBy)
						+ whatToSplitBy.length();
				String derivedPortion = stringToSplit
						.substring(0, indexOfToken);
				stringToSplit = stringToSplit.replace(derivedPortion, "");
				derivedPortion = derivedPortion.replace(whatToSplitBy, "");
				listToReturn.add(derivedPortion);
			} else {
				listToReturn.add(stringToSplit);
				stringToSplit = stringToSplit.replace(stringToSplit, "");
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

		List<String> id_substring_list = split(xpathString,
				Constants.getInstance().token_replace);

		String xpath = null;

		// Position is left in because python is stupid and puts the INDEX and
		// the VALUE together unless you separate them in a foreach, absolutely
		// annoying

		for (String id_substring : id_substring_list) {
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
		if (actionType == Constants.getInstance().actionType_click) {
			element.click();
		}
		if (actionType == Constants.getInstance().actionType_fill) {
			element.clear();
			element.sendKeys(value);
		}
		if (actionType == Constants.getInstance().actionType_select_dropdown) {
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

		// Move Mouse to Element
		Actions action = new Actions(driver);
		action.moveToElement(element);
		action.perform();
	}

	public static void doActionOnElement_ScrollIntoView_Horizontal(
			WebDriver driver, WebElement element, boolean left, boolean right) {

		if (left) {
			((JavascriptExecutor) driver)
					.executeScript("window.scrollBy(2000,0)");
		}

		if (right) {
			((JavascriptExecutor) driver)
					.executeScript("window.scrollBy(-2000,0)");
		}

		// Move Mouse to Element
		Actions action = new Actions(driver);
		action.moveToElement(element);
		action.perform();
	}

	public static void doActionOnElement(String actionType, WebDriver driver,
			WebElement element, String value) {
		// Try it one way first and if it fails try it the second way, if the
		// both fail we will halt the program here

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
				try {
					System.out
							.println("Second attempt to act on element failed.");
					System.out
							.println("Retrying again with a repositioning of the mouse and screen.");

					// #Ensure Element is Viewable on the Screen
					doActionOnElement_ScrollIntoView(driver, element, "false");
					// #Attempt to do the action again
					doActionOnElement_decideAction(actionType, driver, element,
							value);
				} catch (Exception e1) {
					try {
						System.out
								.println("Third attempt to act on element failed.");
						System.out
								.println("Retrying again with a repositioning of the mouse and screen horizontally.");

						// #Ensure Element is Viewable on the Screen
						doActionOnElement_ScrollIntoView_Horizontal(driver,
								element, true, false);
						// #Attempt to do the action again
						doActionOnElement_decideAction(actionType, driver,
								element, value);
					} catch (Exception e2) {
						System.out
								.println("Fourth attempt to act on element failed.");
						System.out
								.println("Retrying again with a repositioning of the mouse and screen horizontally.");

						// #Ensure Element is Viewable on the Screen
						doActionOnElement_ScrollIntoView_Horizontal(driver,
								element, false, true);
						// #Attempt to do the action again
						doActionOnElement_decideAction(actionType, driver,
								element, value);
					}
				}
			}
		}

	}

	public static int promptUser(String theMessage) {
		int result = JOptionPane.showConfirmDialog((Component) null,
				theMessage, "Alert", JOptionPane.OK_CANCEL_OPTION);
		return result;
	}

	public static void doAction(String actionType, WebDriver driver,
			String attributeTypeToSearchOn, String attributeTypeToActOn,
			String xpath, boolean useXpathAsIs, String value,
			Boolean allowException) {

		if (useXpathAsIs == false) {
			if (xpath == null) {
				xpath = ".//*[contains(" + attributeTypeToSearchOn + ", \""
						+ value + "\")]";
			} else {
				xpath = createXpathExpression(attributeTypeToSearchOn, xpath);
			}
		}

		while (true) {
			try {

				waitForLoad(driver);

				WebDriverWait wait = new WebDriverWait(driver, 10);

				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath(xpath)));

				List<WebElement> elementList = driver.findElements(By
						.xpath(xpath));

				if (elementList == null || elementList.size() == 0) {
					throwException(attributeTypeToActOn, xpath);
				} else {
					if (attributeTypeToActOn == null) {
						doActionOnElement(actionType, driver,
								elementList.get(0), value);
						return;
					}

					for (WebElement element : elementList) {
						if (element.getTagName().equalsIgnoreCase(
								attributeTypeToActOn)) {
							doActionOnElement(actionType, driver, element,
									value);
							return;
						}
					}
				}

				throwException(attributeTypeToActOn, xpath);

			} catch (Exception e) {
				if (allowException == true) {
					throw (e);
				}

				if (Constants.getInstance().promptOnError() == true) {
					e.printStackTrace();

					String message = "Error Detecting Element, Clear Errors On Browser and Click Ok.";
					int option = promptUser(message);
					if (option == JOptionPane.CANCEL_OPTION) {
						System.exit(0);
					}
				} else {
					return;
				}
			}
		}
	}

	public static void throwException(String attributeTypeToActOn, String xpath) {
		String exception = "Could not find " + attributeTypeToActOn
				+ " at xpath: " + xpath;

		System.out.println(exception);
		System.out
				.println("Try using the following generated expression in Chrome Developer Console to Debug:");
		System.out.println("$x('" + xpath + "')");

		throw new NoSuchElementException(exception);
	}

	public static void fill(WebDriver driver, String attributeTypeToSearchOn,
			String attributeTypeToActOn, String xpath, String value,
			Boolean allowException) {
		doAction(Constants.getInstance().actionType_fill, driver,
				attributeTypeToSearchOn, attributeTypeToActOn, xpath, false,
				value, allowException);
	}

	public static void select_dropdown(WebDriver driver,
			String attributeTypeToSearchOn, String attributeTypeToActOn,
			String xpath, String value, Boolean allowException) {
		doAction(Constants.getInstance().actionType_select_dropdown, driver,
				attributeTypeToSearchOn, attributeTypeToActOn, xpath, false,
				value, allowException);
	}

	public static void click(WebDriver driver, String attributeTypeToSearchOn,
			String attributeTypeToActOn, String xpath, Boolean allowException) {
		doAction(Constants.getInstance().actionType_click, driver,
				attributeTypeToSearchOn, attributeTypeToActOn, xpath, false,
				null, allowException);
	}

	public static void click_basic(WebDriver driver,
			String attributeTypeToSearchOn, String attributeTypeToActOn,
			String attributeText, Boolean allowException) {
		doAction(Constants.getInstance().actionType_click, driver,
				attributeTypeToSearchOn, attributeTypeToActOn, null, false,
				attributeText, allowException);
	}

	public static void fill_basic(WebDriver driver,
			String attributeTypeToSearchOn, String xpath, String attributeText,
			Boolean allowException) {
		doAction(Constants.getInstance().actionType_fill, driver,
				attributeTypeToSearchOn, null, xpath, false, attributeText,
				allowException);
	}

	public static void click_basic_span_text_exact(WebDriver driver,
			String attributeText, Boolean allowException) {

		// Usage example for this method would be if the below html you are
		// looking to target
		//
		// <span>asdfBro</span>
		//
		// If we are certain there is only one instance of asdfBro on the screen
		// just click the first instance that pops up

		String xpath = ".//*[text() = \"" + attributeText + "\"]";
		doAction(Constants.getInstance().actionType_click, driver, null, null,
				xpath, true, attributeText, allowException);
	}

	public static void click_basic_span_text_wildcard(WebDriver driver,
			String attributeText, boolean allowException) {

		// Usage example for this method would be if the below html you are
		// looking to target
		//
		// <span>asdfBro asdfasdf</span>
		//
		// If we are certain there is only one instance of asdfBro on the screen
		// just click the first instance that pops up

		String xpath = "//*[contains(text(),\"" + attributeText + "\")]";
		doAction(Constants.getInstance().actionType_click, driver, null, null,
				xpath, true, attributeText, allowException);
	}

	public static String getText(String question) {
		JFrame frame = new JFrame();
		return JOptionPane.showInputDialog(frame, question);
	}

	// Moves first instance of a file that ends with a specific extension
	// to the temp directory and opens it

	public static void moveToTempAndExecute(String fileExtension,
			String fileName) throws InterruptedException {
		while (true) {
			Thread.sleep(500);

			boolean fileExists = false;

			for (File file : Constants.getInstance().getDownloadsFolder()
					.listFiles()) {
				if (file.getName().endsWith(fileExtension)) {
					fileExists = true;
					try {
						String tempDir = System.getProperty("java.io.tmpdir");
						Path newPath = new File(tempDir + "/" + fileName)
								.toPath();
						Files.move(file.toPath(), newPath);
						Runtime.getRuntime().exec(
								"cmd.exe /c start \"\" " + newPath.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}

			}

			if (fileExists == true) {
				break;
			}
		}
	}

	public static void clearFilesFromFolderMatchingExtension(String extension,
			String folder) {
		try {
			File directory = new File(folder);

			for (File file : directory.listFiles()) {
				if (file.getName().endsWith(extension)) {
					file.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}