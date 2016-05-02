package com.datohosting.services;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datohosting.services.MiServiceForeground.MiBinderForeground;
import com.datohosting.services.MiServiceIBinder.MiBinderIBinder;

public class MenuPrincipal extends Activity {
	
	
	private BroadcastReceiver receiver;
	private MiServiceForeground mServiceForeground;
	private MiServiceIBinder mServiceIBinder;
	private Messenger messenger = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.menu_principal);
		
		
		
		// CONFIGURACION INTENT SERVICE
		
		final ProgressBar pb_IntentService = (ProgressBar)findViewById(R.id.pb_intentservice);
		
		Button bt_IntentService = (Button) findViewById(R.id.bt_intentservice);
		bt_IntentService.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
                startService(new Intent(MenuPrincipal.this, MiServiceIntent.class));
			}
		});
		
		receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				pb_IntentService.setProgress(intent.getIntExtra("progreso", 0));
			}
		};
		
		registerReceiver(receiver, new IntentFilter(MiServiceIntent.INTENTSERVICE));
		
		
		
		// CONFIGURACION SERVICE
		
		Button bt_Start_Service = (Button) findViewById(R.id.bt_start_service);
		bt_Start_Service.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MenuPrincipal.this, MiService.class);
				intent.putExtra("primerplano", false);
        		startService(intent);
			}
		});
		
		Button bt_Stop_Service = (Button) findViewById(R.id.bt_stop_service);
		bt_Stop_Service.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stopService(new Intent(MenuPrincipal.this, MiService.class));
			}
		});
		
		
		
		// CONFIGURACION SERVICE FOREGROUND
		
		Button bt_Start_Service_F = (Button) findViewById(R.id.bt_start_service_foreground);
		bt_Start_Service_F.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MenuPrincipal.this, MiServiceForeground.class);
				bindService(intent, sConnectionF, Context.BIND_AUTO_CREATE);
			}
		});
		
		Button bt_Stop_Service_F = (Button) findViewById(R.id.bt_stop_service_foreground);
		bt_Stop_Service_F.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mServiceForeground != null) {
					mServiceForeground.stopForeground(true);
					unbindService(sConnectionF);
					mServiceForeground = null;
				}
			}
		});
		
		
		
		// CONFIGURACION SERVICE IBINDER
		
		final TextView texto = (TextView) findViewById(R.id.tx_service_ibinder);
		
		Button bt_Start_Service_IB = (Button) findViewById(R.id.bt_start_service_ibinder);
		bt_Start_Service_IB.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MenuPrincipal.this, MiServiceIBinder.class);
		        bindService(intent, sConnectionIB, Context.BIND_AUTO_CREATE);
			}
		});
		
		Button bt_Resultado_IB = (Button) findViewById(R.id.bt_get_result);
		bt_Resultado_IB.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mServiceIBinder != null) {
					String resultado = Integer.toString(mServiceIBinder.getResultado());
					texto.setText("Su resuldato es: " + resultado);
				}
			}
		});
		
		Button bt_Stop_Service_IB = (Button) findViewById(R.id.bt_stop_service_ibinder);
		bt_Stop_Service_IB.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mServiceIBinder != null) {
					mServiceIBinder.stopForeground(true);
					unbindService(sConnectionIB);
					mServiceIBinder = null;
				}
			}
		});
		
		
		
		// CONFIGURACION SERVICE MESSENGER
		
		Button bt_Start_Service_M = (Button) findViewById(R.id.bt_start_service_messenger);
		bt_Start_Service_M.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MenuPrincipal.this, MiServiceMessenger.class);
				bindService(intent, sConnectionM,Context.BIND_AUTO_CREATE);
			}
		});
		
		Button bt_Saluda_M = (Button) findViewById(R.id.bt_get_result_messenger);
		bt_Saluda_M.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (messenger != null) {
			        Message msg = Message.obtain(null, MiServiceMessenger.MSG_SAY_HELLO, 0, 0);
			        try {
			            messenger.send(msg);
			        } catch (RemoteException e) {}
				}
			}
		});
		
		Button bt_Stop_Service_M = (Button) findViewById(R.id.bt_stop_service_messenger);
		bt_Stop_Service_M.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (messenger != null) {;
					unbindService(sConnectionM);
					messenger = null;
				}
			}
		});
	}

	
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	

	
	
	
	
	// CONFIGURACION INTERFACE SERVICECONNECTION FOREGROUND
	private ServiceConnection sConnectionF = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MiBinderForeground binder = (MiBinderForeground) service;
			mServiceForeground = binder.getService();
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {}
	};
	
	
	
	// CONFIGURACION INTERFACE SERVICECONNECTION IBINDER
	private ServiceConnection sConnectionIB = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MiBinderIBinder binder = (MiBinderIBinder) service;
			mServiceIBinder = binder.getService();
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {}
	};
	
	
	
	// CONFIGURACION INTERFACE SERVICECONNECTION MESSENGER
	private ServiceConnection sConnectionM = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			messenger = new Messenger(service);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {}
	};	
	
	
	
}