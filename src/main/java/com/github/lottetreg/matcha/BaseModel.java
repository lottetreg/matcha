package com.github.lottetreg.matcha;

import org.apache.commons.csv.CSVRecord;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseModel {
  public BaseModel(Map<String, String> data) {
    data.forEach(this::setField);
  }

  private void setField(String field, String value) {
    try {
      Field declaredField = this.getClass().getDeclaredField(field);
      declaredField.set(this, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  static <T> List<T> all(Class<T> klass, String table) throws Exception {
    Iterable<CSVRecord> records = new CsvParser(table).parse(); // abstract out DB
    List<T> objects = new ArrayList<>();

    for(CSVRecord record : records) {
      Constructor<T> constructor = klass.getConstructor(Map.class);
      T object = constructor.newInstance(record.toMap());
      objects.add(object);
    }

    return objects;
  }
}
