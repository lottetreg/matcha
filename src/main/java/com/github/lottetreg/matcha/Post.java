package com.github.lottetreg.matcha;

import java.util.Map;

public class Post extends BaseModel {
  public String slug; // have to be public to allow for setting by BaseModel
  public String title;
  public String body;

  public Post(Map<String, String> data) { // can hide this behaviour?
    super(data);
  }
}
