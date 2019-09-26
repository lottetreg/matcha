package com.github.lottetreg.matcha;

import org.atteo.evo.inflector.English;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseModel {
  private static Persistable database = new CsvDatabase();

  public BaseModel(Map<String, String> data) {
    data.forEach(this::setField);
  }

  private void setField(String field, String value) {
    try {
      this.getClass().getDeclaredField(field).set(this, value);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> List<T> all(Class<T> klass) {
    try {
      Constructor<T> constructor = klass.getConstructor(Map.class);
      List<T> objects = new ArrayList<>();

      String tableName = English.plural(klass.getSimpleName()).toLowerCase();
      List<Map<String, String>> records = database.selectAll(tableName);

      for (Map<String, String> record : records) {
        T object = constructor.newInstance(record);
        objects.add(object);
      }

      return objects;

    } catch (NoSuchMethodException |
        InstantiationException |
        IllegalAccessException |
        InvocationTargetException e)
    {
      throw new RuntimeException(e);
    }
  }
}
