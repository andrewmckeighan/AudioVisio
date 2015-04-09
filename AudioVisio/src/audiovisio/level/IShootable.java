package audiovisio.level;

import com.jme3.scene.Geometry;

/**
 * Describes items in the world that are 'shootable' or can be
 * interacted with by the player. These items will be added to
 * the <code>shootables</code> node contained in the level.
 */
public interface IShootable {

    /**
     * Used to update the state of the shootable.
     *
     * @param state The state to set the shootable to.
     */
    void update( Boolean state );

    /**
     * Used to update the state of the shootable.
     */
    void update();

    /**
     * TODO: What is this used for?
     * Determine whether the shootable was updated.
     *
     * @return true if updated, false otherwise
     */
    Boolean getWasUpdated();

    /**
     * TODO: Docs for this method
     * @param wasUpdated
     */
    void setWasUpdated( boolean wasUpdated );

    /**
     * Get the geometry of the shootable.
     *
     * @return The shootable's geometry
     */
    Geometry getGeometry();
}
