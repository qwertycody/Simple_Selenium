package com.garrett_tech.simple_selenium.etc;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Constants extends Initialization {
	
	private static Constants instance;
	
	public static Constants getInstance()
	{
		if(instance == null)
		{
			instance = new Constants();
		}
		
		return instance;
	}	

	public final String token_replace = "{TOKEN_REPLACE}";

	public final String actionType_click = "click";
	public final String actionType_fill = "fill";
	public final String actionType_select_dropdown = "select_dropdown";
	
	public File getDownloadsFolder()
	{
		return new File(driver_download_directory);
	}
	
	public File getTempFolder()
	{
		return new File(driver_temp_directory);
	}

	public String driver_temp_directory = System.getProperty("java.io.tmpdir");
	
	public String driver_test_to_run = "";
	
	public String driver_username = null;
	public String driver_password = null;
	public String driver_password2 = null;
	
	public String driver_download_directory = "";
	
	public String driver_misc_string="";
	
	public String driver_hostname = "localhost";
	public String driver_basePath = "website";
	public String driver_protocol = "http";
	public String driver_port = "8080";
	
	public String driver_prompt_on_error = "false";
	
	public Boolean promptOnError()
	{
		String trueString = "true";
		
		if(driver_prompt_on_error != null && driver_prompt_on_error.equalsIgnoreCase(trueString))
		{
			return true;
		}
		
		return false;
	}
	
	public void setArguments(String[] args) throws Exception
	{
    	setArguments(args, getClass(), getInstance(), true);
	}
	
	@Override
	public void setArguments(String[] args, Class classObject, Object instanceObject, boolean outputToConsole) throws Exception
	{
    	super.setArguments(args, classObject, instanceObject, outputToConsole);
    	super.setArguments(args, Constants.class, Constants.getInstance(), false);
	}
}