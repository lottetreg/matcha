package com.github.lottetreg.matcha;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class CsvParser {
  private String filePath;

  CsvParser(String filePath) {
    this.filePath = filePath;
  }

  Iterable<CSVRecord> parse() throws IOException {
    Reader in = new FileReader(this.filePath);
    return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
  }
}
