package com.eshop.common.util.script;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class ExcuteSysScript {
	private static final Logger logger = Logger.getLogger(ExcuteSysScript.class);

	// 执行windows脚本
	public static void callCmd(String scriptFilePath) {
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec("cmd.exe /C start " + scriptFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 执行linux脚本
	public static void callShell(String scriptFilePath) {
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec(scriptFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void excute(String scriptFilePath) {
		if (scriptFilePath != null) {
			File file = new File(scriptFilePath);
			if (file.exists()) {
				if (scriptFilePath.toLowerCase().endsWith(".sh")) {
					callShell(scriptFilePath);
				} else if (scriptFilePath.toLowerCase().endsWith(".bat")) {
					callCmd(scriptFilePath);
				} else {
					logger.error("unknowed script file");
				}
			} else {
				logger.error("file not found");
			}
		} else {
			logger.error("scriptPath is null");
		}
	}
}
