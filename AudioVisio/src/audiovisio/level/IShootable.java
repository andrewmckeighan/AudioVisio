package audiovisio.level;

/**
 * Created by Tain on 3/31/2015.
 */
public interface IShootable {

    void update( Boolean state );

    void update();

    Boolean getWasUpdated();

    void setWasUpdated( boolean wasUpdated );

    com.jme3.scene.Geometry getGeometry();
}
