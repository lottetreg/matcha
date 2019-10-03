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

public class CsvDatabaseTest {
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
  public void itReturnsAListOfAllRecordsFromACSV() {
    List<String> lines = new ArrayList<>();
    lines.add("how-to-do-something,How to Do Something,Have you ever wanted to know how to do something?");
    lines.add("how-to-do-something-else,How to Do Something Else,Have you ever wanted to know how to do something else?");
    addLinesToPosts(lines);
    CsvDatabase database = new CsvDatabase();

    List<Map<String, String>> records = database.select("posts");

    assertEquals("how-to-do-something", records.get(0).get("slug"));
    assertEquals("How to Do Something", records.get(0).get("title"));
    assertEquals("Have you ever wanted to know how to do something?", records.get(0).get("body"));
    assertEquals("how-to-do-something-else", records.get(1).get("slug"));
    assertEquals("How to Do Something Else", records.get(1).get("title"));
    assertEquals("Have you ever wanted to know how to do something else?", records.get(1).get("body"));
  }

  @Test
  public void itReturnsAListOfAllRecordsFromACSVThatMatchTheCriteria() {
    List<String> lines = new ArrayList<>();
    lines.add("how-to-do-something,How to Do Something,Have you ever wanted to know how to do something?");
    lines.add("how-to-do-something,How to Do Something Else,Have you ever wanted to know how to do something else?");
    addLinesToPosts(lines);
    CsvDatabase database = new CsvDatabase();

    List<Map<String, String>> records = database.select("posts", "slug", "how-to-do-something");

    assertEquals("how-to-do-something", records.get(0).get("slug"));
    assertEquals("How to Do Something", records.get(0).get("title"));
    assertEquals("Have you ever wanted to know how to do something?", records.get(0).get("body"));
    assertEquals("how-to-do-something", records.get(1).get("slug"));
    assertEquals("How to Do Something Else", records.get(1).get("title"));
    assertEquals("Have you ever wanted to know how to do something else?", records.get(1).get("body"));
  }

  @Test
  public void itReturnsAnEmptyListIfNoRecordsMatchTheCriteria() {
    List<String> lines = new ArrayList<>();
    lines.add("how-to-do-something,How to Do Something,Have you ever wanted to know how to do something?");
    addLinesToPosts(lines);
    CsvDatabase database = new CsvDatabase();

    List<Map<String, String>> records = database.select("posts", "slug", "does-not-exist");

    assertEquals(0, records.size());
  }

  @Test
  public void itReturnsALimitedNumberOfRecordsWithMatchingCriteriaFromACSV() {
    List<String> lines = new ArrayList<>();
    lines.add("how-to-do-something-else,How to Do Something Else,Have you ever wanted to know how to do something else?");
    lines.add("how-to-do-something,How to Do Something Else,Have you ever wanted to know how to do something else?");
    addLinesToPosts(lines);
    CsvDatabase database = new CsvDatabase();

    List<Map<String, String>> records = database.select("posts", "slug", "how-to-do-something-else", 1);

    assertEquals(1, records.size());
    assertEquals("how-to-do-something-else", records.get(0).get("slug"));
    assertEquals("How to Do Something Else", records.get(0).get("title"));
    assertEquals("Have you ever wanted to know how to do something else?", records.get(0).get("body"));
  }

  @Test
  public void itInsertsANewRecordInACSV() throws IOException {
    CsvDatabase database = new CsvDatabase();
    Map<String, Object> data = Map.of(
        "slug", "how-to-do-another-thing",
        "title", "How to Do Another Thing",
        "body", "Have you ever tried to do another thing?"
    );

    database.insert("posts", data);

    byte[] fileContent = Files.readAllBytes(postsTable);
    assertEquals("slug,title,body\nhow-to-do-another-thing,How to Do Another Thing,Have you ever tried to do another thing?\n", new String(fileContent));
  }

  @Test
  public void itEscapesSpecialCharsWhenItInsertsANewRecordInACSV() throws IOException {
    CsvDatabase database = new CsvDatabase();
    Map<String, Object> data = Map.of(
        "slug", "how-to-do-another-thing",
        "title", "How, to Do Another Thing",
        "body","Have, you ever \"tried\" to do another thing?"
    );

    database.insert("posts", data);

    byte[] fileContent = Files.readAllBytes(postsTable);
    assertEquals("slug,title,body\nhow-to-do-another-thing,\"How, to Do Another Thing\",\"Have, you ever \"\"tried\"\" to do another thing?\"\n", new String(fileContent));
  }
}
