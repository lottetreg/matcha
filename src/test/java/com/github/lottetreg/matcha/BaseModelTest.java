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

  @Test
  public void itReturnsAListOfAllObjects() throws Exception {
    List<Post> posts = Post.all(Post.class, "src/test/java/com/github/lottetreg/matcha/support/posts.csv");

    assertEquals("how-to-do-something", posts.get(0).getSlug());
    assertEquals("How to Do Something", posts.get(0).getTitle());
    assertEquals("Have you ever wanted to know how to do something?", posts.get(0).getBody());
  }
}
