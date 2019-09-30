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

  public Boolean hasPath(String path) {
    String[] pathA = splitPath(getPath());
    String[] pathB = splitPath(path);

    return (pathA.length == pathB.length) &&
        IntStream
            .range(0, pathA.length)
            .allMatch((i) -> pathA[i].startsWith(paramSymbol) || pathA[i].equals(pathB[i]));
  }

  Map<String, String> getParams(String path) {
    String[] pathA = splitPath(getPath());
    String[] pathB = splitPath(path);
    HashMap<String, String> params = new HashMap<>();

    IntStream
        .range(0, Math.min(pathA.length, pathB.length))
        .filter((i) -> pathA[i].startsWith(paramSymbol))
        .forEach((i) -> params.put(pathA[i].substring(1), pathB[i]));

    return params;
  }

  private String[] splitPath(String path) {
    return path.split("/");
  }
}
