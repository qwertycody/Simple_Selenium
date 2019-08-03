package com.garrett_tech.simple_selenium.etc;

import java.lang.reflect.Field;

public class Initialization {
	public String driver_path = "";
	
	public void setArguments(String[] args, Class classObject, Object instanceObject, boolean outputToConsole) throws Exception
	{
		if(outputToConsole)
		{
			System.out.println("Running with the following Arguments:");
		}   
		
		for(String arg : args)
		{
			for(Field field : classObject.getFields())
			{
				String fieldName = "--" + field.getName() + "=";
				String fieldNameWithDashes = "--" + field.getName().replace("_", "-") + "=";
				
				if(arg.contains(fieldName) || arg.contains(fieldNameWithDashes))
				{
					if(outputToConsole)
					{
						System.out.println(arg);
					}
					
					arg = arg.replace(fieldName, "");
					arg = arg.replace(fieldNameWithDashes, "");
					
					Object value = field.get(instanceObject);
					field.set(instanceObject, arg);
				}
			}
		}
		
		setDriverPath(driver_path);
	}
	
	public void setDriverPath(String driverPath)
	{
		System.setProperty("webdriver.chrome.driver", driverPath);
	}
}
