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
import java.util.stream.StreamSupport;

class CsvDatabase implements Persistable {
  public List<Map<String, String>> selectAll(String fileName) {
    Iterable<CSVRecord> records = parse(fileName + ".csv");

    List<Map<String, String>> results = new ArrayList<>();
    for (CSVRecord record : records) {
      results.add(record.toMap());
    }

    return results;
  }

  // TODO: Dry up these methods
  public Map<String, String> selectFirstWhere(String fileName, String field, String value) {
    Iterable<CSVRecord> records = parse(fileName + ".csv");

    Optional<CSVRecord> match = StreamSupport.stream(records.spliterator(), false)
        .filter((record) -> record.get(field).equals(value))
        .findFirst();

    CSVRecord record = match.orElseThrow(RuntimeException::new); // TODO: more explicit

    return record.toMap();
  }

  private Iterable<CSVRecord> parse(String filePath) {
    try {
      Reader in = new FileReader(filePath);
      return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
