package com.datohosting.services;

import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MiServiceIBinder extends Service {
	
	private final IBinder iBinder = new MiBinderIBinder();
	private final Random random = new Random();
	
	
	public class MiBinderIBinder extends Binder {
		public MiServiceIBinder getService() {
            return MiServiceIBinder.this;
        }
    }
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		Toast.makeText(this, "Service iniciado", Toast.LENGTH_SHORT).show();
		return iBinder;
	}
	
	
	@Override
    public void onDestroy(){
        Toast.makeText(this, "Service finalizado", Toast.LENGTH_SHORT).show();
    }

	
	public int getResultado() {
		return random.nextInt(1000);
	}
	
	
    
}