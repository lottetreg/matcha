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

  public static <T> List<T> all(Class<T> resourceClass) {
    List<T> objects = new ArrayList<>();

    for (Map<String, String> record : database.select(tableName(resourceClass))) {
      objects.add(newInstance(resourceClass, record));
    }

    return objects;
  }

  public static <T> T findBy(Class<T> resourceClass, String attribute, String value) {
    return newInstance(
        resourceClass,
        database.select(tableName(resourceClass), attribute, value)
    );
  }

  private static String tableName(Class resourceClass) {
    return English.plural(resourceClass.getSimpleName()).toLowerCase();
  }

  private static <T> T newInstance(Class<T> resourceClass, Map<String, String> data) {
    try {
      Constructor<T> constructor = resourceClass.getConstructor(Map.class);
      return constructor.newInstance(data);

    } catch (NoSuchMethodException | InstantiationException |
        IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
