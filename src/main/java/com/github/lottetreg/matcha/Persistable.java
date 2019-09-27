package com.github.lottetreg.matcha;

import java.util.List;
import java.util.Map;

interface Persistable {
  List<Map<String, String>> select(String tableName);
  Map<String, String> select(String tableName, String attribute, String value);
}
