package es.rafaespillaque.desktop.input;

import java.util.LinkedList;
import java.util.Queue;

public abstract class ModelController {
	private Queue<InputEvent> queue;
	protected InputEventPool pool;

	public ModelController() {
		queue = new LinkedList<InputEvent>();
		pool = new InputEventPool();
	}

	protected boolean offer(InputEvent e) {
		return queue.offer(e);
	}

	public InputEvent poll() {
		return queue.poll();
	}
	
	public boolean isEmpty(){
		return queue.isEmpty();
	}

	public void free(InputEvent e) {
		pool.free(e);
	}

	public abstract void update(float time);
}
