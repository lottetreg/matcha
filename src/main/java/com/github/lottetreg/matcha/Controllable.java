package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

import java.util.Map;

interface Controllable {
  Controllable setRequest(Request request);

  Controllable setParams(Map<String, String> params);

  Map<String, String> getParams();

  Response call(String actionName);

  class MissingControllerAction extends RuntimeException {
    MissingControllerAction(String action, Throwable cause) {
      super(action, cause);
    }
  }

  class FailedToInvokeControllerAction extends RuntimeException {
    FailedToInvokeControllerAction(String action, Throwable cause) {
      super(action, cause);
    }
  }
}
