package com.github.lottetreg.matcha;

import java.util.Map;

public class Post extends BaseModel {
  public String slug;
  public String title;
  public String body;

  public Post(Map<String, String> data) {
    super(data);
  }
}
