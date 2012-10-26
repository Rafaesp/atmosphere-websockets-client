package es.rafaespillaque.desktop.input;

import java.util.LinkedList;
import java.util.Queue;


public abstract class ModelController{
    private static final int MAX = 20;
    private Queue<InputEvent> queue;
    protected InputEventPool pool;
    
    public ModelController() {
        queue = new LinkedList<InputEvent>();
        pool = new InputEventPool();
    }
    
    protected boolean offer(InputEvent e) {
        if(queue.size() < MAX)
            return queue.offer(e);
        else
            return false;
    }
    
    public InputEvent poll() {
        return queue.poll();
    }
    
    public void free(InputEvent e) {
        pool.free(e);
    }
    
    public abstract void update(float time);
}
