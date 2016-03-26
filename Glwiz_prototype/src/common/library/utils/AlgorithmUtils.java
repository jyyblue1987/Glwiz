package common.library.utils;

import java.util.Iterator;

import org.json.JSONObject;

public class AlgorithmUtils {
	 public static String invert(String s) {
	        String temp = "";
	        for (int i = s.length() - 1; i >= 0; i--)
	            temp += s.charAt(i);
	        return temp;
     }		 
	 
	 public static void bindJSONObject(JSONObject to, JSONObject from)
	 {
		 if( to == null || from == null )
			 return;
		 
		 Iterator<String> iter = from.keys();
		 while (iter.hasNext()) {
		    String key = iter.next();		    
		    try {
		    	String value = from.optString(key, "");
		    	if( to.has(key) == false )
		    		to.put(key, value);		    	
		    } catch (Exception e) {
		        // Something went wrong!
		    }
		}
	 }
	 
	 
}
