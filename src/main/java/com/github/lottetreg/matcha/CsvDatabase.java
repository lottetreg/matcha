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
  public List<Map<String, String>> selectAll(Class klass) {
    try {
      String tableName = klass.getField("tableName").get(null).toString();
      String csvFileName = tableName + ".csv";
      Iterable<CSVRecord> records = parse(csvFileName);

      List<Map<String, String>> results = new ArrayList<>();

      for (CSVRecord record : records) {
        results.add(record.toMap());
      }

      return results;

    } catch (NoSuchFieldException |
        IllegalAccessException e)
    {
      throw new RuntimeException(e);
    }
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
