package audiovisio.networking.messages;

import java.util.List;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Sent by the server to a newly joined client giving it a list of all
 * currently connected players.
 */
@Serializable
public class PlayerListMessage extends AbstractMessage {
	private List<Integer> playerList;
	
	public PlayerListMessage() {}
	
	/**
	 * @param playerList A list of IDs of all currently connected players
	 */
	public PlayerListMessage(List<Integer> playerList) {
		this.playerList = playerList;
	}
	
	public List<Integer> getPlayerList() {
		return playerList;
	}
}
