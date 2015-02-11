package com.parse.techfest.view;

import com.parse.techfest.R;
import com.parse.techfest.model.Favorites;
import com.parse.techfest.model.Ponencia;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * An ArrayAdapter to handle a list of Talks.
 */
public class TalkListAdapter extends ArrayAdapter<Ponencia> {
  public TalkListAdapter(Context context) {
    super(context, 0);
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    // This is all pretty standard code for setting up an Android view.
    
    if (view == null) {
      view = View.inflate(getContext(), R.layout.list_item_talk, null);
    }

    final Ponencia talk = getItem(position);

    TextView titleView = (TextView) view.findViewById(R.id.title);
    TextView dateView = (TextView) view.findViewById(R.id.start_date);
    titleView.setText(talk.getTitle());
    dateView.setText(talk.getFechaDateTime().substring(0,10));
    
    final ImageButton favoriteButton = (ImageButton) view.findViewById(R.id.favorite_button);
    if (Favorites.get().contains(talk)) {
      favoriteButton.setImageResource(R.drawable.light_rating_important);
    } else {
      favoriteButton.setImageResource(R.drawable.light_rating_not_important);
    }
    favoriteButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        Favorites favorites = Favorites.get();
        if (favorites.contains(talk)) {
          favorites.remove(talk);
          favoriteButton.setImageResource(R.drawable.light_rating_not_important);
        } else {
          favorites.add(talk);
          favoriteButton.setImageResource(R.drawable.light_rating_important);
        }
        favorites.save(getContext());
      }
    });
    favoriteButton.setFocusable(false);
    
    if (talk.isAlwaysFavorite()) {
      favoriteButton.setVisibility(View.GONE);
    } else {
      favoriteButton.setVisibility(View.VISIBLE);
    }
    
    if (talk.isAlwaysFavorite()) {
      view.setBackgroundColor(Color.rgb(255, 255, 245));
    } else {
      view.setBackgroundColor(Color.rgb(245, 245, 245));
    }
    
    return view;
  }
}
