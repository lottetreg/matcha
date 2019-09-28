package com.github.lottetreg.matcha;

import java.util.stream.IntStream;

public abstract class BaseRoute implements Responsive {
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
    String[] pathA = getPath().split("/");
    String[] pathB = path.split("/");

    return (pathA.length == pathB.length) &&
        IntStream
            .range(0, Math.min(pathA.length, pathB.length))
            .allMatch((i) -> pathA[i].startsWith(paramSymbol) || pathA[i].equals(pathB[i]));
  }
}
