package es.rafaespillaque.desktop.input;

import java.util.ArrayList;

import com.badlogic.gdx.utils.FloatArray;

import es.rafaespillaque.desktop.RealTimeApp;
import es.rafaespillaque.desktop.net.UpdateMessageListener;
import es.rafaespillaque.desktop.net.WebSocket2;

public class ModelOnlineController extends ModelController {

	private String uuid;
	private ArrayList<String> movs;
	private FloatArray times;

    private int index = 0;
    private Object lock = new Object();
    private boolean buffering = true;

    public ModelOnlineController(String id) {
    	uuid = id;
    	WebSocket2.get().addUpdateMessageListener(new UpdateMessageListener() {
			
			@Override
			public void OnUpdateMessage(String id, float time, String dir) {
				if (uuid.equals(id)) {
					System.out.println(RealTimeApp.time-time+" - Nuevo update: "+dir + " - "+time);
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
    		if(times.size >= 2 && buffering){
    			buffering = false;
    		}
    		else if(times.size == 0 && !buffering){
    			buffering = true;
    		}
            while (!buffering && index < times.size && time >= times.get(index)) {
                InputEvent e = pool.obtain();
                e.timestamp = times.get(index);
                e.action = movs.get(index);
                System.out.println(time + "-->"+e.action + "   " +e.timestamp);
                offer(e);
                times.removeIndex(index);
                movs.remove(index);
                index++;
            }
            //FIXME Hay que borrar solo los que se usan, para ello mejor usar
            //alguna estructura que tenga un indice por abajo y un indice por arriba
            //para poder ir recortando desde abajo
		}
    }

}
