package com.gruppoD.andruino;

import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//** GUI **//
	protected TextView latitudine, longitudine, bussola, isFound;
	protected Button bluetoothStatus, setLocation;
	protected Switch gpsStatus;
	//** FINE GUI **//
	
	private BussolaAndruino bussolaAndruino;
	private LocationAndruino location;
	private BluetoothAndruino bluetooth;
	private FileReaderAndruino fileReader;
		
	private String latDest = "0.0";
	private String lonDest = "0.0";
	
	StringTokenizer st;
	int count = 0;
	String stringa;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.bussolaAndruino = new BussolaAndruino(this);
		this.location = new LocationAndruino(this);
		this.bluetooth = new BluetoothAndruino("20:15:03:16:12:17");
		
		if(bluetooth.isConnected())
			bluetoothStatus.setText("Disconnettiti");
		
		fileReader = new FileReaderAndruino(this, "comandi.txt");
				
		latitudine = (TextView) findViewById(R.id.latitudine);
		longitudine = (TextView) findViewById(R.id.longitudine);
		bussola = (TextView) findViewById(R.id.bussola);
		isFound = (TextView) findViewById(R.id.isFound);
		bluetoothStatus = (Button) findViewById(R.id.bntBluetoothStatus);
		bluetoothStatus.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View view){
	        	 if(bluetoothStatus.getText().equals("Connettiti")){
		        	 if(MainActivity.this.bluetooth.isConnected()){
		        		 if(MainActivity.this.bluetooth.connect()){
		        			bluetoothStatus.setText("Disconnettiti");  
		        		 	msgDialog("Bluetooth connesso");
		        		 }else
		        			 msgDialog("Errore con la connessione bluetooth");
		        	 }else{
		        		 if(!bluetooth.hasBluetooth())
		        			 msgDialog("Bluetooth Disabilitato!");
		        		 else{
		        			 if(bluetooth.connect()){
		        				 msgDialog("Bluetooth connesso");
		        				 bluetoothStatus.setText("Disconnettiti");  
		        			 }
		        		 }
		        	 }
	        	 }else{
	        		 if(MainActivity.this.bluetooth.closeBluetooth())
	        			 bluetoothStatus.setText("Connettiti"); 
	        	 }
	         }
	       });
		gpsStatus = (Switch) findViewById(R.id.gpsStatus);
		
		setLocation = (Button) findViewById(R.id.setDestination);
		setLocation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				latDest = location.getLatitudine()+"";
				lonDest = location.getLongitudine()+"";
				msgDialog("Destinazione impostata!");
			}
		});
		
		
		final Handler myHandler = new Handler();
		final Runnable myRunnable = new Runnable() {
			   public void run() {
					try {
						if(bluetooth.isConnected()){
							if(fileReader.isLoaded()){								
								st = new StringTokenizer(stringa, ", ");
								String send = st.nextToken();
								if(Integer.parseInt(st.nextToken())/2 >= count){
									bluetooth.writeData(send);
									count++;
								}else{
									stringa = fileReader.getNextLine();
									count = 0;
								}
							}
								
						}else
								bluetooth.writeData(location.getLongitudine()+"\t"+location.getLatitudine()+"\t"+bussolaAndruino.getBussola()+"\t"+lonDest+"\t"+latDest+"\n");
							
						//latitudine.setText(location.getLatitudine());
						longitudine.setText(location.getLongitudine());
						bussola.setText(bussolaAndruino.getBussola());
						
					}catch(Exception e){
						msgDialog(e.toString());
					}
			   }
			};
		
	    Timer myTimer = new Timer();
	    myTimer.schedule(new TimerTask() {
	    	public void run(){
	    		myHandler.post(myRunnable);
	        }
	    }, 0, 5);
	}
			
	public void msgDialog(String data){
		Toast.makeText(this.getApplicationContext(), data, Toast.LENGTH_SHORT).show();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
}