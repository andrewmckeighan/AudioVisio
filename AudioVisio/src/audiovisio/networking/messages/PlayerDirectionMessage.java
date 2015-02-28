package audiovisio.networking.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

@Serializable
public class PlayerDirectionMessage extends AbstractMessage{
	
	private Vector3f direction;
	
	public PlayerDirectionMessage(){
		
	}

	public PlayerDirectionMessage(Vector3f direction){
		this.direction = direction;
		
	}
	
	public Vector3f getDirection(){
		return direction;
	}
}
