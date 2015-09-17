package com.gruppoD.andruino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class BluetoothAndruino{
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothSocket bluetoothSocket;
	private OutputStream outputStream;
	private InputStream inputStream;
	
	private Thread workerThread;
	  
	private byte[] readBuffer;
	private int readBufferPosition;
	  	
	private boolean isConnected = false;
	private String address;
	private String ricevuto = "";
	
	private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
		
	public BluetoothAndruino(String address){
		this.address = address;
		/*if(hasBluetooth())
			connect();*/
	}
	
	//Restituisce se il dispositivo ha acceso il bluetooth
	public boolean hasBluetooth(){
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!bluetoothAdapter.isEnabled())
			return false;
		if (bluetoothAdapter == null)
			return false;
		return true;
	}
	
	public boolean isConnected(){
		return isConnected;
	}
	
	//Address è l'indirizzo MAC del dispositivo da connettere
	public boolean connect(){
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(this.address.toString());		
		bluetoothAdapter.cancelDiscovery();
		try{
			bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid);
			bluetoothSocket.connect();
			this.isConnected = true;
			return true;
			
		}catch(IOException e){
			this.closeBluetooth();
			this.isConnected = false;
		}
		this.beginListenForData();
		return false;	
	}
	
	public void readData(String data){
		this.ricevuto = data;	
		
	}
	
	/* ritorna falso se ha inviato il dato
	 * altrimenti restituisce vero
	 */
	boolean complete = false;
	
	public final boolean writeData(final String data){
		if(!this.isConnected())
			return false;

		Thread thread = new Thread() {     			
		    public void run() {
		        try{
		        	outputStream = bluetoothSocket.getOutputStream();

		    		String message = data;
		    		byte[] msgBuffer = message.getBytes();

		    		
		    		outputStream.write(msgBuffer);  	
		        	complete = true;
		        }catch (Exception e) {
		        	complete = false;
		        }
		    }   
		};
		thread.start();
		return complete;
	}
	
	private void beginListenForData() {
		final Handler handler = new Handler(); 
		final byte delimiter = 10; //This is the ASCII code for a newline character

		readBufferPosition = 0;
		readBuffer = new byte[1024];
		workerThread = new Thread(new Runnable() {
			public void run() {
				while(!Thread.currentThread().isInterrupted() && !isConnected) {
					try{
						int bytesAvailable = inputStream.available();            
						if(bytesAvailable > 0) {
							byte[] packetBytes = new byte[bytesAvailable];
							inputStream.read(packetBytes);
							for(int i=0;i<bytesAvailable;i++) {
								byte b = packetBytes[i];
								if(b == delimiter) {
									byte[] encodedBytes = new byte[readBufferPosition];
									System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
									final String data = new String(encodedBytes, "US-ASCII");
									readBufferPosition = 0;

									handler.post(new Runnable() {
										public void run() {
											dataRead(data);
										}
									});
								}else{
									readBuffer[readBufferPosition++] = b;
								}
							}
						}
					}catch(Exception e){
						isConnected = false;
					}
				}
			}
		});
		workerThread.start();
	}
	
	public String getData(){
		return this.ricevuto;
	}
	
	private void dataRead(String data){
		this.ricevuto = data;
	}
	  
	public boolean closeBluetooth(){
		try{
			outputStream.close();
			bluetoothSocket.close();
			isConnected = false;
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
