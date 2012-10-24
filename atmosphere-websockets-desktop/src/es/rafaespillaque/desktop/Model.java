package es.rafaespillaque.desktop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import es.rafaespillaque.desktop.input.InputEvent;
import es.rafaespillaque.desktop.input.ModelMouseController;
import es.rafaespillaque.desktop.input.ModelOnlineController;

public class Model {
	public static final float VELOCITY = 120f;
	public Vector2 pos = new Vector2(0, 0);
	public Vector2 vel = new Vector2(0, 0);
	private float stateTime = 0f;
	private boolean local = false;
	private String id;
	
	private ModelMouseController mouse;
	private ModelOnlineController online;
	private StringBuilder sBuilder;
	
	public Model() {
		mouse = new ModelMouseController();
		online = new ModelOnlineController();
		
		sBuilder = new StringBuilder();
	}
	
	public void update(float dt, float time) {
		stateTime += dt;
		
		mouse.update(time);
		online.update(time);
		
		InputEvent event;
		while((event = mouse.poll()) != null) {
		    if(event.action == InputEvent.LEFT) {
                pos.x += -1f * dt * VELOCITY;
		    }else if(event.action == InputEvent.RIGHT){
                pos.x += 1f * dt * VELOCITY;
            }
		    sendPos(event.action);
		    mouse.free(event);
		}
//		pos.y += dir.y * dt * VELOCITY;
	}
	
	public void render(SpriteBatch batcher) {
		if(local)
			batcher.draw(Assets.blueCircle, pos.x, pos.y);
		else
			batcher.draw(Assets.redCircle, pos.x, pos.y);
	}

    private void sendPos(String action) {
    	sBuilder.delete(0, sBuilder.length());
    	sBuilder.append("{type:'update',");
    	sBuilder.append("uuid:'");
    	sBuilder.append(WebSocket.get().getUUID());
    	sBuilder.append("',dir:'");
    	sBuilder.append(action);
    	sBuilder.append("'}");
    	WebSocket.get().sendTextMessage(sBuilder.toString());
    }
	
	public void setLocal(boolean local) {
		this.local = local;
	}

}
