package com.github.lottetreg.matcha;

import java.util.Map;

public class Post extends BaseModel {
  public String slug; // have to be public to allow for setting by BaseModel (like attr_accessible?)
  public String title;
  public String body;

  public Post(Map<String, String> data) {
    super(data);
  }

  public String getSlug() {
    return this.slug;
  }

  public String getTitle() {
    return this.title;
  }

  public String getBody() {
    return this.body;
  }
}
