package audiovisio.utils;

import com.jme3.math.Vector3f;

import java.text.DecimalFormat;

/**
 * Created by Taylor Premo on 3/10/2015.
 */
public class PrintHelper {
    public static final String DEFAULT_FLOAT_FORMAT = "000.00";

    public static String printVector3f( Vector3f vec ){
        if (vec == null){
            return "[null]";
        }
        return "[" + PrintHelper.printFormat(vec.x) + ", " + PrintHelper.printFormat(vec.y) + ", " + PrintHelper.printFormat(vec.z) + "]";
    }

    public static String printFormat( float value ){
        return PrintHelper.printFormat(PrintHelper.DEFAULT_FLOAT_FORMAT, value);
    }

    public static String printFormat( String format, float value ){
        DecimalFormat formatter = new DecimalFormat(format);
        return formatter.format(value);
    }

}
