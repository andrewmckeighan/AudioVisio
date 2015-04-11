package audiovisio.utils;

import audiovisio.level.ILevelItem;
import audiovisio.rsle.editor.LevelNode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

import java.util.Set;

/**
 * @author Matt Gerst
 */
public class LevelUtils {
    public static LevelNode vector2node( Vector3f vec ){
        return LevelUtils.vector2node("Location", vec);
    }

    public static LevelNode vector2node( String name, Vector3f vec ){
        LevelNode location = new LevelNode(name, true);

        LevelNode x = new LevelNode("X", vec.x, false);
        LevelNode y = new LevelNode("Y", vec.y, false);
        LevelNode z = new LevelNode("Z", vec.z, false);

        location.add(x);
        location.add(y);
        location.add(z);

        location.setSourceVector(vec);

        return location;
    }

    public static LevelNode linksNode( Set<Long> ids ){
        LevelNode links = new LevelNode("Links", true);

        for (Long id : ids){
            LevelNode idNode = new LevelNode(id, false);
            links.add(idNode);
        }

        return links;
    }

    public static Vector3f getRotation( Vector3f location, ILevelItem.Direction direction, Quaternion defaultRotation, Geometry geometry ){
        if (direction == ILevelItem.Direction.EAST){
            location = location.add(0.5F, 0.0F, 0.0F);
        } else if (direction == ILevelItem.Direction.SOUTH){
            location = location.add(0.0F, 0.0F, -0.5F);
            geometry.setLocalRotation(defaultRotation);
        } else if (direction == ILevelItem.Direction.WEST){
            location = location.add(-0.5F, 0.0F, 0.0F);
        } else if (direction == ILevelItem.Direction.NORTH){
            location = location.add(0.0F, 0.0F, -0.5F);
            geometry.setLocalRotation(defaultRotation);
        }

        return location;
    }
}
