package com.github.lottetreg.matcha;

import org.atteo.evo.inflector.English;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Model {
  private static Persistable database = new CsvDatabase();

  public Model(Map<String, Object> data) {
    data.forEach(this::setField);
  }

  private void setField(String field, Object value) {
    try {
      this.getClass().getDeclaredField(field).set(this, value);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static Map<String, Object> toMap(Object resource) {
    List<Field> fields = List.of(resource.getClass().getDeclaredFields());
    HashMap<String, Object> attributes = new HashMap<>();

    fields.forEach(field -> {
      try {
        attributes.put(field.getName(), field.get(resource));
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    });

    return attributes;
  }

  public static <T> T create(T resource) {
    database.insert(getTableName(resource.getClass()), toMap(resource));
    return resource;
  }

  public static <T> List<T> all(Class<T> resourceClass) {
    return newInstances(resourceClass, database.select(getTableName(resourceClass)));
  }

  public static <T> List<T> findBy(Class<T> resourceClass, String attribute, Object value) {
    return newInstances(resourceClass, database.select(getTableName(resourceClass), attribute, value));
  }

  public static <T> T findFirstBy(Class<T> resourceClass, String attribute, Object value) {
    List<Map<String, String>> records = database.select(getTableName(resourceClass), attribute, value, 1);

    if(records.size() == 0) {
      throw new RecordNotFound(resourceClass.getSimpleName(), attribute, value.toString());
    } else {
      return newInstance(resourceClass, records.get(0));
    }
  }

  private static String getTableName(Class resourceClass) {
    return English.plural(resourceClass.getSimpleName()).toLowerCase();
  }

  private static <T> T newInstance(Class<T> resourceClass, Map<String, String> record) {
    try {
      Constructor<T> constructor = resourceClass.getConstructor(Map.class);
      return constructor.newInstance(record);

    } catch (NoSuchMethodException | InstantiationException |
        IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private static <T> List<T> newInstances(Class<T> resourceClass, List<Map<String, String>> records) {
    List<T> objects = new ArrayList<>();

    for (Map<String, String> record : records) {
      objects.add(newInstance(resourceClass, record));
    }

    return objects;
  }

  public static class RecordNotFound extends RuntimeException {
    RecordNotFound(String className, String field, String value) {
      super("Not found: " + className + " with " + field + " of " + value);
    }
  }
}
