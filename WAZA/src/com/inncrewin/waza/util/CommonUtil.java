package com.inncrewin.waza.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.inncrewin.waza.attributes.ElementAttributes;

public class CommonUtil {

	
	public static String getErrorXMl(String msg){
		Element root= DocumentFactory.getInstance().createElement("Error");
		root.addText(msg);
		return root.asXML();
	}
	
	public static String getStatusXMl(String msg, String status){
		Element root= null;
		if(status==null || status.equals(ElementAttributes.SUCCESS))
			root= DocumentFactory.getInstance().createElement("Success");
		else
			root= DocumentFactory.getInstance().createElement("Error");
		root.addText(msg);
		return root.asXML();
	}
	
	
	// Parses input date string with format "yyyy-MM-dd" to java.util.Date
	public static Date parse(String date) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date dayDateObj = new Date();
		try {
			dayDateObj = fmt.parse(date);
			return dayDateObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//Formats input java.util.Date to a String with format "yyyy-MM-dd"
	public static String format(Date date){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
