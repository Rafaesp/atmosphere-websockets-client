package es.rafaespillaque.desktop.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import es.rafaespillaque.desktop.input.InputEvent.Direction;

public class ModelMouseController extends ModelController{
    private Vector2 pointer;

    public ModelMouseController() {
        pointer = new Vector2();
    }
    
    public void update(float time) {
        if (Gdx.input.isTouched()) {
            
            pointer.set(Gdx.input.getX(), Gdx.input.getY());
            InputEvent event = pool.obtain();
            event.timestamp = time;
            
            if (pointer.x > 3 * Gdx.graphics.getWidth() / 4) {
                event.action = Direction.RIGHT;
            }
            if (pointer.x < Gdx.graphics.getWidth() / 4) {
                event.action = Direction.LEFT;
            }
            if (pointer.y > 3 * Gdx.graphics.getHeight() / 4) {
                event.action = Direction.DOWN;
            }
            if (pointer.y < Gdx.graphics.getHeight() / 4) {
                event.action = Direction.UP;
            }
            
            offer(event);
        }
    }
    
    
}
