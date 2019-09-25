package com.github.lottetreg.matcha;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseModel {
  static <T> List<T> all(Class<T> klass, String table) throws Exception {
    Iterable<CSVRecord> records = new CsvParser(table).parse();
    List<T> objects = new ArrayList<>();

    for(CSVRecord record : records) {
      Constructor<T> constructor = klass.getConstructor(Map.class);
      T object = constructor.newInstance(record.toMap());
      objects.add(object);
    }

    return objects;
  }
}
