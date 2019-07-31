package com.garrett_tech.simple_selenium.etc;

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

	public String driver_username = null;
	public String driver_password = null;
	public String driver_hostname = "localhost";
	public String driver_basePath = "website";
	public String driver_protocol = "http";
	public String driver_port = "--driver-port=";
	
	public void setArguments(String[] args) throws Exception
	{
    	setArguments(args, this.getClass(), this.getInstance());
	}
}