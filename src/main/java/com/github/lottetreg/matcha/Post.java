package com.github.lottetreg.matcha;

import java.util.Map;

public class Post extends BaseModel {
  private String slug;
  private String title;
  private String body;

  public Post(Map<String, String> data) {
    this.slug = data.get("slug");
    this.title = data.get("title");
    this.body = data.get("body");
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
