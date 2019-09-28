package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

// TODO: still need this? Can just use BaseRoute?
public interface Responsive {
  String getPath();

  String getMethod();

  Response getResponse(Request request);

  Boolean hasPath(String path);
}
