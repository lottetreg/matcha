package com.github.lottetreg.matcha;

import java.util.List;
import java.util.Map;

interface Persistable {
  void insert(String tableName, Map<String, Object> data);
  List<Map<String, String>> select(String tableName);
  List<Map<String, String>> select(String tableName, String attribute, Object value);
  List<Map<String, String>> select(String tableName, String attribute, Object value, Integer limit);
}
