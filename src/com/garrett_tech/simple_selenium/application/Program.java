package com.garrett_tech.simple_selenium.application;

import com.garrett_tech.simple_selenium.etc.Constants;
import com.garrett_tech.simple_selenium.etc.Utils;
import com.garrett_tech.simple_selenium.tests.ExampleTest_NavigateToGithub;

public class Program {
		
    public static void main(String[] args) throws Exception{
    	
    	
    	
    	setArguments(args);
    	ExampleTest_NavigateToGithub.navigate();
    }
    
	private static void setArguments(String[] args)
	{
    	System.out.println("Running Simple Selenium with the following Arguments:");

		for(String arg : args)
		{
			System.out.println(arg);
			
			if(arg.contains(Constants.arg_driver_path))
			{
				arg = arg.replace(Constants.arg_driver_path, "");
				System.setProperty("webdriver.chrome.driver", arg);
			}
		}
	}
}