package com.github.lottetreg.matcha;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class CsvDatabaseTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

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
  public void itReturnsAListOfAllRecordsFromACSV() {
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
  public void itReturnsARecordWithMatchingCriteriaFromACSV() {
    CsvDatabase database = new CsvDatabase();

    Map<String, String> record = database.select("posts", "slug", "how-to-do-something-else");

    assertEquals("how-to-do-something-else", record.get("slug"));
    assertEquals("How to Do Something Else", record.get("title"));
    assertEquals("Have you ever wanted to know how to do something else?", record.get("body"));
  }

  @Test
  public void itThrowsAnExceptionIfItCannotFindAMatchingRecord() {
    CsvDatabase database = new CsvDatabase();

    exceptionRule.expect(CsvDatabase.NoRecordFound.class);
    exceptionRule.expectMessage("No record found with slug of i-do-not-exist in posts");

    database.select("posts", "slug", "i-do-not-exist");
  }
}
