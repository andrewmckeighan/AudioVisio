package audiovisio.networking.utilities;

import com.jme3.network.serializing.Serializer;

public class GeneralUtilities {
	
	public static int PORT;
	
	public static void initializeSerializables(){
		Serializer.registerClass(NetworkMessage.class);
	}
	
	public static void setPort(int portNumber){
		
		PORT = portNumber;
		
	}
	
	public static int getPort(){
		
		return PORT;
		
	}

}
