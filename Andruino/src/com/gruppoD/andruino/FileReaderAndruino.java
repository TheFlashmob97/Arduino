package com.gruppoD.andruino;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.R.array;

public class FileReaderAndruino {
	private ArrayList<String> arrayList;
	private BufferedReader bufferedReader;
	
	private int count = 0;
	private boolean isLoaded = false;
	
	public FileReaderAndruino(MainActivity main, String path){
		arrayList = new ArrayList<String>();
		
		String row = "";
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(main.getAssets().open(path)));

            while((row = bufferedReader.readLine()) != null  ){
                
                arrayList.add(row);
                
            }
            isLoaded = true;
            main.stringa = this.arrayList.get(0);
            
            main.msgDialog("File letto con successo!");
        }catch(Exception e){
        	main.msgDialog("Errore con lettura del file" + e.toString());
        }		
	}
	
	public boolean isLoaded(){
		return isLoaded;
	}
	
	public void setIndex(int i){
		count = i;		
	}
	
	public String getNextLine(){
		if(count < arrayList.size()-1)
			count++;
		else
			count = 0;
		return arrayList.get(count);		
	}
}