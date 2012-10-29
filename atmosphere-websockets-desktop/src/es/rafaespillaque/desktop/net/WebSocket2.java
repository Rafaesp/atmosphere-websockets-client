package es.rafaespillaque.desktop.net;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;

public class WebSocket2 {

	private static final String WS_URL = WebSocket.WS_URL;
	
	private static WebSocket2 websocket;
	private de.roderick.weberknecht.WebSocket ws;
	private String uuid;

	private ArrayList<UpdateMessageListener> updateListeners = new ArrayList<UpdateMessageListener>();
	private ArrayList<NewPlayerMessageListener> newPlayerListeners = new ArrayList<NewPlayerMessageListener>();
	private JsonParser parser = new JsonParser();
	
	private WebSocket2() {

		URI url;
		try {
			url = new URI(WS_URL);

			ws = new WebSocketConnection(
					url);

			ws.setEventHandler(new WebSocketEventHandler() {
				public void onOpen() {
					System.out.println("--open");
				}

				public void onMessage(WebSocketMessage message) {
					System.out.println("onMessage " + message);
					JsonElement jElement = parser.parse(message.getText());
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

				public void onClose() {
					System.out.println("--close");
				}
			});

			ws.connect();

		} catch (WebSocketException e) {
			e.printStackTrace();

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
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


	public void sendTextMessage(
			String message) {

		try {
			ws.send(message);
		} catch (WebSocketException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			ws.close();
		} catch (WebSocketException e) {
			e.printStackTrace();
		}
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
