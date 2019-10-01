package com.github.lottetreg.matcha;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class ModelTest {
  private static Path postsTable = Path.of("posts.csv");

  @BeforeClass
  public static void setUpPostsTable() throws IOException {
    Files.createFile(postsTable);
    List<String> lines = new ArrayList<>();
    lines.add("slug,title,body");
    lines.add("how-to-do-something,How to Do Something,Have you ever wanted to know how to do something?");
    lines.add("how-to-do-something-else,How to Do Something Else,Have you ever wanted to know how to do something else?");
    Files.write(postsTable, lines);
  }

  @AfterClass
  public static void tearDownPostsTable() throws IOException {
    Files.delete(postsTable);
  }

  @Test
  public void allReturnsAListOfAllResourcesForAClass() {
    List<Post> posts = Model.all(Post.class);

    assertEquals("how-to-do-something", posts.get(0).slug);
    assertEquals("How to Do Something", posts.get(0).title);
    assertEquals("Have you ever wanted to know how to do something?", posts.get(0).body);
    assertEquals("how-to-do-something-else", posts.get(1).slug);
    assertEquals("How to Do Something Else", posts.get(1).title);
    assertEquals("Have you ever wanted to know how to do something else?", posts.get(1).body);
  }

  @Test
  public void findByReturnsAResourceWithAGivenAttribute() {
    Post post = Model.findBy(Post.class, "slug", "how-to-do-something-else");

    assertEquals("how-to-do-something-else", post.slug);
    assertEquals("How to Do Something Else", post.title);
    assertEquals("Have you ever wanted to know how to do something else?", post.body);
  }

  @Test
  public void createPersistsTheObjectAndReturnsIt() {
    Post post = new Post(
        Map.of(
            "slug", "how-to-do-another-thing",
            "title", "How to Do Another Thing",
            "body", "Have you ever tried to do another thing?"
        )
    );

    Post createdPost = Model.create(post);

    assertEquals("how-to-do-another-thing", createdPost.slug);
    Post postFromDB = Model.findBy(Post.class, "slug", "how-to-do-another-thing");
    assertEquals("how-to-do-another-thing", postFromDB.slug);
    assertEquals("How to Do Another Thing", postFromDB.title);
    assertEquals("Have you ever tried to do another thing?", postFromDB.body);
  }
}
