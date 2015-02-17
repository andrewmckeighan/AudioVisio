package Server;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

public class NetworkUtils {
	//port variable
	public static final int PORT = 6000;
	
	//initializes serializable variables, (server sends serializables, not sure what this means yet)
	public static void initializeSerializables(){
		Serializer.registerClass(NetworkMessage.class);
	}
	//creates a message for the network to send
	@Serializable
	public static class NetworkMessage extends AbstractMessage{
		
		private String message;
		
		public NetworkMessage(){}
		
		public NetworkMessage(String message){
			this.message = message;
		}
		
		public String getMessage(){
			return message;
		}
	}
	//All outgoing and incoming messages must be serializable
	//This message will send a position vector for the character
	@Serializable
	public static class CharPositionMessage extends AbstractMessage{
		
		private Vector3f position;
		
		public CharPositionMessage(){
			
		}
		
		public CharPositionMessage(Vector3f position){
			this.position = position;
		}
		
		public Vector3f GetPosition(){
			return position;
		}
		
	}
	//This message will send a location and direction message;
	@Serializable
	public static class CharLocAndDirMessage extends AbstractMessage{
		
		private Vector3f location;
		private Vector3f direction;
		
		public CharLocAndDirMessage(){
			
		}
		
		public CharLocAndDirMessage(Vector3f location, Vector3f direction){
			this.location = location;
			this.direction = direction;
		}
		
		public Vector3f GetLocation(){
			return location;
		}
		
		public Vector3f GetDirection(){
			return direction;
		}
		
	}
	
}