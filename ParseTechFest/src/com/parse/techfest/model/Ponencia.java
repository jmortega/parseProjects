package com.parse.techfest.model;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;

/**
 * A talk being given at Parse Developer Day.
 */
@ParseClassName("Ponencia")
public class Ponencia extends ParseObject {
  /**
   * Wraps a FindCallback so that we can use the CACHE_THEN_NETWORK caching policy, but only call
   * the callback once, with the first data available.
   */
  private abstract static class PonenciaFindCallback extends FindCallback<Ponencia> {
    private boolean isCachedResult = true;
    private boolean calledCallback = false;

    @Override
    public void done(List<Ponencia> objects, ParseException e) {
      if (!calledCallback) {
        if (objects != null) {
          // We got a result, use it.
          calledCallback = true;
          doneOnce(objects, null);
        } else if (!isCachedResult) {
          // We got called back twice, but got a null result both times. Pass on the latest error.
          doneOnce(null, e);
        }
      }
      isCachedResult = false;
    }

    /**
     * Override this method with the callback that should only be called once.
     */
    protected abstract void doneOnce(List<Ponencia> objects, ParseException e);
  }

  /**
   * Creates a query for talks with all the includes and cache policy set.
   */
  private static ParseQuery<Ponencia> createQuery() {
    ParseQuery<Ponencia> query = new ParseQuery<Ponencia>(Ponencia.class);
    query.orderByAscending("fechaDatetime");
    query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
    return query;
  }

  /**
   * Gets the objectId of the Talk associated with the given URI.
   */
  public static String getTalkId(Uri uri) {
    List<String> path = uri.getPathSegments();
    if (path.size() != 2 || !"talk".equals(path.get(0))) {
      throw new RuntimeException("Invalid URI for talk: " + uri);
    }
    return path.get(1);
  }

  /**
   * Returns a URI to use in Intents to represent this talk. The format is
   * parsedevday://talk/theObjectId
   */
  public Uri getUri() {
    Uri.Builder builder = new Uri.Builder();
    builder.scheme("parsedevday");
    builder.path("talk/" + getObjectId());
    return builder.build();
  }

  /**
   * Retrieves the set of all talks, ordered by time. Uses the cache if possible.
   */
  public static void findInBackground(final FindCallback<Ponencia> callback) {
    ParseQuery<Ponencia> query = Ponencia.createQuery();
    query.findInBackground(new PonenciaFindCallback() {
      @Override
      protected void doneOnce(List<Ponencia> objects, ParseException e) {
        if (objects != null) {
          // Sort the talks by start time.
        	callback.done(objects, e);
        }
      }
    });
  }

  /**
   * Gets the data for a single talk. We use this instead of calling fetch on a ParseObject so that
   * we can use query cache if possible.
   */
  public static void getInBackground(final String objectId, final GetCallback<Ponencia> callback) {
    ParseQuery<Ponencia> query = Ponencia.createQuery();
    query.whereEqualTo("objectId", objectId);
    query.findInBackground(new PonenciaFindCallback() {
      @Override
      protected void doneOnce(List<Ponencia> objects, ParseException e) {
        if (objects != null) {
          // Emulate the behavior of getFirstInBackground by using only the first result.
          if (objects.size() < 1) {
            callback.done(null, new ParseException(ParseException.OBJECT_NOT_FOUND,
                "No talk with id " + objectId + " was found."));
          } else {
            callback.done(objects.get(0), e);
          }
        } else {
          callback.done(null, e);
        }
      }
    });
  }

  public String getTitle() {
    return getString("tituloString");
  }
  
  public String getFechaDateTime() {
	    return getString("fechaDatetime");
  }

  public String getAbstract() {
    String talkAbstract = getString("descripcionString");
    if (talkAbstract == null) {
      talkAbstract = "";
    }
    return talkAbstract;
  }

  public List<Autor> getAuthors(String id) {
	  
	  //Obtain authors from Ponencia
		List<Autor> autores=new ArrayList<Autor>();
		ParseQuery<Autor> queryAuthors = new ParseQuery<Autor>("Autor");
		queryAuthors.whereContains("autor__Ponencias__bcklsFK__ONE_TO_MANY", id);
		try {
			autores = queryAuthors.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return autores;
  }
  
  public String getObjectIdStringId() {
	    return getString("objectIdStringId");
  }


  /**
   * Items like breaks and the after party are marked as "alwaysFavorite" so they always show up on
   * the Favorites tab of the schedule. We also color them slightly differently to make the UI
   * prettier.
   */
  public boolean isAlwaysFavorite() {
    return getBoolean("alwaysFavorite");
  }
}
