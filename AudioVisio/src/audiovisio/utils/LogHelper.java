package audiovisio.utils;

import java.io.IOException;
import java.util.logging.*;

public class LogHelper {
    private static final Level LEVEL        = Level.WARNING;
    private static final int   DIVIDER_SIZE = 40;
    static FileHandler fileHandler;
    private static Logger  LOGGER     = Logger.getLogger("AudioVisio");
    private static boolean DUMP_STACK = true;

    public static void finest( String msg ){LogHelper.LOGGER.log(Level.FINEST, msg);}

    public static void finer( String msg ){LogHelper.LOGGER.log(Level.FINER, msg);}

    public static void fine( String msg ){
        LogHelper.LOGGER.log(Level.FINE, msg);
    }

    public static void info( String msg ){
        LogHelper.LOGGER.log(Level.INFO, msg);
    }

    public static void info( String msg, Exception e ){
        LogHelper.LOGGER.log(Level.INFO, msg, e);
    }

    public static void warn( String msg ){
        LogHelper.LOGGER.log(Level.WARNING, msg);
        if (LogHelper.DUMP_STACK){
            Thread.dumpStack();
        }
    }

    public static void warn( String msg, Exception e ){
        LogHelper.LOGGER.log(Level.WARNING, msg, e);
    }

    public static void severe( String msg ){
        LogHelper.LOGGER.log(Level.SEVERE, msg);
        if (LogHelper.DUMP_STACK){
            Thread.dumpStack();
        }
    }

    public static void severe( String msg, Exception e ){
        LogHelper.LOGGER.log(Level.SEVERE, msg, e);
    }

    public static void setLevel( Level level ){
        LogHelper.LOGGER.setLevel(level);

        // Because apparently java's default ConsoleHandler doesn't think
        // anybody would ever want to see FINE, and doesn't respect the
        // system property to change it's logging level, we have to do this
        // http://stackoverflow.com/questions/470430/java-util-logging-logger-doesnt-respect-java-util-logging-level
        Logger topLogger = Logger.getLogger("");

        Handler consoleHandler = null;

        for (Handler handler : topLogger.getHandlers()){
            if (handler instanceof ConsoleHandler){
                consoleHandler = handler;
                break;
            }
        }

        if (consoleHandler == null){
            consoleHandler = new ConsoleHandler();
            topLogger.addHandler(consoleHandler);
        }
        consoleHandler.setLevel(level);
    }

    public static void init(){
        LogHelper.LOGGER.setLevel(LogHelper.LEVEL);
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS [%4$s]: [%5$s%6$s]%n");
    }

    public static void init( String fileName ){
        LogHelper.LOGGER.setLevel(LogHelper.LEVEL);
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tH:%1$tM:%1$tS [%4$s]: [%5$s%6$s]%n");

        try{
            LogHelper.fileHandler = new FileHandler(fileName);
            LogHelper.LOGGER.addHandler(LogHelper.fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            LogHelper.fileHandler.setFormatter(formatter);

            LogHelper.LOGGER.info("writing to file: " + fileName);
        } catch (SecurityException securityException){
            securityException.printStackTrace();
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    public static void toggleStackDump(){
        LogHelper.DUMP_STACK = !LogHelper.DUMP_STACK;
    }

    public static void divider( String msg ){
        String s = "=";
        LogHelper.divider();
        for (int i = 0; i < LogHelper.DIVIDER_SIZE / 2 - msg.length() / 2 - 2; i++){
            s += " ";
        }
        s += msg;
        while (s.length() + 1 < LogHelper.DIVIDER_SIZE){
            s += " ";
        }
        s += "=";
        LogHelper.LOGGER.log(Level.INFO, s);
        LogHelper.divider();
    }

    public static void divider(){
        String s = "";
        for (int i = 0; i < LogHelper.DIVIDER_SIZE; i++){
            s += "=";
        }
        LogHelper.LOGGER.log(Level.INFO, s);
    }
}
