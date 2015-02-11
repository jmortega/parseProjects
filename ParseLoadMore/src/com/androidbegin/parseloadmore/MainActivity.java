package com.androidbegin.parseloadmore;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends Activity  {
	// Declare Variables
	ListView listview;
	List<ParseObject> ponencias;
	List<ParseObject> autores;
	ProgressDialog mProgressDialog;
	ListViewAdapter adapter;
	private List<Ponencia> ponenciasList = null;
	// Set the limit of objects to show
	private int limit = 10;

	private Spinner spinner;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from listview_main.xml
		setContentView(R.layout.listview_main);
		
		spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("12/02/2015");
        list.add("13/02/2015");
         
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                     (this, android.R.layout.simple_spinner_item,list);
                      
        dataAdapter.setDropDownViewResource
                     (android.R.layout.simple_spinner_dropdown_item);
                      
        spinner.setAdapter(dataAdapter);
         
        // Spinner item selection Listener  
        addListenerOnSpinnerItemSelection();
        
		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
	}
	

	public void addListenerOnSpinnerItemSelection(){
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				if(position==1){
	        		Intent intent = new Intent(MainActivity.this, OtherActivity.class);
	        	    startActivity(intent);
	        	}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
        	

        	
        	
        
     	});
	}
	
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(MainActivity.this);
			// Set progressdialog title
			mProgressDialog.setTitle("Parse t3chfest");
			// Set progressdialog message
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Create the array
			ponenciasList = new ArrayList<Ponencia>();
			try {
				// Locate the class table named "TestLimit" in Parse.com
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						"Ponencia");
				query.orderByAscending("createdAt");
				query.whereContains("fechaDatetime", "02/12/2015");

				// Set the limit of objects to show
				query.setLimit(limit);
				ponencias = query.find();
				for (ParseObject ponencia : ponencias) {
					Ponencia map = new Ponencia();
					map.setTitulo((String) ponencia.get("tituloString"));
					
					
					//Obtain authors from Ponencia
					
					ParseQuery<ParseObject> queryAuthors = new ParseQuery<ParseObject>("Autor");
					queryAuthors.whereContains("autor__Ponencias__bcklsFK__ONE_TO_MANY", ponencia.get("objectIdStringId").toString());
					autores = queryAuthors.find();
					String array_autores[]=new String[autores.size()];
					int i=0;
					for (ParseObject autor : autores) {
						array_autores[i]=(String) autor.get("nombreString");
						i++;
					}
					
					map.setAutores(array_autores);
					map.setDescription((String) ponencia.get("descripcionString"));
					ponenciasList.add(map);
				}
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Locate the ListView in listview.xml
			listview = (ListView) findViewById(R.id.listview);
			// Pass the results into ListViewAdapter.java
			adapter = new ListViewAdapter(MainActivity.this, ponenciasList);
			// Binds the Adapter to the ListView
			listview.setAdapter(adapter);
			// Close the progressdialog
			mProgressDialog.dismiss();
			// Create an OnScrollListener
			listview.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) { // TODO Auto-generated method stub
					int threshold = 1;
					int count = listview.getCount();
					
					if (scrollState == SCROLL_STATE_IDLE) {
						if (listview.getLastVisiblePosition() >= count
								- threshold) {
							// Execute LoadMoreDataTask AsyncTask
							new LoadMoreDataTask().execute();
						}
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

				}

			});
		}

		private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// Create a progressdialog
				mProgressDialog = new ProgressDialog(MainActivity.this);
				// Set progressdialog title
				mProgressDialog.setTitle("Parse t3chfest");
				// Set progressdialog message
				mProgressDialog.setMessage("Loading...");
				mProgressDialog.setIndeterminate(false);
				// Show progressdialog
				mProgressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// Create the array
				ponenciasList = new ArrayList<Ponencia>();
				try {
					// Locate the class table named "TestLimit" in Parse.com
					ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
							"Ponencia");
					query.orderByAscending("createdAt");
					query.whereContains("fechaDatetime", "02/12/2015");
					// Add 10 results to the default limit
					query.setLimit(limit += 10);
					ponencias = query.find();
					for (ParseObject ponencia : ponencias) {
						Ponencia map = new Ponencia();
						map.setTitulo((String) ponencia.get("tituloString"));
						
						//Obtain authors from Ponencia
						
						ParseQuery<ParseObject> queryAuthors = new ParseQuery<ParseObject>("Autor");
						queryAuthors.whereContains("autor__Ponencias__bcklsFK__ONE_TO_MANY", ponencia.get("objectIdStringId").toString());
						autores = queryAuthors.find();
						String array_autores[]=new String[autores.size()];
						int i=0;
						for (ParseObject autor : autores) {
							array_autores[i]=(String) autor.get("nombreString");
							i++;
						}
						
						map.setAutores(array_autores);
						
						map.setDescription((String) ponencia.get("descripcionString"));
						ponenciasList.add(map);
					}
				} catch (ParseException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// Locate listview last item
				int position = listview.getLastVisiblePosition();
				// Pass the results into ListViewAdapter.java
				adapter = new ListViewAdapter(MainActivity.this, ponenciasList);
				// Binds the Adapter to the ListView
				listview.setAdapter(adapter);
				// Show the latest retrived results on the top
				listview.setSelectionFromTop(position, 0);
				// Close the progressdialog
				mProgressDialog.dismiss();
			}
		}

	}

}