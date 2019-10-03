package com.github.lottetreg.matcha;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class BaseRoute implements Routable {
  private String path;
  private String method;
  private static final String paramSymbol = ":";

  public BaseRoute(String method, String path) {
    this.path = path;
    this.method = method;
  }

  public String getPath() {
    return this.path;
  }

  public String getMethod() {
    return this.method;
  }

  public Boolean hasPath(String requestPath) {
    String[] splitPath = splitPath(getPath());
    String[] splitRequestPath = splitPath(requestPath);

    return (splitPath.length == splitRequestPath.length) &&
        IntStream
            .range(0, splitPath.length)
            .allMatch((i) ->
                splitPath[i].startsWith(paramSymbol) || splitPath[i].equals(splitRequestPath[i])
            );
  }

  Map<String, String> getParams(String requestPath) {
    String[] splitPath = splitPath(getPath());
    String[] splitRequestPath = splitPath(requestPath);
    HashMap<String, String> params = new HashMap<>();

    IntStream
        .range(0, Math.min(splitPath.length, splitRequestPath.length))
        .filter((i) -> splitPath[i].startsWith(paramSymbol))
        .forEach((i) -> params.put(splitPath[i].substring(1), splitRequestPath[i]));

    return params;
  }

  private String[] splitPath(String path) {
    return path.split("/");
  }
}
