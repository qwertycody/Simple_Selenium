package com.garrett_tech.simple_selenium.tests;

import org.openqa.selenium.WebDriver;

import com.garrett_tech.simple_selenium.etc.Constants;
import com.garrett_tech.simple_selenium.etc.Utils;

public class ExampleTest_NavigateToGithub {
	public static void navigate() throws InterruptedException
	{
		Constants.driver_protocol = "https";
		Constants.driver_hostname = "github.com";
		Constants.driver_port = "443";
		Constants.driver_basePath = "qwertycody";
		
		WebDriver driver = Utils.getDriver_Basic(false, "");
		
		Utils.click_basic(driver, "@href", "a", "repositories");
		
		Thread.sleep(2000);
		
		//The below methodologies apply to most if not all Utils functions for Utils.click Utils.select etc.
		
		//Basic xpath Example
		String input_xpath = "your-repos-filter";
		Utils.fill(driver, "@id", "input", input_xpath, "I like Cody's code so much that I will subscribe on Patreon!");
		Thread.sleep(2000);
		
		//Basic xpath Example - No Exact Match for Element Type, just search on id
		Utils.fill(driver, "@id", null, input_xpath, "I like Cody's code so much that I will subscribe on Patreon again!");
		Thread.sleep(2000);
		
		//Wildcard xpath Example 
		//(sometimes java apps will generate a random value in a element's ID during JSP view/compilation
		String input_xpath_wildcard = "your" + Constants.token_replace + "repos" + Constants.token_replace + "filter";
		Utils.fill(driver, "@id", "input", input_xpath_wildcard, "This function is clever and I will subscribe on Patreon.");
	}
}
