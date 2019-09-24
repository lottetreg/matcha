package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

public interface Responsive {
  String getPath();

  String getMethod();

  Response getResponse(Request request);
}
