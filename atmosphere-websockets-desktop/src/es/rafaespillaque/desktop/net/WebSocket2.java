package es.rafaespillaque.desktop.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.WebSocketListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

public class WebSocket2 {

	private static final String WS_URL = "ws://127.0.0.1:8081/";
	private static WebSocket2 websocket;
	private com.ning.http.client.websocket.WebSocket ws;
	private String uuid;

	private ArrayList<UpdateMessageListener> updateListeners = new ArrayList<UpdateMessageListener>();
	private ArrayList<NewPlayerMessageListener> newPlayerListeners = new ArrayList<NewPlayerMessageListener>();
	private JsonParser parser = new JsonParser();

	private WebSocket2() {
		BaseWebSocketTextListener baseWS = new BaseWebSocketTextListener() {

			@Override
			public void onMessage(String message) {
				System.out.println("onMessage " + message);
				JsonElement jElement = parser.parse(message);
				JsonObject jObj = jElement.getAsJsonObject();
				String type = jObj.get("type").getAsString();

				if (type.equals("uuid")) {
					uuid = jObj.get("uuid").getAsString();
					System.out.println("uuid obtenido: " + uuid);
				} else if (type.equals("update")) {
					for (int i = 0; i < updateListeners.size(); ++i) {
						updateListeners.get(i).OnUpdateMessage(
								jObj.get("uuid").getAsString(),
								jObj.get("time").getAsFloat(),
								jObj.get("dir").getAsString());
					}
				} else if (type.equals("newplayer")) {
					for (int i = 0; i < updateListeners.size(); ++i) {
						newPlayerListeners.get(i).OnNewPlayerMessage(jObj.get("uuid").getAsString());
					}
				}
			}

			@Override
			public void onOpen(
					com.ning.http.client.websocket.WebSocket websocket) {
				System.out.println("onOpen");
			}

		};

		AsyncHttpClient c = new AsyncHttpClient();
		try {
			ws = c.prepareGet(WS_URL)
					.execute(
							new WebSocketUpgradeHandler.Builder()
									.addWebSocketListener(baseWS).build())
					.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void init() {
		websocket = new WebSocket2();
		websocket.sendTextMessage("{type=uuid}");
	}

	public static WebSocket2 get() {
		if (websocket == null) {
			init();
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

	public boolean isOpen() {
		return ws.isOpen();
	}

	public void close() {
		ws.close();
	}

	public String getUUID() {
		return uuid;
	}

	public void addNewPlayerMessageListener(NewPlayerMessageListener l) {
		newPlayerListeners.add(l);
	}

	public void addUpdateMessageListener(UpdateMessageListener l) {
		updateListeners.add(l);
	}

}
