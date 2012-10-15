package es.rafaespillaque.desktop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import es.rafaespillaque.desktop.input.InputEvent;
import es.rafaespillaque.desktop.input.ModelController;
import es.rafaespillaque.desktop.input.ModelJSONController;

public class Model {
	public static final float VELOCITY = 120f;
	public Vector2 pos = new Vector2(0, 0);
	public Vector2 vel = new Vector2(0, 0);
	private float stateTime = 0f;
	private boolean local;
	
	private ModelController controller;
	
	public Model(boolean local) {
		this.local = local;
//		controller = new ModelMouseController();
		controller = new ModelJSONController();
	}
	
	public void update(float dt, float time) {
		stateTime += dt;
		controller.update(time);
		InputEvent event;
		while((event = controller.poll()) != null) {
		    switch (event.action) {
            case LEFT:
                pos.x += -1f * dt * VELOCITY;
                break;
            case RIGHT:
                pos.x += 1f * dt * VELOCITY;
                break;
            default:
                break;
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
	
}
