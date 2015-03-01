package audiovisio.networking.messages;

import java.util.Set;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Sent by the server to a newly joined client giving it a list of all
 * currently connected players.
 */
@Serializable
public class PlayerListMessage extends AbstractMessage {
	private Set<Integer> playerList;
	
	public PlayerListMessage() {}
	
	/**
	 * @param playerList A list of IDs of all currently connected players
	 */
	public PlayerListMessage(Set<Integer> playerList) {
		this.playerList = playerList;
	}
	
	public Set<Integer> getPlayerList() {
		return playerList;
	}
}
