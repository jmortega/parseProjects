package com.parse.techfest.util;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.techfest.R;
import com.parse.techfest.model.Ponencia;
import com.parse.techfest.view.TalkActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * A BroadcastReceiver to handle Intents sent by the AlarmManager for local notifications.
 */
public class FavoritesNotificationReceiver extends BroadcastReceiver {
  /**
   * When the notification happens, it will vibrate for 750ms after 0ms.
   */
  private static final long[] VIBRATION = { 0, 750 };

  /**
   * Shows a notification for the given talk. Clicking the notification will open the talk.
   */
  private static void showStartNotification(Ponencia talk, final Context context) {
    if (!talk.isDataAvailable()) {
      throw new RuntimeException("Talk should have been fetched.");
    }

    // Set up an Intent to open the talk, with the back button going back to the schedule.
    Intent talkIntent = new Intent(context, TalkActivity.class);
    talkIntent.setData(talk.getUri());
    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(TalkActivity.class);
    stackBuilder.addNextIntent(talkIntent);
    PendingIntent talkPendingIntent = stackBuilder.getPendingIntent(0,
        PendingIntent.FLAG_UPDATE_CURRENT);

    // Build the UI for the notification.
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
    builder.setSmallIcon(R.drawable.light_rating_important);
    builder.setContentTitle(talk.getTitle());
    builder.setContentIntent(talkPendingIntent);
    builder.setAutoCancel(true);
    builder.setVibrate(VIBRATION);
    Notification notification = builder.build();

    /*
     * Display the notification. We use the label "start" to identify this kind of notification.
     * That would be useful for cancelling the notification if we wanted.
     */
    NotificationManager manager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    manager.notify("start", talk.getObjectId().hashCode(), notification);
  }

  /**
   * This is called by Android to deliver the Intent to the receiver.
   */
  @Override
  public void onReceive(final Context context, Intent intent) {
    // Grab the data for the talk this Intent is for.
    String talkId = Ponencia.getTalkId(intent.getData());
    Ponencia.getInBackground(talkId, new GetCallback<Ponencia>() {
      @Override
      public void done(Ponencia talk, ParseException e) {
        if (talk != null) {
          showStartNotification(talk, context);
        }
      }
    });
  }
}
