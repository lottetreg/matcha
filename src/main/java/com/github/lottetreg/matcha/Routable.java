package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

import java.util.Map;

public interface Routable {
  String getPath();

  String getMethod();

  Response getResponse(Request request);

  Boolean hasPath(String path);
}
