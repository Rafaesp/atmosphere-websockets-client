package es.rafaespillaque.desktop;

import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;

public class BaseWebSocketTextListener implements WebSocketTextListener{

	@Override
	public void onOpen(WebSocket websocket) {
	}

	@Override
	public void onClose(WebSocket websocket) {
	}

	@Override
	public void onError(Throwable t) {
	}

	@Override
	public void onMessage(String message) {
	}

	@Override
	public void onFragment(String fragment, boolean last) {
	}

}
