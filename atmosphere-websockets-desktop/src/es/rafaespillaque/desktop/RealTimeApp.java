package es.rafaespillaque.desktop;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RealTimeApp implements ApplicationListener {
	private SpriteBatch batcher;
	private HashMap<Integer, Model> users;
	private Model user;
	private Vector2 pointer;
	private final float dt = 0.01f;
	private float accumulator = 0f;
	private float time = 0f;
	
	@Override
	public void create() {
		batcher = new SpriteBatch();
		users = new HashMap<Integer, Model>();
		user = new Model();
		user.setLocal(true);
		user.pos.set(0, Gdx.graphics.getHeight() / 2);
		pointer = new Vector2();

		Assets.load();
		WebSocket.init();
		
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

		while (accumulator >= dt) {
			user.update(dt, time);
			accumulator -= dt;
		}

	}

	private void present() {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batcher.begin();
		user.render(batcher);
		for (Model user : users.values()) {
			user.render(batcher);
		}
		batcher.end();
	}

	private void updatePos(int newPosId, String json) {
		// try {
		// float x = Float.parseFloat(jsonObject.getString("x"));
		// float y = Float.parseFloat(jsonObject.getString("y"));
		// Model m = new Model(false);
		// m.pos.set(x, y);
		// users.put(newPosId, m);
		//
		//
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
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
