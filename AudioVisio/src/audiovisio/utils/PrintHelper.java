package audiovisio.utils;

import com.jme3.math.Vector3f;

import java.text.DecimalFormat;

/**
 * Created by Taylor Premo on 3/10/2015.
 */
public class PrintHelper {
    public static final String DEFAULT_FLOAT_FORMAT = "000.00";

    public static String printFormat(String format, float value){
        DecimalFormat formatter = new DecimalFormat(format);
        return formatter.format(value);
    }

    public static String printFormat(float value){
        return printFormat(DEFAULT_FLOAT_FORMAT, value);
    }

    public static String printVector3f(Vector3f vec){
        return "[" + printFormat(vec.x) + ", " + printFormat(vec.y) + ", " + printFormat(vec.z) + "]";
    }

}
