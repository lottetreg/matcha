package com.github.lottetreg.matcha;

import org.junit.Ignore;

import java.util.Map;

@Ignore
public class Post extends Model {
  public String slug;
  public String title;
  public String body;

  public Post(Map<String, Object> data) {
    super(data);
  }
}
