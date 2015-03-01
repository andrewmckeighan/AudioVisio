package audiovisio.networking.utilities;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerLocationMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;

import com.jme3.network.serializing.Serializer;

public class GeneralUtilities {
	
	public static int PORT;
	
	public static void initializeSerializables(){
		Serializer.registerClass(NetworkMessage.class);
		Serializer.registerClass(PlayerSendMovementMessage.class);
		Serializer.registerClass(PlayerLocationMessage.class);
	}
	
	public static void setPort(int portNumber){
		
		PORT = portNumber;
		
	}
	
	public static int getPort(){
		
		return PORT;
		
	}

}
