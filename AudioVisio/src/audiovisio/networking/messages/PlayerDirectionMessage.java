package audiovisio.networking.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

@Serializable
public class PlayerDirectionMessage extends AbstractMessage{
	
	private Vector3f audioDirection;
	private Vector3f visualDirection;
	
	public PlayerDirectionMessage(){
		
	}

	public PlayerDirectionMessage(Vector3f audioDirection, Vector3f visualDirection){
		this.audioDirection = audioDirection;
		this.visualDirection = visualDirection;
		
	}
	
	public Vector3f getAudioDirection(){
		return audioDirection;
	}
	
	public Vector3f getVisualDirection(){
		return audioDirection;
	}
}
