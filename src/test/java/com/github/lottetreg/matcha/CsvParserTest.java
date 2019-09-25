package com.github.lottetreg.matcha;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.StreamSupport;

import static junit.framework.TestCase.assertEquals;

public class CsvParserTest {
  @Test
  public void itParsesTheCSV() throws IOException {
    CsvParser csvParser = new CsvParser("src/test/java/com/github/lottetreg/matcha/support/posts.csv");

    Iterable<CSVRecord> records = csvParser.parse();

    CSVRecord firstRecord = StreamSupport.stream(records.spliterator(), false).findFirst().get();
    assertEquals("how-to-do-something", firstRecord.get("slug"));
    assertEquals("How to Do Something", firstRecord.get("title"));
    assertEquals("Have you ever wanted to know how to do something?", firstRecord.get("body"));
  }
}
