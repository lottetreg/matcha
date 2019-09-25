package com.github.lottetreg.matcha;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class CsvDatabaseTest {

  @Test
  public void itReturnsAListOfAllRecordsOfAClass() {
    CsvDatabase database = new CsvDatabase();

    List<Map<String, String>> records = database.selectAll("posts"); // can create and write to /posts.csv file from in here?

    assertEquals("how-to-do-something", records.get(0).get("slug"));
    assertEquals("How to Do Something", records.get(0).get("title"));
    assertEquals("Have you ever wanted to know how to do something?", records.get(0).get("body"));
  }
}
