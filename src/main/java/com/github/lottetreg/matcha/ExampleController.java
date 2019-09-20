package com.github.lottetreg.matcha;

import java.nio.file.Path;
import java.time.Instant;

public class ExampleController extends BaseController {
  public void empty() {
  }

  public String echo() {
    return getRequest().getBody();
  }

  public Path pickles() {
    return Path.of("/pickles.jpg");
  }

  public Path picklesWithHeader() {
    addHeader("Some-Header", "HI");
    return Path.of("/pickles.jpg");
  }

  public Template time() {
    addData("currentTime", Instant.now().toString());
    return new Template("/time.html");
  }
}
