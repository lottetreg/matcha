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

public class CsvDatabaseTest {

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
  public void itReturnsAListOfAllRecordsOfAClass() {
    CsvDatabase database = new CsvDatabase();

    List<Map<String, String>> records = database.selectAll("posts");

    assertEquals("how-to-do-something", records.get(0).get("slug"));
    assertEquals("How to Do Something", records.get(0).get("title"));
    assertEquals("Have you ever wanted to know how to do something?", records.get(0).get("body"));
  }
}
