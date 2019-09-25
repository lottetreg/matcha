package com.github.lottetreg.matcha;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class BaseModelTest {
//  @Ignore
//  public class Post extends BaseModel {
//    private String slug;
//
//    public Post() {}
//
//    public String getSlug() {
//      return this.slug;
//    }
//  }

  // can create and write to /posts.csv file from in here?

  @Test
  public void itReturnsAListOfAllObjects() {
    List<Post> posts = BaseModel.all(Post.class);

    assertEquals("how-to-do-something", posts.get(0).slug);
    assertEquals("How to Do Something", posts.get(0).title);
    assertEquals("Have you ever wanted to know how to do something?", posts.get(0).body);
  }
}
