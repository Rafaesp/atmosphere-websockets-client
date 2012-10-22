package es.rafaespillaque.desktop;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.websocket.WebSocket;
import com.ning.http.client.websocket.WebSocketTextListener;
import com.ning.http.client.websocket.WebSocketUpgradeHandler;

public class RealTimeApp implements ApplicationListener {
    private SpriteBatch batcher;
    private HashMap<Integer, Model> users;
    private Model user;
    private String id;
    private Vector2 pointer;
    private final float dt = 0.01f;
    private float accumulator = 0f;
    private WebSocket websocket;
    private float time = 0f;
    

    @Override
    public void create() {
        batcher = new SpriteBatch();
        users = new HashMap<Integer, Model>();
        user = new Model();
        user.setLocal(true);
        user.pos.set(0, Gdx.graphics.getHeight() / 2);
        pointer = new Vector2();

        Assets.load();

        try {
            initWebSocket();
            user.setWebSocket(websocket);
        } catch (InterruptedException e) {
            System.out.println("int " + e);
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ex " + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("io " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        float frameTime = Gdx.graphics.getDeltaTime();
        time += frameTime;
        accumulator += frameTime;
        update(frameTime);
        present();
    }

    private void update(float frameTime) {
        
        while (accumulator >= dt) {
            user.update(dt, time);
            accumulator -= dt;
        }

    }

    private void present() {
        GL10 gl = Gdx.graphics.getGL10();
        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        user.render(batcher);
        for (Model user : users.values()) {
            user.render(batcher);
        }
        batcher.end();
    }

    private void initWebSocket() throws InterruptedException, ExecutionException, IOException {
        AsyncHttpClient c = new AsyncHttpClient();
        websocket = c.prepareGet("ws://127.0.0.1:8080/atmosphere-websockets-server")
                .execute(new WebSocketUpgradeHandler.Builder().addWebSocketListener(new WebSocketTextListener() {

                    public void onMessage(String message) {
                        System.out.println("Message Received: " + message);
                        
                    }

                    public void onOpen(WebSocket websocket) {
                        System.out.println("WebSocket Opened");
                    }

                    @Override
                    public void onClose(WebSocket websocket) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onFragment(String fragment, boolean last) {

                    }
                }).build()).get();
    }

    private void updatePos(int newPosId, String json) {
        // try {
        // float x = Float.parseFloat(jsonObject.getString("x"));
        // float y = Float.parseFloat(jsonObject.getString("y"));
        // Model m = new Model(false);
        // m.pos.set(x, y);
        // users.put(newPosId, m);
        //
        //
        // } catch (NumberFormatException e) {
        // e.printStackTrace();
        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
    }

    @Override
    public void resize(int arg0, int arg1) {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void pause() {
    }
}
