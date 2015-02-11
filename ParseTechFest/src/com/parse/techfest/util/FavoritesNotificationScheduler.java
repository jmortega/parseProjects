package com.parse.techfest.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.techfest.model.Favorites;
import com.parse.techfest.model.Ponencia;

/**
 * Listens for changes to the set of favorite talks and sets up alarms to send out notifications a
 * few minutes before the talks start.
 */
public class FavoritesNotificationScheduler implements Favorites.Listener {
  private Context context;

  public FavoritesNotificationScheduler(Context context) {
    this.context = context;
  }

  /**
   * Creates a PendingIntent to be sent when the alarm for this talk goes off.
   */
  private PendingIntent getPendingIntent(Ponencia talk) {
    Intent intent = new Intent();
    intent.setClass(context, FavoritesNotificationReceiver.class);
    intent.setData(talk.getUri());
    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
  }

  /**
   * Schedules an alarm to go off a few minutes before this talk.
   */
  private void scheduleNotification(Ponencia talk) {
    // We need to know the time slot of the talk, so fetch its data if we haven't already.
    if (!talk.isDataAvailable()) {
    	Ponencia.getInBackground(talk.getObjectId(), new GetCallback<Ponencia>() {
        @Override
        public void done(Ponencia talk, ParseException e) {
          if (talk != null) {
            scheduleNotification(talk);
          }
        }
      });
      return;
    }

    
  }

  /**
   * Cancels any alarm scheduled for the given talk.
   */
  private void unscheduleNotification(Ponencia talk) {
    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    PendingIntent pendingIntent = getPendingIntent(talk);
    manager.cancel(pendingIntent);
  }

  @Override
  public void onFavoriteAdded(Ponencia talk) {
    scheduleNotification(talk);
  }

  @Override
  public void onFavoriteRemoved(Ponencia talk) {
    unscheduleNotification(talk);
  }
}
