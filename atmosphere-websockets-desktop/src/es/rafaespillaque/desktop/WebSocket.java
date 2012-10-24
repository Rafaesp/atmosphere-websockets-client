package es.rafaespillaque.desktop;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.WebSocketListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

public class WebSocket{
	
	private static WebSocket websocket;
	private com.ning.http.client.websocket.WebSocket ws;
	private String uuid;
	
	private WebSocket(){
		BaseWebSocketTextListener baseWS = new BaseWebSocketTextListener(){

			@Override
			public void onMessage(String message) {
				JsonParser parser = new JsonParser();
				JsonElement jElement = parser.parse(message);
				JsonObject jObj = jElement.getAsJsonObject();
				if(jObj.get("type").getAsString().equals("uuid")){
					uuid = jObj.get("uuid").getAsString();
					websocket.removeWebSocketListener(this);
				}
			}
			
		};
		
		AsyncHttpClient c = new AsyncHttpClient();
		try {
			ws = c.prepareGet("ws://127.0.0.1:8080/atmosphere-websockets")
			        .execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(baseWS).build()).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void init(){
		if(websocket == null){
			websocket = new WebSocket();
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
		System.out.println("Enviando: "+message);
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
	
	public String getUUID(){
		return uuid;
	}

}
