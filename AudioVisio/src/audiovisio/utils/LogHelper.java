package audiovisio.utils;

import java.util.logging.*;

public class LogHelper {
	private static Logger LOGGER = Logger.getLogger("AudioVisio");

	public static void fine(String msg) {
		LOGGER.log(Level.FINE, msg);
	}
	
	public static void info(String msg) {
		LOGGER.log(Level.INFO, msg);
	}
	
	public static void info(String msg, Exception e) {
		LOGGER.log(Level.INFO, msg, e);
	}
	
	public static void warn(String msg) {
		LOGGER.log(Level.WARNING, msg);
	}
	
	public static void warn(String msg, Exception e) {
		LOGGER.log(Level.WARNING, msg, e);
	}
	
	public static void severe(String msg) {
		LOGGER.log(Level.SEVERE, msg);
	}
	
	public static void severe(String msg, Exception e) {
		LOGGER.log(Level.SEVERE, msg, e);
	}

	public static void setLevel(Level level) {
		LOGGER.setLevel(level);
	}

    public static void init() {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
    }
}
