package com.bafomdad.realfilingcabinet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LogRFC {
	
	public static final Logger LOGGER = LogManager.getLogger(RealFilingCabinet.MOD_ID);

	public static void debug(String msg) {
		
		if (ConfigRFC.debugLogger)
			LOGGER.info(msg);
	}
	
	public static void info(String msg) {
		
		LOGGER.info(msg);
	}
	
	private LogRFC() {
		
	}
}
