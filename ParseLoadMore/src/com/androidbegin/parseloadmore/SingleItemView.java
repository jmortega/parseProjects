package com.androidbegin.parseloadmore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleItemView extends Activity {
	// Declare Variables
	TextView txtTitulo;
	String titulo;
	
	TextView txtDescription;
	String description;
	
	TextView txtAutores;
	String autores[];
	String textAutores="";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singleitemview);
		// Retrieve data from ListViewAdapter on click event
		Intent i = getIntent();
		// Get the result of num
		titulo = i.getStringExtra("titulo");
		// Locate the TextView in singleitemview.xml
		txtTitulo = (TextView) findViewById(R.id.titulo);
		// Set the results into TextView
		txtTitulo.setText(titulo);
		
		description = i.getStringExtra("description");
		// Locate the TextView in singleitemview.xml
		txtDescription = (TextView) findViewById(R.id.description);
		// Set the results into TextView
		txtDescription.setText(description);
		
		autores = i.getStringArrayExtra("autores");
		if(autores!=null && autores.length>0){
			for(int j=0;j<autores.length;j++){
			
				textAutores = textAutores+autores[j]+"\n";
			}
		}
		// Locate the TextView in singleitemview.xml
		txtAutores = (TextView) findViewById(R.id.autores);
		// Set the results into TextView
		txtAutores.setText(textAutores);

	}
}