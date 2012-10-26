package es.rafaespillaque.desktop.input;

import es.rafaespillaque.desktop.net.UpdateMessageListener;
import es.rafaespillaque.desktop.net.WebSocket2;

public class ModelOnlineController extends ModelController {

	private static final int SIZE = 100;
	
	private String uuid;
    private String[] movs;
    private float[] times;

    private int count = 0;
    private int index = 0;
    private Object lock = new Object();

    public ModelOnlineController(String id) {
    	uuid = id;
    	WebSocket2.get().addUpdateMessageListener(new UpdateMessageListener() {
			
			@Override
			public void OnUpdateMessage(String id, float time, String dir) {
				if (uuid.equals(id)) {
					synchronized (lock) {
						movs[count] = dir;
						times[count] = time;
					}

					count++;
				}
			}
		});
    	
    	movs = new String[SIZE];
    	times = new float[SIZE];
    	
    }

    @Override
    public void update(float time) {
    	index = 0;
    	synchronized (lock) {
            while (time >= times[index] && index < count) {
                InputEvent e = pool.obtain();
                e.timestamp = times[index];
                e.action = movs[index];
                offer(e);
                index++;
            }
            count = 0;
		}

    }

}
