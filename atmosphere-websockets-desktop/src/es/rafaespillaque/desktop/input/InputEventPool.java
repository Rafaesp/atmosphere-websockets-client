package es.rafaespillaque.desktop.input;

import com.badlogic.gdx.utils.Pool;


public class InputEventPool extends Pool<InputEvent>{
    
    public InputEventPool() {
        super(1, 5);
    }
    
    @Override
    protected InputEvent newObject() {
        return new InputEvent();
    }
    
}
