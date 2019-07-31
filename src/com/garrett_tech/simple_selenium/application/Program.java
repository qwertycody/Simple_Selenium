package com.garrett_tech.simple_selenium.application;

import com.garrett_tech.simple_selenium.etc.Constants;
import com.garrett_tech.simple_selenium.etc.Utils;
import com.garrett_tech.simple_selenium.tests.ExampleTest_NavigateToGithub;

public class Program {
		
    public static void main(String[] args) throws Exception{
    	try {
			Constants.getInstance().setArguments(args);
			ExampleTest_NavigateToGithub.navigate();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.exit(0);
    }
}