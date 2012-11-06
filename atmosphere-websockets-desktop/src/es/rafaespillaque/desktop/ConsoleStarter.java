package es.rafaespillaque.desktop;

import com.badlogic.gdx.utils.Array;

import es.rafaespillaque.desktop.net.NewPlayerMessageListener;
import es.rafaespillaque.desktop.net.WebSocket2;

public class ConsoleStarter {
	
	private Array<Model> users;
	private final float dt = 0.01f;
	private float accumulator = 0f;
	public static float time = 0f;
	private boolean init;
	private long lastFrameTime = 0;
	private float nextSyso = 0;
	private float frameTime;

	public static void main(String[] args) {
		ConsoleStarter cs = new ConsoleStarter();
		cs.buclePrincipal();
	}
	
	private void buclePrincipal(){
		users = new Array<Model>();
		init();
		lastFrameTime = System.nanoTime();
		while(true){
			long now = System.nanoTime();
			frameTime = (now - lastFrameTime) / 1000000000.0f;
			lastFrameTime = now;
			
			time += frameTime;
			accumulator += frameTime;
			update();
			
			if(time > nextSyso){
				for(int i = 0; i<users.size; ++i)
					System.out.println(users.get(i));
				nextSyso += 1f;
			}
			
			
			lastFrameTime = System.nanoTime();
		}
	}

	
	private void update() {
		synchronized (this) {
		if(!init){
			return;
		}}

		while (accumulator >= dt) {
			for(int i = 0; i<users.size; ++i){
				users.get(i).update(dt, time);
			}
			accumulator -= dt;
		}
		
	}
	
	private void init() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
//				String uuid;
				while((WebSocket2.get().getUUID()) == null){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
				}
				
				WebSocket2.get().addNewPlayerMessageListener(
						new NewPlayerMessageListener() {

							@Override
							public void OnNewPlayerMessage(String uuid) {
								System.out.println("new player");
								Model user = new Model(uuid, false);
								users.add(user);
							}
						});
				
				synchronized (ConsoleStarter.this) {
					System.out.println("init true");
					init = true;	
				}
				
			}
		}).start();
		
	}
}
