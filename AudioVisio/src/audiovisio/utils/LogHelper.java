package audiovisio.utils;

import java.util.logging.*;

public class LogHelper {
	private static Logger LOGGER = Logger.getLogger("AudioVisio");
    private static final boolean DUMP_STACK = true;
    private static final Level LEVEL = Level.WARNING;

    public static void finest(String msg) {LogHelper.LOGGER.log(Level.FINEST, msg);}

    public static void finer(String msg) {LogHelper.LOGGER.log(Level.FINER, msg);}

	public static void fine(String msg) {
        LogHelper.LOGGER.log(Level.FINE, msg);
	}

	public static void info(String msg) {
        LogHelper.LOGGER.log(Level.INFO, msg);
	}

	public static void info(String msg, Exception e) {
        LogHelper.LOGGER.log(Level.INFO, msg, e);
	}

	public static void warn(String msg) {
        LogHelper.LOGGER.log(Level.WARNING, msg);
        if(LogHelper.DUMP_STACK) {
            Thread.dumpStack();
        }
	}

	public static void warn(String msg, Exception e) {
        LogHelper.LOGGER.log(Level.WARNING, msg, e);
	}

	public static void severe(String msg) {
        LogHelper.LOGGER.log(Level.SEVERE, msg);
        if(LogHelper.DUMP_STACK) {
            Thread.dumpStack();
        }
	}

	public static void severe(String msg, Exception e) {
        LogHelper.LOGGER.log(Level.SEVERE, msg, e);
	}

	public static void setLevel(Level level) {
        LogHelper.LOGGER.setLevel(level);
	}

    public static void init() {
        LogHelper.LOGGER.setLevel(LogHelper.LEVEL);
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS [%4$s]: [%5$s%6$s]%n");
    }
}
