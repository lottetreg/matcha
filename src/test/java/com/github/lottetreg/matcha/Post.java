package com.github.lottetreg.matcha;

import org.junit.Ignore;

@Ignore
public class Post {
  public String slug;
  public String title;
  public String body;

  public Post() {}

  public Post(String slug, String title, String body) {
    this.slug = slug;
    this.title = title;
    this.body = body;
  }
}
