package com.garrett_tech.simple_selenium.etc;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	public static String driver_protocol = "http";
	public static String driver_hostname = "localhost";
	public static String driver_port = "8080";
	public static String driver_basePath = "website";

	public static final String token_replace = "{TOKEN_REPLACE}";

	public static String driver_username = null;
	public static String driver_password = null;

	public static final String actionType_click = "click";
	public static final String actionType_fill = "fill";
	public static final String actionType_select_dropdown = "select_dropdown";

	public static final String arg_driver_path = "--driver-path=";
	public static final String arg_driver_username = "--driver-username=";
	public static final String arg_driver_password = "--driver-password=";
	public static final String arg_driver_hostname = "--driver-hostname=";
	public static final String arg_driver_basePath = "--driver-basePath=";
	public static final String arg_driver_protocol = "--driver-protocol=";
	public static final String arg_driver_port = "--driver-port=";

	public static List<String> getArgs() {
		return new ArrayList<String>() {
			{
				this.add(arg_driver_path);
			}
		};
	}
	
	public static void setArguments(String[] args)
	{
    	System.out.println("Running with the following Arguments:");

		for(String arg : args)
		{
			System.out.println(arg);
			
			if(arg.contains(Constants.arg_driver_path))
			{
				arg = arg.replace(Constants.arg_driver_path, "");
				System.setProperty("webdriver.chrome.driver", arg);
			}

			if(arg.contains(Constants.arg_driver_username))
			{
				arg = arg.replace(Constants.arg_driver_username, "");
				Constants.driver_username = arg;
			}
			
			if(arg.contains(Constants.arg_driver_password))
			{
				arg = arg.replace(Constants.arg_driver_password, "");
				Constants.driver_password = arg;
			}
			
			if(arg.contains(Constants.arg_driver_hostname))
			{
				arg = arg.replace(Constants.arg_driver_hostname, "");
				Constants.driver_hostname = arg;
			}
			
			if(arg.contains(Constants.arg_driver_basePath))
			{
				arg = arg.replace(Constants.arg_driver_basePath, "");
				Constants.driver_basePath = arg;
			}
			
			if(arg.contains(Constants.arg_driver_protocol))
			{
				arg = arg.replace(Constants.arg_driver_protocol, "");
				Constants.driver_protocol = arg;
			}
			
			if(arg.contains(Constants.arg_driver_port))
			{
				arg = arg.replace(Constants.arg_driver_port, "");
				Constants.driver_port = arg;
			}
		}
	}
}