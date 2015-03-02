package audiovisio.networking.utilities;

import audiovisio.networking.messages.NetworkMessage;
import audiovisio.networking.messages.PlayerJoinMessage;
import audiovisio.networking.messages.PlayerLeaveMessage;
import audiovisio.networking.messages.PlayerListMessage;
import audiovisio.networking.messages.PlayerLocationMessage;
import audiovisio.networking.messages.PlayerSendMovementMessage;

import com.jme3.network.serializing.Serializer;

public class GeneralUtilities {
	
	public static int PORT;

	public static final float NETWORK_SYNC_FREQUENCY = 0.25f;
	
	public static void initializeSerializables(){
		// General Messages
		Serializer.registerClass(NetworkMessage.class);
		
		// Client Messages
		Serializer.registerClass(PlayerSendMovementMessage.class);
		
		// Server Messages
		Serializer.registerClass(PlayerLocationMessage.class);
		Serializer.registerClass(PlayerJoinMessage.class);
		Serializer.registerClass(PlayerLeaveMessage.class);
		Serializer.registerClass(PlayerListMessage.class);
	}
	
	public static void setPort(int portNumber){
		
		PORT = portNumber;
		
	}
	
	public static int getPort(){
		
		return PORT;
		
	}

}
