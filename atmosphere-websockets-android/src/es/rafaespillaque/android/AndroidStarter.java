package es.rafaespillaque.android;

import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;

import es.rafaespillaque.desktop.RealTimeApp;


public class AndroidStarter extends AndroidApplication {
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		initialize(new RealTimeApp(), false);
		System.out.println("dsdssdsdsdssd");
	}
}
