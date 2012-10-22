package es.rafaespillaque.desktop;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.WebSocketListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

public class WebSocket{
	
	private static WebSocket websocket;
	private com.ning.http.client.websocket.WebSocket ws;
	
	private WebSocket(){
		AsyncHttpClient c = new AsyncHttpClient();
		try {
			ws = c.prepareGet("ws://127.0.0.1:8080/atmosphere-websockets-server")
			        .execute(new WebSocketUpgradeHandler.Builder().build()).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static WebSocket get(){
		if(websocket == null){
			websocket = new WebSocket();
		}
		return websocket;
	}
	
	public com.ning.http.client.websocket.WebSocket sendMessage(byte[] message) {
		return ws.sendMessage(message);
	}

	public com.ning.http.client.websocket.WebSocket stream(byte[] fragment,
			boolean last) {
		return ws.stream(fragment, last);
	}

	public com.ning.http.client.websocket.WebSocket stream(byte[] fragment,
			int offset, int len, boolean last) {
		return ws.stream(fragment, offset, len, last);
	}

	public com.ning.http.client.websocket.WebSocket sendTextMessage(
			String message) {
		return ws.sendTextMessage(message);
	}

	public com.ning.http.client.websocket.WebSocket streamText(String fragment,
			boolean last) {
		return ws.streamText(fragment, last);
	}

	public com.ning.http.client.websocket.WebSocket sendPing(byte[] payload) {
		return ws.sendPing(payload);
	}

	public com.ning.http.client.websocket.WebSocket sendPong(byte[] payload) {
		return ws.sendPong(payload);
	}

	public com.ning.http.client.websocket.WebSocket addWebSocketListener(
			WebSocketListener l) {
		return ws.addWebSocketListener(l);
	}

	public com.ning.http.client.websocket.WebSocket removeWebSocketListener(
			WebSocketListener l) {
		return ws.removeWebSocketListener(l);
	}

	public boolean isOpen() {
		return ws.isOpen();
	}

	public void close() {
		ws.close();
	}

}
