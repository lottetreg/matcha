package com.github.lottetreg.matcha;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class CsvDatabase implements Persistable {
  public List<Map<String, String>> selectAll(String csvFileName) {
    Iterable<CSVRecord> records = parse(csvFileName + ".csv");

    List<Map<String, String>> results = new ArrayList<>();
    for (CSVRecord record : records) {
      results.add(record.toMap());
    }

    return results;
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
