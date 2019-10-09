package com.github.lottetreg.matcha;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class ModelTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  private static Path postsTable = Path.of("posts.csv");

  @Before
  public void setUpPostsTable() throws IOException {
    Files.createFile(postsTable);
    List<String> lines = new ArrayList<>();
    lines.add("slug,title,body");
    Files.write(postsTable, lines);
  }

  @After
  public void tearDownPostsTable() throws IOException {
    Files.delete(postsTable);
  }

  private void addLinesToPosts(List<String> lines) {
    try {
      FileWriter csvWriter = new FileWriter(postsTable.toString(), true);

      lines.forEach((line) -> {
        try {
          csvWriter.append(line);
          csvWriter.append("\n");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });

      csvWriter.flush();
      csvWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void allReturnsAListOfAllResourcesForAClass() {
    List<String> lines = new ArrayList<>();
    lines.add("how-to-do-something,How to Do Something,Have you ever wanted to know how to do something?");
    lines.add("how-to-do-something-else,How to Do Something Else,Have you ever wanted to know how to do something else?");
    addLinesToPosts(lines);

    List<Post> posts = Model.all(Post.class);

    assertEquals("how-to-do-something", posts.get(0).slug);
    assertEquals("How to Do Something", posts.get(0).title);
    assertEquals("Have you ever wanted to know how to do something?", posts.get(0).body);
    assertEquals("how-to-do-something-else", posts.get(1).slug);
    assertEquals("How to Do Something Else", posts.get(1).title);
    assertEquals("Have you ever wanted to know how to do something else?", posts.get(1).body);
  }

  @Test
  public void findByReturnsAListOfAllResourcesForAClassThatMatchTheCriteria() {
    List<String> lines = new ArrayList<>();
    lines.add("how-to-do-something,How to Do Something,Have you ever wanted to know how to do something?");
    lines.add("how-to-do-something-else,How to Do Something Else,Have you ever wanted to know how to do something else?");
    addLinesToPosts(lines);

    List<Post> posts = Model.findBy(Post.class, "slug", "how-to-do-something");

    assertEquals(1, posts.size());
    assertEquals("how-to-do-something", posts.get(0).slug);
    assertEquals("How to Do Something", posts.get(0).title);
    assertEquals("Have you ever wanted to know how to do something?", posts.get(0).body);
  }

  @Test
  public void findFirstByReturnsAResourceWithAGivenAttribute() {
    List<String> lines = new ArrayList<>();
    lines.add("how-to-do-something,How to Do Something,Have you ever wanted to know how to do something?");
    lines.add("how-to-do-something-else,How to Do Something Else,Have you ever wanted to know how to do something else?");
    addLinesToPosts(lines);

    Post post = Model.findFirstBy(Post.class, "slug", "how-to-do-something-else");

    assertEquals("how-to-do-something-else", post.slug);
    assertEquals("How to Do Something Else", post.title);
    assertEquals("Have you ever wanted to know how to do something else?", post.body);
  }


  @Test
  public void findFirstByThrowsAnExceptionIfItCannotFindAResource() {
    exceptionRule.expect(Model.RecordNotFound.class);
    exceptionRule.expectMessage("Not found: Post with slug of does-not-exist");

    Model.findFirstBy(Post.class, "slug", "does-not-exist");
  }

  @Test
  public void savePersistsTheGivenObjectAndReturnsIt() {
    Post post = new Post(
        "how-to-do-another-thing",
        "How to Do Another Thing",
        "Have you ever tried to do another thing?"
    );

    Post createdPost = Model.save(post);

    assertEquals("how-to-do-another-thing", createdPost.slug);
    Post postFromDB = Model.findFirstBy(Post.class, "slug", "how-to-do-another-thing");
    assertEquals("how-to-do-another-thing", postFromDB.slug);
    assertEquals("How to Do Another Thing", postFromDB.title);
    assertEquals("Have you ever tried to do another thing?", postFromDB.body);
  }

  @Test
  public void createPersistsAnObjectOfTheGivenClassWithTheGivenAttributesAndReturnsIt() {
    Post createdPost = Model.create(
        Post.class,
        Map.of(
            "slug", "how-to-do-another-thing",
            "title", "How to Do Another Thing",
            "body", "Have you ever tried to do another thing?"
        )
    );

    assertEquals("how-to-do-another-thing", createdPost.slug);
    Post postFromDB = Model.findFirstBy(Post.class, "slug", "how-to-do-another-thing");
    assertEquals("how-to-do-another-thing", postFromDB.slug);
    assertEquals("How to Do Another Thing", postFromDB.title);
    assertEquals("Have you ever tried to do another thing?", postFromDB.body);
  }
}
