package audiovisio.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {
	private static Logger LOGGER = Logger.getLogger("AudioVisio");
	
	public static void info(String msg) {
		LOGGER.info(msg);
	}
	
	public static void warn(String msg) {
		LOGGER.log(Level.WARNING, msg);
	}
	
	public static void severe(String msg) {
		LOGGER.log(Level.SEVERE, msg);
	}
}
