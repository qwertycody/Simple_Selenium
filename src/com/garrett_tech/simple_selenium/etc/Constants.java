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

	public static List<String> getArgs() {
		return new ArrayList<String>() {
			{
				this.add(arg_driver_path);
			}
		};
	}
}