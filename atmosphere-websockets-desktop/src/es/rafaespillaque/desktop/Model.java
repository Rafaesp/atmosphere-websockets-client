package es.rafaespillaque.desktop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ning.http.client.websocket.WebSocket;

import es.rafaespillaque.desktop.input.InputEvent;
import es.rafaespillaque.desktop.input.ModelController;
import es.rafaespillaque.desktop.input.ModelMouseController;

public class Model {
	public static final float VELOCITY = 120f;
	public Vector2 pos = new Vector2(0, 0);
	public Vector2 vel = new Vector2(0, 0);
	private float stateTime = 0f;
	private boolean local = false;
	private WebSocket ws;
	private String id;
	
	private ModelController controller;
	private StringBuilder sBuilder;
	
	public Model() {
		controller = new ModelMouseController();
//		controller = new ModelJSONController();
		
		sBuilder = new StringBuilder();
	}
	
	public void update(float dt, float time) {
		stateTime += dt;
		controller.update(time);
		InputEvent event;
		while((event = controller.poll()) != null) {
		    if(event.action == InputEvent.LEFT) {
                pos.x += -1f * dt * VELOCITY;
		    }else if(event.action == InputEvent.RIGHT){
                pos.x += 1f * dt * VELOCITY;
            }
		    sendPos();
		    controller.free(event);
		}
//		pos.y += dir.y * dt * VELOCITY;
	}
	
	public void render(SpriteBatch batcher) {
		if(local)
			batcher.draw(Assets.blueCircle, pos.x, pos.y);
		else
			batcher.draw(Assets.redCircle, pos.x, pos.y);
	}

    private void sendPos() {
    	sBuilder.append("{action:'update',");
    	sBuilder.append("id:'");
    	sBuilder.append(id);
    	sBuilder.append("',x:");
//    	sBuilder.append(user.d)
    }
	
	public void setLocal(boolean local) {
		this.local = local;
	}

}
