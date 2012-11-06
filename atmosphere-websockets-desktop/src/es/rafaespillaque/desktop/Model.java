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
	private float lastSentTime = 0f;
	
	public Model(String uuid, boolean local) {
		this.uuid = uuid;
		this.local = local;
		
		if(local)
			controller = new ModelMouseController();
		else
			controller = new ModelOnlineController(uuid);
		
		sBuilder = new StringBuilder();
		sBuilder.append("{type:update,");
    	sBuilder.append("uuid:");
    	sBuilder.append(uuid);
    	sBuilder.append(",actions:[");
	}
	
	public void update(float dt, float time) {
		if (!local) {
			time = time - 1;
			if (time < 0)
				time = 0;
		}
		controller.update(time);

		InputEvent event;
		while ((event = controller.poll()) != null) {
			if (event.action.equals(InputEvent.LEFT)) {
				pos.x += -1f * dt * VELOCITY;
			} else if (event.action.equals(InputEvent.RIGHT)) {
				pos.x += 1f * dt * VELOCITY;
			} else if (event.action.equals(InputEvent.UP)) {
				pos.y += 1f * dt * VELOCITY;
			} else if (event.action.equals(InputEvent.DOWN)) {
				pos.y += -1f * dt * VELOCITY;
			}
			if (local) {
				sendPos(event.action, time);
				System.out.println("Enviado " + event.action + " - " + event.timestamp);
			}
			controller.free(event);
		}

	}
	
	public void render(SpriteBatch batcher) {
		if(local)
			batcher.draw(Assets.blueCircle, pos.x, pos.y);
		else
			batcher.draw(Assets.redCircle, pos.x, pos.y);
	}

    private void sendPos(String action, float time) {
    	//FIXME Enviar cada 1 segundo se haya movido o no. Almacenar ¿InputEvents? y luego mandarlos
    	//Separar este método en varios. (init/añadir/enviar)
//    	if(time <= lastSentTime + 1){
//        	sBuilder.append("{dir:");
//        	sBuilder.append(action);
//        	sBuilder.append(",time:");
//        	sBuilder.append(time);
//        	sBuilder.append("},");
//        	
//    	}else{
    		sBuilder.append("{dir:");
        	sBuilder.append(action);
        	sBuilder.append(",time:");
        	sBuilder.append(time);
        	sBuilder.append("}");
        	
        	sBuilder.append("]}");
        	
        	WebSocket.get().sendTextMessage(sBuilder.toString());
        	lastSentTime = time;
        	sBuilder.delete(0, sBuilder.length());
        	
        	sBuilder.append("{type:update,");
        	sBuilder.append("uuid:");
        	sBuilder.append(uuid);
        	sBuilder.append(",actions:[");
//    	}
    }
	
	public void setLocal(boolean local) {
		this.local = local;
	}
	
	public String toString(){
		return String.format("%s -> {x:%f, y:%f}", uuid, pos.x, pos.y);
	}

}
