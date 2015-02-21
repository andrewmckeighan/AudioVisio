package audiovisio.gui;

import java.net.InetAddress;

public class Gui {

	public Gui(){

	}

	public void start(){

    }

    public String getIp(){
        return InetAddress.getLocalHost().getHostAddress();
    }

}
