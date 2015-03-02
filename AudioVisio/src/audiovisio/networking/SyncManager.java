package audiovisio.networking;

import java.util.Iterator;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.network.MessageListener;

import audiovisio.networking.messages.*;



public class SyncManager extends AbstractAppState implements MessageListener {
	
	private Server server;
	private Client client;
	private float syncFrequency = 0.2f;
	
	double time = 0;
	double minDelay = Double.MIN_VALUE;
	private double maxDelay = 0.7;
	
	float timer = 0f;
	LinkedList<PhysicsSyncMessage> messageQueue = new LinkedList<PhysicsSyncMessage>();
	
	Application app;
	
	public SyncManager(Application app, Server server){
		this.app = app;
		this.server = server;
	}
	
	public SyncManager(Application app, Client client) {
		this.app = app;
		this.client = client;
	}
	
	/**
	 * updates the manager, sends messages to the client in order.
	 * or syncs the info on the server periodically.
	 * @param tpf time per frame
	 */
	@Override
	public void update(float tpf){
		time += tpf;
		
		if (time < 0){
			time = 0;//prevent overflow;
		}
		
		if(client != null){
			Iterator<PhysicsSyncMessage> iter = messageQueue.iterator();
			while(iter.hasNext()){
				PhysicsSyncMessage message = iter.next();
				handleMessage(message);
				iter.remove();
			}
		} else if (server != null){
			timer += tpf;
			if(timer >= syncFrequency){
				sendSyncData();
				timer = 0;
			}
		}
	}
	
	public void addObject(long id, Object object){
		syncObjects.put(id, object);
	}
	
	public void removeObject()

}
