package com.parse.techfest;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.PushService;
import com.parse.techfest.model.Favorites;
import com.parse.techfest.model.Autor;
import com.parse.techfest.model.Ponencia;
import com.parse.techfest.util.FavoritesNotificationScheduler;
import com.parse.techfest.view.MainActivity;

import android.app.Application;

/**
 * The main app class mostly handles setting up global state, such as Parse keys.
 */
public class ParseTechfestApp extends Application {
  public void onCreate() {
    // Register subclasses for various kinds of Parse Objects.
    ParseObject.registerSubclass(Autor.class);
    ParseObject.registerSubclass(Ponencia.class);

    // Initialize Parse with the application ID and client key.
    Parse.initialize(this, "rz05IhFzx1dFvQs8Y8RW8BDdrSARJ9V2I5Gu91JK", "iNBQitoUMUBBdTaklwaDRTewgdTufvXfZqKCEuyZ");

    // Enable the Parse push notification service for remote pushes.
    PushService.setDefaultPushCallback(this, MainActivity.class);

    // Setup the listener to handle local notifications for when talks start.
    Favorites.get().addListener(new FavoritesNotificationScheduler(this));

    // Read in the favorites from the local disk on this device.
    Favorites.get().findLocally(this);
  }
}
