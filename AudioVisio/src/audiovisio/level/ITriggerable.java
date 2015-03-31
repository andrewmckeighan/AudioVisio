package audiovisio.level;

import java.util.Map;
import java.util.Set;

/**
 * Level Items that need to link to other objects (i.e. {@link audiovisio.entities.InteractableEntity}),
 * need to implement this interface. It will be used by level to get and resolve dependencies
 * on other objects.
 *
 * @author Matt Gerst
 */
public interface ITriggerable {

    /**
     * Get a list of IDs that this item links to. This is used by the
     * level during loading to create a map of objects to their
     * dependencies.
     *
     * @return List of IDs to link to
     */
    Set<Long> getLinked();

    /**
     * Used by the level when the dependencies have been resolved.
     *
     * @param links A map of Ids to Objects that were requested
     */
    void resolveLinks( Map<Long, ITriggerable> links );

    /**
     * Link this item to another item
     *
     * @param id The id to link to
     */
    void link( Long id );

    void update( Boolean state );

}
