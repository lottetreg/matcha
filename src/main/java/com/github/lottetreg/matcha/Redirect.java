package com.github.lottetreg.matcha;

import com.github.lottetreg.cup.Request;
import com.github.lottetreg.cup.Response;

import java.util.Map;

public class Redirect extends BaseRoute {
  private String redirectPath;

  public Redirect(String method, String path, String redirectPath) {
    super(method, path);
    this.redirectPath = redirectPath;
  }

  public Response getResponse(Request request) {
    String URI = "http://" + request.getHeader("Host") + getRedirectPath();

    return new Response(301, Map.of("Location", URI));
  }

  public String getRedirectPath() {
    return this.redirectPath;
  }
}
