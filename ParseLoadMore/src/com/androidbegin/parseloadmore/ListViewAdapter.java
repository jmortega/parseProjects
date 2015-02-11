package com.androidbegin.parseloadmore;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context mContext;
	LayoutInflater inflater;
	private List<Ponencia> ponenciasList = null;
	private ArrayList<Ponencia> arraylist;
	protected int count;

	public ListViewAdapter(Context context, List<Ponencia> ponenciasList) {
		mContext = context;
		this.ponenciasList = ponenciasList;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = new ArrayList<Ponencia>();
		this.arraylist.addAll(ponenciasList);
	}

	public class ViewHolder {
		TextView titulo;

	}

	@Override
	public int getCount() {
		return ponenciasList.size();
	}

	@Override
	public Ponencia getItem(int position) {
		return ponenciasList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_item, null);
			// Locate the TextView in listview_item.xml
			holder.titulo = (TextView) view.findViewById(R.id.titulo);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextView
		holder.titulo.setText(ponenciasList.get(position).getTitulo());
		// Listen for ListView Item Click
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Send single item click data to SingleItemView Class
				Intent intent = new Intent(mContext, SingleItemView.class);
				// Pass all data 
				intent.putExtra("titulo", (ponenciasList.get(position).getTitulo()));
				
				intent.putExtra("description", (ponenciasList.get(position).getDescription()));
				
				intent.putExtra("autores", (ponenciasList.get(position).getAutores()));
				
				// Start SingleItemView Class
				mContext.startActivity(intent);
			}
		});

		return view;
	}
}