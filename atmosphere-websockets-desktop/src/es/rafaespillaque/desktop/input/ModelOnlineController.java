package es.rafaespillaque.desktop.input;

import es.rafaespillaque.desktop.BaseWebSocketTextListener;
import es.rafaespillaque.desktop.WebSocket;
import es.rafaespillaque.desktop.input.InputEvent;

public class ModelOnlineController extends ModelController {

    private float[] deltas;

    private int index = 0;
    private WebSocket ws;

    public ModelOnlineController() {
    	ws = WebSocket.get();
    	ws.addWebSocketListener(new BaseWebSocketTextListener(){

			@Override
			public void onMessage(String message) {
				//TODO :) parsear message y añadir a lista deltas y actions :)
			}
    		
    	});
//        float[] deltas2 = { 0.01f, 0.05f, 0.1f, 0.2f, 0.8f, 1f, 3f, 3.02f, 3.1f, 4f, 4.1f, 4.5f, 4.6f, 
//                4.7f, 4.8f, 4.95644f, 4.956445f, 5.00001f, 5.00002f,
//                5.5f, 6.0f, 6.0001f, 100f };
//        deltas = deltas2;
    }

    @Override
    public void update(float time) {
//        while (time >= deltas[index]) {
//            index++;
//            InputEvent e = pool.obtain();
//            e.action = InputEvent.RIGHT;
//            offer(e);
//        }
    }

}
