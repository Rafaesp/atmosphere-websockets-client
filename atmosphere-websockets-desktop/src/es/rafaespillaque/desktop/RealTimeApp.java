package es.rafaespillaque.desktop;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import es.rafaespillaque.desktop.net.NewPlayerMessageListener;
import es.rafaespillaque.desktop.net.WebSocket;
import es.rafaespillaque.desktop.net.WebSocket2;

public class RealTimeApp implements ApplicationListener {
	private SpriteBatch batcher;
	private LinkedList<Model> users;
	private Model user;
	private final float dt = 0.01f;
	private float accumulator = 0f;
	private float time = 0f;
	private boolean init = false;
	
	@Override
	public void create() {
		init();
		Assets.load();
	}

	private void init() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String uuid;
				while((uuid = WebSocket.get().getUUID()) == null){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
				}
				
				batcher = new SpriteBatch();
				users = new LinkedList<Model>();
				user = new Model(uuid, true);
				user.pos.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
				
				while((WebSocket2.get().getUUID()) == null){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
				}
				Model m2 = new Model(uuid, false);
				m2.pos.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100);
				users.add(m2);


				WebSocket.get().addNewPlayerMessageListener(
						new NewPlayerMessageListener() {

							@Override
							public void OnNewPlayerMessage(String uuid) {
								Model m = new Model(uuid, false);
								m.pos.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 100);
								users.add(m);
							}
						});
				
				synchronized (RealTimeApp.this) {
					System.out.println("init true");
					init = true;	
				}
				
			}
		}).start();
		
	}
		
	@Override
	public void render() {
		float frameTime = Gdx.graphics.getDeltaTime();
		time += frameTime;
		accumulator += frameTime;
		update(frameTime);
		present();
	}

	private void update(float frameTime) {
		synchronized (RealTimeApp.this) {
		if(!init){
			return;
		}}

		while (accumulator >= dt) {
			user.update(dt, time);
			for(int i = 0; i<users.size(); ++i){
				Model user = users.get(i);
				user.update(dt, time);
			}
			accumulator -= dt;
		}
		
	}

	private void present() {
		synchronized (RealTimeApp.this) {
		if(!init){
			return;
		}}
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batcher.begin();
		user.render(batcher);
		for(int i = 0; i<users.size(); ++i){
			Model user = users.get(i);
			user.render(batcher);
		}
		batcher.end();
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}
}
