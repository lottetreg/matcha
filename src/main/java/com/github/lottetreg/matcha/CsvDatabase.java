package com.github.lottetreg.matcha;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

class CsvDatabase implements Persistable {
  public void insert(String tableName, Map<String, Object> data) {
    try {
      String csvName = tableName + ".csv";
      Map<String, Integer> headerMap = CSVFormat.DEFAULT.withFirstRecordAsHeader()
          .parse(new FileReader(csvName))
          .getHeaderMap();

      ArrayList<String> recordValues = new ArrayList<>();
      headerMap.forEach((header, i) -> {
        String value = "\"" + data.get(header).toString() + "\"";
        recordValues.add(i, value);
      });

      FileWriter csvWriter = new FileWriter(csvName, true);
      String value = String.join(",", recordValues);
      csvWriter.append(value);
      csvWriter.append("\n");
      csvWriter.flush();
      csvWriter.close();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<Map<String, String>> select(String tableName) {
    Iterable<CSVRecord> records = parse(tableName + ".csv");

    List<Map<String, String>> results = new ArrayList<>();
    records.forEach((record) -> results.add(record.toMap()));

    return results;
  }

  public List<Map<String, String>> select(String tableName, String field, Object value) {
    return select(tableName).stream()
        .filter((record) -> record.get(field).equals(value.toString()))
        .collect(Collectors.toList());
  }

  public List<Map<String, String>> select(String tableName, String field, Object value, Integer limit) {
    return select(tableName, field, value).stream()
        .limit(limit)
        .collect(Collectors.toList());
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
