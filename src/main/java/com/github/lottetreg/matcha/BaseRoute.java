package com.github.lottetreg.matcha;

public abstract class BaseRoute implements Responsive {
  private String path;
  private String method;

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
}
