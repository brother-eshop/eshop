package com.eshop.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ErrorUtils {
	
	public static String getStackTraceString(Throwable ex){
		if(ex == null){
			return null;
		}
		try{
			final Writer result = new StringWriter();
	        final PrintWriter printWriter = new PrintWriter(result);
	        ex.printStackTrace(printWriter);
	        printWriter.close();
	        result.close();
	        return result.toString();
		}catch(Exception ex2){
			ex.printStackTrace();
			return null;
		}
	}

}
