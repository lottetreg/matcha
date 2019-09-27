package com.github.lottetreg.matcha;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class BaseModelTest {
  static Path postsTable = Path.of("posts.csv");

  @BeforeClass
  public static void setUpPostsTable() throws IOException {
    Files.createFile(postsTable);
    List<String> lines = new ArrayList<>();
    lines.add("slug,title,body");
    lines.add("how-to-do-something,How to Do Something,Have you ever wanted to know how to do something?");
    Files.write(postsTable, lines);
  }

  @AfterClass
  public static void tearDownPostsTable() throws IOException {
    Files.delete(postsTable);
  }

  @Test
  public void itReturnsAListOfAllResourcesForAClass() {
    List<Post> posts = BaseModel.all(Post.class);

    assertEquals("how-to-do-something", posts.get(0).slug);
    assertEquals("How to Do Something", posts.get(0).title);
    assertEquals("Have you ever wanted to know how to do something?", posts.get(0).body);
  }

  @Test
  public void itReturnsAResourcesByAGivenAttribute() {
    Post post = BaseModel.findBy(Post.class, "slug", "how-to-do-something");

    assertEquals("how-to-do-something", post.slug);
    assertEquals("How to Do Something", post.title);
    assertEquals("Have you ever wanted to know how to do something?", post.body);
  }
}
