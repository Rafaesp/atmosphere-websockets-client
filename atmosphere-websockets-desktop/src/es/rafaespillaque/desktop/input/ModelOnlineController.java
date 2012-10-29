package es.rafaespillaque.desktop.input;

import java.util.ArrayList;

import com.badlogic.gdx.utils.FloatArray;

import es.rafaespillaque.desktop.net.UpdateMessageListener;
import es.rafaespillaque.desktop.net.WebSocket2;

public class ModelOnlineController extends ModelController {

	private String uuid;
	private ArrayList<String> movs;
	private FloatArray times;

    private int index = 0;
    private Object lock = new Object();

    public ModelOnlineController(String id) {
    	uuid = id;
    	WebSocket2.get().addUpdateMessageListener(new UpdateMessageListener() {
			
			@Override
			public void OnUpdateMessage(String id, float time, String dir) {
				if (uuid.equals(id)) {
					synchronized (lock) {
						movs.add(dir);
						times.add(time);
					}
				}
			}
		});
    	
    	movs = new ArrayList<String>();
    	times = new FloatArray();
    	
    }

    @Override
    public void update(float time) {
    	index = 0;
    	synchronized (lock) {
            while (index < times.size && times.get(index) >= times.get(index)) {
                InputEvent e = pool.obtain();
                e.timestamp = times.get(index);
                e.action = movs.get(index);
                offer(e);
                index++;
            }
            times.clear();
            movs.clear();
		}
    }

}
