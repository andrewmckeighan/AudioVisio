package audiovisio.rsle.editor;

import java.lang.annotation.*;

/**
 * This indicates that RSLE should use the annotated method in
 * order to set the value of the specified attribute on an item.
 * The value of the annoation <strong>MUST</strong>be the same
 * as the name of the node the value is editing.
 *
 * @author Matt Gerst
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RSLESetter {
    String value();
}
