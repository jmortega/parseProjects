package com.parse.techfest.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * A person speaking in a talk.
 */
@ParseClassName("Autor")
public class Autor extends ParseObject {
  public String getName() {
    return getString("nombreString");
  }

  public String getTitle() {
    return getString("title");
  }

  public String getCompany() {
    return getString("company");
  }

  public String getBio() {
    return getString("bio");
  }
  
  public String getPhotoURL() {
    return getString("photoURL");
  }

  public ParseFile getPhoto() {
    return getParseFile("photo");
  }
}
