package es.rafaespillaque.desktop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import es.rafaespillaque.desktop.input.InputEvent;
import es.rafaespillaque.desktop.input.ModelController;
import es.rafaespillaque.desktop.input.ModelMouseController;
import es.rafaespillaque.desktop.input.ModelOnlineController;
import es.rafaespillaque.desktop.net.WebSocket;

public class Model {
	public static final float VELOCITY = 120f;
	
	public String uuid;
	public Vector2 pos = new Vector2(0, 0);
	public Vector2 vel = new Vector2(0, 0);
	private boolean local = false;
	
	private ModelController controller;
	private StringBuilder sBuilder;
	
	public Model(String uuid, boolean local) {
		this.uuid = uuid;
		this.local = local;
		
		if(local)
			controller = new ModelMouseController();
		else
			controller = new ModelOnlineController(uuid);
		
		sBuilder = new StringBuilder();
	}
	
	public void update(float dt, float time) {
		controller.update(time);
		
		InputEvent event;
		while((event = controller.poll()) != null) {
		    if(event.action.equals(InputEvent.LEFT)) {
                pos.x += -1f * dt * VELOCITY;
		    }else if(event.action.equals(InputEvent.RIGHT)){
                pos.x += 1f * dt * VELOCITY;
            }
		    if(local){
		    	sendPos(event.action, time);
		    }
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

    private void sendPos(String action, float time) {
    	sBuilder.delete(0, sBuilder.length());
    	sBuilder.append("{type:update,");
    	sBuilder.append("uuid:");
    	sBuilder.append(uuid);
    	sBuilder.append(",dir:");
    	sBuilder.append(action);
    	sBuilder.append(",time:");
    	sBuilder.append(time);
    	sBuilder.append("}");
    	WebSocket.get().sendTextMessage(sBuilder.toString());
    }
	
	public void setLocal(boolean local) {
		this.local = local;
	}

}
