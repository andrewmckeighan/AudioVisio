package audiovisio.rsle.editor;

import java.lang.annotation.*;

/**
 * @author Matt Gerst
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RSLESetter {
    String value();
}
