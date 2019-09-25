package com.github.lottetreg.matcha;

import java.util.List;
import java.util.Map;

interface Persistable {
  List<Map<String, String>> selectAll(Class klass);
}
