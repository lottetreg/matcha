package com.github.lottetreg.matcha;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class CsvDatabase implements Persistable {
  public List<Map<String, String>> select(String fileName) {
    Iterable<CSVRecord> records = parse(fileName + ".csv");

    List<Map<String, String>> results = new ArrayList<>();
    records.forEach((record) -> results.add(record.toMap()));

    return results;
  }

  public Map<String, String> select(String fileName, String field, String value) {
    Optional<Map<String, String>> match = select(fileName).stream()
        .filter((record) -> record.get(field).equals(value))
        .findFirst();

    return match.orElseThrow(() -> new NoRecordFound(fileName, field, value));
  }

  private Iterable<CSVRecord> parse(String filePath) {
    try {
      Reader in = new FileReader(filePath);
      return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  class NoRecordFound extends RuntimeException {
    NoRecordFound(String fileName, String field, String value) {
      super("No record found with " + field + " of " + value + " in " + fileName);
    }
  }
}
